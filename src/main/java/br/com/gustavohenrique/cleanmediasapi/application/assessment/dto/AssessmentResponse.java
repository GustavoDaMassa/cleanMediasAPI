package br.com.gustavohenrique.cleanmediasapi.application.assessment.dto;

import br.com.gustavohenrique.cleanmediasapi.domain.assessment.Assessment;

public record AssessmentResponse(
        Long id,
        Long projectionId,
        String identifier,
        double maxValue,
        double grade,
        double requiredGrade,
        boolean fixed
) {
    public static AssessmentResponse from(Assessment a) {
        return new AssessmentResponse(
                a.getId(), a.getProjectionId(), a.getIdentifier(),
                a.getMaxValue(), a.getGrade(), a.getRequiredGrade(), a.isFixed()
        );
    }
}
