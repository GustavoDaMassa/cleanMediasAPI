package br.com.gustavohenrique.cleanmediasapi.application.projection;

import br.com.gustavohenrique.cleanmediasapi.application.assessment.AssessmentService;
import br.com.gustavohenrique.cleanmediasapi.application.projection.dto.*;
import br.com.gustavohenrique.cleanmediasapi.domain.course.Course;
import br.com.gustavohenrique.cleanmediasapi.domain.exception.BusinessException;
import br.com.gustavohenrique.cleanmediasapi.domain.projection.Projection;
import br.com.gustavohenrique.cleanmediasapi.domain.projection.ProjectionNotFoundException;
import br.com.gustavohenrique.cleanmediasapi.domain.projection.ProjectionRepository;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectionServiceImplTest {

    @Mock ProjectionRepository projectionRepository;
    @Mock AssessmentService assessmentService;
    @InjectMocks ProjectionServiceImpl projectionService;

    private final Course course = new Course(1L, 10L, "Cálculo I", "(AV1+AV2)/2", 6.0);
    private final Projection projection = new Projection(1L, 1L, "Cálculo I", 0.0);

    // --- initializeForCourse ---

    @Test
    void shouldCreateDefaultProjectionForCourse() {
        when(projectionRepository.save(any())).thenReturn(projection);

        projectionService.initializeForCourse(course);

        verify(projectionRepository).save(any());
        verify(assessmentService).initializeForProjection(eq(projection), eq("(AV1+AV2)/2"));
    }

    // --- deleteAllByCourse ---

    @Test
    void shouldDeleteAllProjectionsForCourse() {
        projectionService.deleteAllByCourse(1L);

        verify(projectionRepository).deleteAllByCourseId(1L);
    }

    // --- create ---

    @Test
    void shouldCreateProjection() {
        Projection saved = new Projection(2L, 1L, "Projeção Extra", 0.0);
        when(projectionRepository.existsByNameAndCourseId("Projeção Extra", 1L)).thenReturn(false);
        when(projectionRepository.save(any())).thenReturn(saved);

        // Para criar, precisa do curso para saber o averageMethod
        ProjectionResponse response = projectionService.create(course,
                new CreateProjectionRequest("Projeção Extra"));

        assertThat(response.name()).isEqualTo("Projeção Extra");
        verify(assessmentService).initializeForProjection(eq(saved), eq("(AV1+AV2)/2"));
    }

    @Test
    void shouldThrowWhenProjectionNameAlreadyExists() {
        when(projectionRepository.existsByNameAndCourseId("Cálculo I", 1L)).thenReturn(true);

        assertThatThrownBy(() -> projectionService.create(course,
                new CreateProjectionRequest("Cálculo I")))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Projection name already in use");
    }

    // --- listByCourse ---

    @Test
    void shouldListProjectionsByCourse() {
        when(projectionRepository.findAllByCourseId(1L)).thenReturn(List.of(projection));

        List<ProjectionResponse> result = projectionService.listByCourse(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Cálculo I");
    }

    // --- listAllByUser ---

    @Test
    void shouldListAllProjectionsByUser() {
        when(projectionRepository.findAllByUserId(10L)).thenReturn(List.of(projection));

        List<ProjectionResponse> result = projectionService.listAllByUser(10L);

        assertThat(result).hasSize(1);
    }

    // --- updateName ---

    @Test
    void shouldUpdateProjectionName() {
        Projection updated = new Projection(1L, 1L, "Nova Projeção", 0.0);
        when(projectionRepository.findByIdAndCourseId(1L, 1L)).thenReturn(Optional.of(projection));
        when(projectionRepository.existsByNameAndCourseId("Nova Projeção", 1L)).thenReturn(false);
        when(projectionRepository.save(any())).thenReturn(updated);

        ProjectionResponse response = projectionService.updateName(1L, 1L,
                new UpdateProjectionNameRequest("Nova Projeção"));

        assertThat(response.name()).isEqualTo("Nova Projeção");
    }

    @Test
    void shouldThrowWhenProjectionNotFoundOnUpdateName() {
        when(projectionRepository.findByIdAndCourseId(99L, 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectionService.updateName(1L, 99L,
                new UpdateProjectionNameRequest("Novo")))
                .isInstanceOf(ProjectionNotFoundException.class);
    }

    @Test
    void shouldThrowWhenNewNameAlreadyInUseOnUpdate() {
        when(projectionRepository.findByIdAndCourseId(1L, 1L)).thenReturn(Optional.of(projection));
        when(projectionRepository.existsByNameAndCourseId("Outro", 1L)).thenReturn(true);

        assertThatThrownBy(() -> projectionService.updateName(1L, 1L,
                new UpdateProjectionNameRequest("Outro")))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Projection name already in use");
    }

    // --- delete ---

    @Test
    void shouldDeleteProjection() {
        when(projectionRepository.findByIdAndCourseId(1L, 1L)).thenReturn(Optional.of(projection));

        projectionService.delete(1L, 1L);

        verify(projectionRepository).deleteByIdAndCourseId(1L, 1L);
    }

    @Test
    void shouldThrowWhenProjectionNotFoundOnDelete() {
        when(projectionRepository.findByIdAndCourseId(99L, 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> projectionService.delete(1L, 99L))
                .isInstanceOf(ProjectionNotFoundException.class);
    }
}
