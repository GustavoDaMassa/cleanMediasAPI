package br.com.gustavohenrique.cleanmediasapi.infrastructure.persistence.projection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

interface ProjectionJpaRepository extends JpaRepository<ProjectionJpaEntity, Long> {

    Optional<ProjectionJpaEntity> findByIdAndCourseId(Long id, Long courseId);

    List<ProjectionJpaEntity> findAllByCourseId(Long courseId);

    @Query("SELECT p FROM ProjectionJpaEntity p WHERE p.courseId IN " +
           "(SELECT c.id FROM CourseJpaEntity c WHERE c.userId = :userId)")
    List<ProjectionJpaEntity> findAllByUserId(Long userId);

    void deleteByIdAndCourseId(Long id, Long courseId);

    void deleteAllByCourseId(Long courseId);

    boolean existsByNameAndCourseId(String name, Long courseId);
}
