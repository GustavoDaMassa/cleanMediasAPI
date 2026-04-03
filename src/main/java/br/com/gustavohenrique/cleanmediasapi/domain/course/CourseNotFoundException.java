package br.com.gustavohenrique.cleanmediasapi.domain.course;

import br.com.gustavohenrique.cleanmediasapi.domain.exception.NotFoundException;

public class CourseNotFoundException extends NotFoundException {

    public CourseNotFoundException(Long id) {
        super("Course with id " + id + " not found");
    }

    public CourseNotFoundException(Long id, Long userId) {
        super("Course with id " + id + " not found for user " + userId);
    }
}
