package br.com.gustavohenrique.cleanmediasapi.domain.projection;

import java.util.List;
import java.util.Optional;

public interface ProjectionRepository {

    Projection save(Projection projection);

    Optional<Projection> findById(Long id);

    Optional<Projection> findByIdAndCourseId(Long id, Long courseId);

    List<Projection> findAllByCourseId(Long courseId);

    List<Projection> findAllByUserId(Long userId);

    void deleteByIdAndCourseId(Long id, Long courseId);

    void deleteAllByCourseId(Long courseId);

    boolean existsByNameAndCourseId(String name, Long courseId);
}
