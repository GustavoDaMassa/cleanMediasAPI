package br.com.gustavohenrique.cleanmediasapi.infrastructure.persistence.course;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface CourseJpaRepository extends JpaRepository<CourseJpaEntity, Long> {

    Optional<CourseJpaEntity> findByIdAndUserId(Long id, Long userId);

    List<CourseJpaEntity> findAllByUserId(Long userId);

    void deleteByIdAndUserId(Long id, Long userId);

    boolean existsByNameAndUserId(String name, Long userId);
}
