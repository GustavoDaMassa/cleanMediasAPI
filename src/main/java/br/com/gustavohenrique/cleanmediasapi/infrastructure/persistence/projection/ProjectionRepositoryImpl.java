package br.com.gustavohenrique.cleanmediasapi.infrastructure.persistence.projection;

import br.com.gustavohenrique.cleanmediasapi.domain.projection.Projection;
import br.com.gustavohenrique.cleanmediasapi.domain.projection.ProjectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProjectionRepositoryImpl implements ProjectionRepository {

    private final ProjectionJpaRepository jpa;

    @Override
    public Projection save(Projection projection) {
        return jpa.save(ProjectionJpaEntity.fromDomain(projection)).toDomain();
    }

    @Override
    public Optional<Projection> findById(Long id) {
        return jpa.findById(id).map(ProjectionJpaEntity::toDomain);
    }

    @Override
    public Optional<Projection> findByIdAndCourseId(Long id, Long courseId) {
        return jpa.findByIdAndCourseId(id, courseId).map(ProjectionJpaEntity::toDomain);
    }

    @Override
    public List<Projection> findAllByCourseId(Long courseId) {
        return jpa.findAllByCourseId(courseId).stream().map(ProjectionJpaEntity::toDomain).toList();
    }

    @Override
    public List<Projection> findAllByUserId(Long userId) {
        return jpa.findAllByUserId(userId).stream().map(ProjectionJpaEntity::toDomain).toList();
    }

    @Override
    public void deleteByIdAndCourseId(Long id, Long courseId) {
        jpa.deleteByIdAndCourseId(id, courseId);
    }

    @Override
    public void deleteAllByCourseId(Long courseId) {
        jpa.deleteAllByCourseId(courseId);
    }

    @Override
    public boolean existsByNameAndCourseId(String name, Long courseId) {
        return jpa.existsByNameAndCourseId(name, courseId);
    }
}
