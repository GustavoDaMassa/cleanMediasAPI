package br.com.gustavohenrique.cleanmediasapi.application.projection.dto;

import br.com.gustavohenrique.cleanmediasapi.domain.projection.Projection;

public record ProjectionResponse(Long id, Long courseId, String name, double finalGrade) {

    public static ProjectionResponse from(Projection projection) {
        return new ProjectionResponse(
                projection.getId(),
                projection.getCourseId(),
                projection.getName(),
                projection.getFinalGrade()
        );
    }
}
