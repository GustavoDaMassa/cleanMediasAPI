package br.com.gustavohenrique.cleanmediasapi.application.assessment;

import br.com.gustavohenrique.cleanmediasapi.domain.projection.Projection;

// Contrato mínimo necessário pelo ProjectionService.
// Será expandido quando o domínio Assessment for implementado.
public interface AssessmentService {

    // Parseia a fórmula do curso e cria os assessments para a projeção
    void initializeForProjection(Projection projection, String averageMethod);
}
