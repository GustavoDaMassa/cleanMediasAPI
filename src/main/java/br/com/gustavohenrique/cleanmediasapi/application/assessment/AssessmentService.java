package br.com.gustavohenrique.cleanmediasapi.application.assessment;

import br.com.gustavohenrique.cleanmediasapi.application.assessment.dto.AssessmentResponse;
import br.com.gustavohenrique.cleanmediasapi.domain.projection.Projection;

import java.util.List;

public interface AssessmentService {

    // Parseia a fórmula do curso e cria os assessments para a projeção
    void initializeForProjection(Projection projection, String averageMethod);

    // Remove todos os assessments de uma projeção (usado ao deletar projeção)
    void deleteAllByProjectionId(Long projectionId);

    List<AssessmentResponse> findAllByProjectionId(Long projectionId);

    AssessmentResponse findByIdAndProjectionId(Long id, Long projectionId);

    // Aplica nota, persiste, recalcula finalGrade da projeção via RPN e retorna o assessment atualizado
    AssessmentResponse applyGrade(Long projectionId, Long assessmentId, double grade, String averageMethod);
}
