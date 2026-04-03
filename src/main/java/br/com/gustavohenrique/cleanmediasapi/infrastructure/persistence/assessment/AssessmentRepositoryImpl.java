package br.com.gustavohenrique.cleanmediasapi.infrastructure.persistence.assessment;

import br.com.gustavohenrique.cleanmediasapi.domain.assessment.Assessment;
import br.com.gustavohenrique.cleanmediasapi.domain.assessment.AssessmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AssessmentRepositoryImpl implements AssessmentRepository {

    private final AssessmentJpaRepository jpa;

    @Override
    public Assessment save(Assessment assessment) {
        return jpa.save(AssessmentJpaEntity.fromDomain(assessment)).toDomain();
    }

    @Override
    public List<Assessment> saveAll(List<Assessment> assessments) {
        return jpa.saveAll(assessments.stream().map(AssessmentJpaEntity::fromDomain).toList())
                .stream().map(AssessmentJpaEntity::toDomain).toList();
    }

    @Override
    public Optional<Assessment> findByIdAndProjectionId(Long id, Long projectionId) {
        return jpa.findByIdAndProjectionId(id, projectionId).map(AssessmentJpaEntity::toDomain);
    }

    @Override
    public List<Assessment> findAllByProjectionId(Long projectionId) {
        return jpa.findAllByProjectionId(projectionId).stream()
                .map(AssessmentJpaEntity::toDomain).toList();
    }

    @Override
    public boolean existsByIdentifierAndProjectionId(String identifier, Long projectionId) {
        return jpa.existsByIdentifierAndProjectionId(identifier, projectionId);
    }

    @Override
    public void deleteAllByProjectionId(Long projectionId) {
        jpa.deleteAllByProjectionId(projectionId);
    }
}
