package br.com.gustavohenrique.cleanmediasapi.domain.assessment;

import java.util.List;
import java.util.Optional;

public interface AssessmentRepository {

    Assessment save(Assessment assessment);

    List<Assessment> saveAll(List<Assessment> assessments);

    Optional<Assessment> findByIdAndProjectionId(Long id, Long projectionId);

    List<Assessment> findAllByProjectionId(Long projectionId);

    boolean existsByIdentifierAndProjectionId(String identifier, Long projectionId);

    void deleteAllByProjectionId(Long projectionId);
}
