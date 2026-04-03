package br.com.gustavohenrique.cleanmediasapi.application.course;

import br.com.gustavohenrique.cleanmediasapi.application.course.dto.*;
import br.com.gustavohenrique.cleanmediasapi.application.projection.ProjectionService;
import br.com.gustavohenrique.cleanmediasapi.domain.course.Course;
import br.com.gustavohenrique.cleanmediasapi.domain.course.CourseNotFoundException;
import br.com.gustavohenrique.cleanmediasapi.domain.course.CourseRepository;
import br.com.gustavohenrique.cleanmediasapi.domain.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock CourseRepository courseRepository;
    @Mock ProjectionService projectionService;
    @InjectMocks CourseServiceImpl courseService;

    private final Course course = new Course(1L, 10L, "Cálculo I", "(AV1+AV2)/2", 6.0);

    // --- create ---

    @Test
    void shouldCreateCourseAndInitializeProjection() {
        when(courseRepository.existsByNameAndUserId("Cálculo I", 10L)).thenReturn(false);
        when(courseRepository.save(any())).thenReturn(course);

        CourseResponse response = courseService.create(10L,
                new CreateCourseRequest("Cálculo I", "(AV1+AV2)/2", 6.0));

        assertThat(response.name()).isEqualTo("Cálculo I");
        verify(projectionService).initializeForCourse(course);
    }

    @Test
    void shouldThrowWhenCourseNameAlreadyExistsForUser() {
        when(courseRepository.existsByNameAndUserId("Cálculo I", 10L)).thenReturn(true);

        assertThatThrownBy(() -> courseService.create(10L,
                new CreateCourseRequest("Cálculo I", "(AV1+AV2)/2", 6.0)))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Course name already in use");
    }

    // --- listAll ---

    @Test
    void shouldListCoursesForUser() {
        when(courseRepository.findAllByUserId(10L)).thenReturn(List.of(course));

        List<CourseResponse> result = courseService.listAll(10L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Cálculo I");
    }

    // --- updateName ---

    @Test
    void shouldUpdateCourseName() {
        Course updated = new Course(1L, 10L, "Cálculo II", "(AV1+AV2)/2", 6.0);
        when(courseRepository.findByIdAndUserId(1L, 10L)).thenReturn(Optional.of(course));
        when(courseRepository.existsByNameAndUserId("Cálculo II", 10L)).thenReturn(false);
        when(courseRepository.save(any())).thenReturn(updated);

        CourseResponse response = courseService.updateName(10L, 1L,
                new UpdateCourseNameRequest("Cálculo II"));

        assertThat(response.name()).isEqualTo("Cálculo II");
    }

    @Test
    void shouldThrowWhenCourseNotFoundOnUpdateName() {
        when(courseRepository.findByIdAndUserId(99L, 10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.updateName(10L, 99L,
                new UpdateCourseNameRequest("Novo")))
                .isInstanceOf(CourseNotFoundException.class);
    }

    @Test
    void shouldThrowWhenNewNameAlreadyInUseOnUpdate() {
        when(courseRepository.findByIdAndUserId(1L, 10L)).thenReturn(Optional.of(course));
        when(courseRepository.existsByNameAndUserId("Outro", 10L)).thenReturn(true);

        assertThatThrownBy(() -> courseService.updateName(10L, 1L,
                new UpdateCourseNameRequest("Outro")))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Course name already in use");
    }

    // --- updateAverageMethod ---

    @Test
    void shouldUpdateAverageMethodAndResetProjections() {
        Course updated = new Course(1L, 10L, "Cálculo I", "(AV1+AV2+AV3)/3", 6.0);
        when(courseRepository.findByIdAndUserId(1L, 10L)).thenReturn(Optional.of(course));
        when(courseRepository.save(any())).thenReturn(updated);

        CourseResponse response = courseService.updateAverageMethod(10L, 1L,
                new UpdateAverageMethodRequest("(AV1+AV2+AV3)/3"));

        assertThat(response.averageMethod()).isEqualTo("(AV1+AV2+AV3)/3");
        verify(projectionService).deleteAllByCourse(1L);
        verify(projectionService).initializeForCourse(updated);
    }

    // --- updateCutOffGrade ---

    @Test
    void shouldUpdateCutOffGrade() {
        Course updated = new Course(1L, 10L, "Cálculo I", "(AV1+AV2)/2", 7.0);
        when(courseRepository.findByIdAndUserId(1L, 10L)).thenReturn(Optional.of(course));
        when(courseRepository.save(any())).thenReturn(updated);

        CourseResponse response = courseService.updateCutOffGrade(10L, 1L,
                new UpdateCutOffGradeRequest(7.0));

        assertThat(response.cutOffGrade()).isEqualTo(7.0);
    }

    // --- delete ---

    @Test
    void shouldDeleteCourse() {
        when(courseRepository.findByIdAndUserId(1L, 10L)).thenReturn(Optional.of(course));

        courseService.delete(10L, 1L);

        verify(courseRepository).deleteByIdAndUserId(1L, 10L);
    }

    @Test
    void shouldThrowWhenCourseNotFoundOnDelete() {
        when(courseRepository.findByIdAndUserId(99L, 10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.delete(10L, 99L))
                .isInstanceOf(CourseNotFoundException.class);
    }
}
