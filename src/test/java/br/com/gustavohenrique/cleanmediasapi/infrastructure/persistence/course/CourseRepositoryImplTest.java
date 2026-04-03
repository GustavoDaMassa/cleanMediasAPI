package br.com.gustavohenrique.cleanmediasapi.infrastructure.persistence.course;

import br.com.gustavohenrique.cleanmediasapi.domain.course.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(CourseRepositoryImpl.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
class CourseRepositoryImplTest {

    @Autowired
    private CourseRepositoryImpl courseRepository;

    private Course saved(Long userId) {
        return courseRepository.save(new Course(userId, "Cálculo I", "(AV1+AV2)/2", 6.0));
    }

    @Test
    void shouldSaveAndFindByIdAndUserId() {
        Course course = saved(1L);

        assertThat(course.getId()).isNotNull();
        assertThat(courseRepository.findByIdAndUserId(course.getId(), 1L)).isPresent();
        assertThat(courseRepository.findByIdAndUserId(course.getId(), 99L)).isEmpty();
    }

    @Test
    void shouldFindAllByUserId() {
        saved(2L);
        saved(2L);
        saved(3L);

        List<Course> courses = courseRepository.findAllByUserId(2L);
        assertThat(courses).hasSize(2);
    }

    @Test
    void shouldReturnTrueWhenNameExistsForUser() {
        saved(4L);

        assertThat(courseRepository.existsByNameAndUserId("Cálculo I", 4L)).isTrue();
        assertThat(courseRepository.existsByNameAndUserId("Cálculo I", 99L)).isFalse();
    }

    @Test
    void shouldDeleteByIdAndUserId() {
        Course course = saved(5L);

        courseRepository.deleteByIdAndUserId(course.getId(), 5L);

        assertThat(courseRepository.findByIdAndUserId(course.getId(), 5L)).isEmpty();
    }
}
