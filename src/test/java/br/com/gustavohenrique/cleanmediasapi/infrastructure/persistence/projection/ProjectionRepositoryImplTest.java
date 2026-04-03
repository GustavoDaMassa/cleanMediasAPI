package br.com.gustavohenrique.cleanmediasapi.infrastructure.persistence.projection;

import br.com.gustavohenrique.cleanmediasapi.domain.projection.Projection;
import br.com.gustavohenrique.cleanmediasapi.infrastructure.persistence.course.CourseJpaEntity;
import br.com.gustavohenrique.cleanmediasapi.infrastructure.persistence.course.CourseRepositoryImpl;
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
@Import(ProjectionRepositoryImpl.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
class ProjectionRepositoryImplTest {

    @Autowired
    private ProjectionRepositoryImpl projectionRepository;

    @Autowired
    private TestEntityManager em;

    private Long courseId;

    @BeforeEach
    void setUp() {
        CourseJpaEntity course = new CourseJpaEntity();
        // usa reflexão via TestEntityManager para persistir sem expor construtor
        courseId = em.persistAndGetId(
                buildCourse(1L, "Cálculo I"), Long.class);
        em.flush();
    }

    private CourseJpaEntity buildCourse(Long userId, String name) {
        // CourseJpaEntity tem fromDomain, mas precisa de Course domain
        br.com.gustavohenrique.cleanmediasapi.domain.course.Course c =
                new br.com.gustavohenrique.cleanmediasapi.domain.course.Course(
                        userId, name, "(AV1+AV2)/2", 6.0);
        return CourseJpaEntity.fromDomain(c);
    }

    @Test
    void shouldSaveAndFindByIdAndCourseId() {
        Projection saved = projectionRepository.save(new Projection(courseId, "Extra"));

        assertThat(saved.getId()).isNotNull();
        assertThat(projectionRepository.findByIdAndCourseId(saved.getId(), courseId)).isPresent();
        assertThat(projectionRepository.findByIdAndCourseId(saved.getId(), 999L)).isEmpty();
    }

    @Test
    void shouldFindAllByCourseId() {
        projectionRepository.save(new Projection(courseId, "P1"));
        projectionRepository.save(new Projection(courseId, "P2"));

        List<Projection> result = projectionRepository.findAllByCourseId(courseId);
        assertThat(result).hasSize(2);
    }

    @Test
    void shouldDeleteByIdAndCourseId() {
        Projection saved = projectionRepository.save(new Projection(courseId, "Para deletar"));

        projectionRepository.deleteByIdAndCourseId(saved.getId(), courseId);

        assertThat(projectionRepository.findByIdAndCourseId(saved.getId(), courseId)).isEmpty();
    }

    @Test
    void shouldDeleteAllByCourseId() {
        projectionRepository.save(new Projection(courseId, "P1"));
        projectionRepository.save(new Projection(courseId, "P2"));

        projectionRepository.deleteAllByCourseId(courseId);

        assertThat(projectionRepository.findAllByCourseId(courseId)).isEmpty();
    }
}
