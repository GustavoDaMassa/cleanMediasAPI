package br.com.gustavohenrique.cleanmediasapi.infrastructure.persistence.assessment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface AssessmentJpaRepository extends JpaRepository<AssessmentJpaEntity, Long> {

    Optional<AssessmentJpaEntity> findByIdAndProjectionId(Long id, Long projectionId);

    List<AssessmentJpaEntity> findAllByProjectionId(Long projectionId);

    boolean existsByIdentifierAndProjectionId(String identifier, Long projectionId);

    void deleteAllByProjectionId(Long projectionId);
}
