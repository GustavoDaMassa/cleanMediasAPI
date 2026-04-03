package br.com.gustavohenrique.cleanmediasapi.domain.assessment;

import br.com.gustavohenrique.cleanmediasapi.domain.exception.NotFoundException;

public class AssessmentNotFoundException extends NotFoundException {

    public AssessmentNotFoundException(Long id) {
        super("Assessment with id " + id + " not found");
    }

    public AssessmentNotFoundException(Long id, Long projectionId) {
        super("Assessment with id " + id + " not found for projection " + projectionId);
    }
}
