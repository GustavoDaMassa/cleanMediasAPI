package br.com.gustavohenrique.cleanmediasapi.domain.projection;

import br.com.gustavohenrique.cleanmediasapi.domain.exception.NotFoundException;

public class ProjectionNotFoundException extends NotFoundException {

    public ProjectionNotFoundException(Long id) {
        super("Projection with id " + id + " not found");
    }

    public ProjectionNotFoundException(Long id, Long courseId) {
        super("Projection with id " + id + " not found for course " + courseId);
    }
}
