package br.com.gustavohenrique.cleanmediasapi.domain.course;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {

    Course save(Course course);

    Optional<Course> findByIdAndUserId(Long id, Long userId);

    List<Course> findAllByUserId(Long userId);

    void deleteByIdAndUserId(Long id, Long userId);

    boolean existsByNameAndUserId(String name, Long userId);
}
