package br.com.gustavohenrique.cleanmediasapi.application.course.dto;

import br.com.gustavohenrique.cleanmediasapi.domain.course.Course;

public record CourseResponse(Long id, String name, String averageMethod, double cutOffGrade) {

    public static CourseResponse from(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getName(),
                course.getAverageMethod(),
                course.getCutOffGrade()
        );
    }
}
