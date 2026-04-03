package br.com.gustavohenrique.cleanmediasapi.application.assessment;

import br.com.gustavohenrique.cleanmediasapi.application.assessment.dto.AssessmentResponse;
import br.com.gustavohenrique.cleanmediasapi.domain.assessment.Assessment;
import br.com.gustavohenrique.cleanmediasapi.domain.assessment.AssessmentNotFoundException;
import br.com.gustavohenrique.cleanmediasapi.domain.assessment.AssessmentRepository;
import br.com.gustavohenrique.cleanmediasapi.domain.exception.BusinessException;
import br.com.gustavohenrique.cleanmediasapi.domain.projection.Projection;
import br.com.gustavohenrique.cleanmediasapi.domain.projection.ProjectionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssessmentServiceImplTest {

    @Mock
    private AssessmentRepository assessmentRepository;

    @Mock
    private ProjectionRepository projectionRepository;

    @InjectMocks
    private AssessmentServiceImpl assessmentService;

    // ----- initializeForProjection -----

    @Test
    void shouldCreateAssessmentsFromFormula() {
        // fórmula: AV1[10] + AV2[8] → 2 assessments criados
        Projection projection = new Projection(1L, 10L, "P1", 0.0);
        when(assessmentRepository.saveAll(anyList())).thenAnswer(i -> i.getArgument(0));

        assessmentService.initializeForProjection(projection, "AV1[10]+AV2[8]");

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<Assessment>> captor = ArgumentCaptor.forClass(List.class);
        verify(assessmentRepository).saveAll(captor.capture());

        List<Assessment> saved = captor.getValue();
        assertThat(saved).hasSize(2);
        assertThat(saved).extracting(Assessment::getIdentifier)
                .containsExactlyInAnyOrder("AV1", "AV2");
        assertThat(saved).extracting(Assessment::getMaxValue)
                .containsExactlyInAnyOrder(10.0, 8.0);
    }

    @Test
    void shouldCreateAssessmentsWithDefaultMaxValue() {
        // fórmula sem colchete → maxValue padrão 10.0
        Projection projection = new Projection(1L, 10L, "P1", 0.0);
        when(assessmentRepository.saveAll(anyList())).thenAnswer(i -> i.getArgument(0));

        assessmentService.initializeForProjection(projection, "(AV1+AV2)/2");

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<Assessment>> captor = ArgumentCaptor.forClass(List.class);
        verify(assessmentRepository).saveAll(captor.capture());

        assertThat(captor.getValue()).extracting(Assessment::getMaxValue)
                .containsOnly(10.0);
    }

    // ----- deleteAllByProjectionId -----

    @Test
    void shouldDeleteAllAssessmentsOfProjection() {
        assessmentService.deleteAllByProjectionId(5L);

        verify(assessmentRepository).deleteAllByProjectionId(5L);
    }

    // ----- findAllByProjectionId -----

    @Test
    void shouldReturnAllAssessmentsOfProjection() {
        Assessment a = new Assessment(1L, 5L, "AV1", 8.0, 10.0, 0.0, true);
        when(assessmentRepository.findAllByProjectionId(5L)).thenReturn(List.of(a));

        List<AssessmentResponse> result = assessmentService.findAllByProjectionId(5L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).identifier()).isEqualTo("AV1");
        assertThat(result.get(0).grade()).isEqualTo(8.0);
    }

    // ----- findByIdAndProjectionId -----

    @Test
    void shouldReturnAssessmentByIdAndProjection() {
        Assessment a = new Assessment(1L, 5L, "AV1", 8.0, 10.0, 0.0, true);
        when(assessmentRepository.findByIdAndProjectionId(1L, 5L)).thenReturn(Optional.of(a));

        AssessmentResponse result = assessmentService.findByIdAndProjectionId(1L, 5L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.identifier()).isEqualTo("AV1");
    }

    @Test
    void shouldThrowNotFoundWhenAssessmentDoesNotExist() {
        when(assessmentRepository.findByIdAndProjectionId(99L, 5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> assessmentService.findByIdAndProjectionId(99L, 5L))
                .isInstanceOf(AssessmentNotFoundException.class);
    }

    // ----- applyGrade -----

    @Test
    void shouldApplyGradeAndRecalculateFinalGrade() {
        // projeção com 2 assessments: AV1=0 (vai virar 8), AV2=6 (já fixo)
        // fórmula: (AV1+AV2)/2 → finalGrade = (8+6)/2 = 7.0
        Assessment target = new Assessment(1L, 5L, "AV1", 0.0, 10.0, 0.0, false);
        Assessment other  = new Assessment(2L, 5L, "AV2", 6.0, 10.0, 0.0, true);
        Projection projection = new Projection(5L, 10L, "P1", 0.0);

        when(assessmentRepository.findByIdAndProjectionId(1L, 5L)).thenReturn(Optional.of(target));
        when(assessmentRepository.findAllByProjectionId(5L)).thenReturn(List.of(target, other));
        when(assessmentRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(projectionRepository.findById(5L)).thenReturn(Optional.of(projection));
        when(projectionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        AssessmentResponse result = assessmentService.applyGrade(5L, 1L, 8.0, "(AV1+AV2)/2");

        assertThat(result.grade()).isEqualTo(8.0);
        assertThat(result.fixed()).isTrue();

        // verifica que o finalGrade da projeção foi atualizado
        ArgumentCaptor<Projection> projCaptor = ArgumentCaptor.forClass(Projection.class);
        verify(projectionRepository).save(projCaptor.capture());
        assertThat(projCaptor.getValue().getFinalGrade()).isCloseTo(7.0, within(0.001));
    }

    @Test
    void shouldThrowBusinessExceptionWhenGradeExceedsMax() {
        Assessment target = new Assessment(1L, 5L, "AV1", 0.0, 10.0, 0.0, false);
        when(assessmentRepository.findByIdAndProjectionId(1L, 5L)).thenReturn(Optional.of(target));

        assertThatThrownBy(() -> assessmentService.applyGrade(5L, 1L, 15.0, "(AV1)/1"))
                .isInstanceOf(BusinessException.class);
    }
}
