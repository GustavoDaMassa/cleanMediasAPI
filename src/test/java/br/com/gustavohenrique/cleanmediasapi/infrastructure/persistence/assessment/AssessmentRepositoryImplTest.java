package br.com.gustavohenrique.cleanmediasapi.infrastructure.persistence.assessment;

import br.com.gustavohenrique.cleanmediasapi.domain.assessment.Assessment;
import br.com.gustavohenrique.cleanmediasapi.infrastructure.persistence.course.CourseJpaEntity;
import br.com.gustavohenrique.cleanmediasapi.infrastructure.persistence.projection.ProjectionJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(AssessmentRepositoryImpl.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
class AssessmentRepositoryImplTest {

    @Autowired
    private AssessmentRepositoryImpl assessmentRepository;

    @Autowired
    private TestEntityManager em;

    private Long projectionId;

    @BeforeEach
    void setUp() {
        CourseJpaEntity course = CourseJpaEntity.fromDomain(
                new br.com.gustavohenrique.cleanmediasapi.domain.course.Course(
                        1L, "Física", "(AV1+AV2)/2", 5.0));
        Long courseId = em.persistAndGetId(course, Long.class);

        ProjectionJpaEntity projection = ProjectionJpaEntity.fromDomain(
                new br.com.gustavohenrique.cleanmediasapi.domain.projection.Projection(
                        courseId, "Aprovação"));
        projectionId = em.persistAndGetId(projection, Long.class);
        em.flush();
    }

    @Test
    void shouldSaveAndFindByIdAndProjectionId() {
        Assessment saved = assessmentRepository.save(new Assessment(projectionId, "AV1", 10.0));

        assertThat(saved.getId()).isNotNull();
        assertThat(assessmentRepository.findByIdAndProjectionId(saved.getId(), projectionId)).isPresent();
        assertThat(assessmentRepository.findByIdAndProjectionId(saved.getId(), 999L)).isEmpty();
    }

    @Test
    void shouldSaveAllAndFindAllByProjectionId() {
        List<Assessment> assessments = List.of(
                new Assessment(projectionId, "AV1", 10.0),
                new Assessment(projectionId, "AV2", 8.0)
        );

        assessmentRepository.saveAll(assessments);

        List<Assessment> found = assessmentRepository.findAllByProjectionId(projectionId);
        assertThat(found).hasSize(2);
        assertThat(found).extracting(Assessment::getIdentifier)
                .containsExactlyInAnyOrder("AV1", "AV2");
    }

    @Test
    void shouldReturnTrueWhenIdentifierExistsInProjection() {
        assessmentRepository.save(new Assessment(projectionId, "AV1", 10.0));

        assertThat(assessmentRepository.existsByIdentifierAndProjectionId("AV1", projectionId)).isTrue();
        assertThat(assessmentRepository.existsByIdentifierAndProjectionId("AV2", projectionId)).isFalse();
    }

    @Test
    void shouldDeleteAllByProjectionId() {
        assessmentRepository.saveAll(List.of(
                new Assessment(projectionId, "AV1", 10.0),
                new Assessment(projectionId, "AV2", 8.0)
        ));

        assessmentRepository.deleteAllByProjectionId(projectionId);

        assertThat(assessmentRepository.findAllByProjectionId(projectionId)).isEmpty();
    }
}
