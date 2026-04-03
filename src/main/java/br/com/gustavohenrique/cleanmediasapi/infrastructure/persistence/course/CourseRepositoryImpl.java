package br.com.gustavohenrique.cleanmediasapi.infrastructure.persistence.course;

import br.com.gustavohenrique.cleanmediasapi.domain.course.Course;
import br.com.gustavohenrique.cleanmediasapi.domain.course.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CourseRepositoryImpl implements CourseRepository {

    private final CourseJpaRepository jpa;

    @Override
    public Course save(Course course) {
        return jpa.save(CourseJpaEntity.fromDomain(course)).toDomain();
    }

    @Override
    public Optional<Course> findByIdAndUserId(Long id, Long userId) {
        return jpa.findByIdAndUserId(id, userId).map(CourseJpaEntity::toDomain);
    }

    @Override
    public List<Course> findAllByUserId(Long userId) {
        return jpa.findAllByUserId(userId).stream().map(CourseJpaEntity::toDomain).toList();
    }

    @Override
    public void deleteByIdAndUserId(Long id, Long userId) {
        jpa.deleteByIdAndUserId(id, userId);
    }

    @Override
    public boolean existsByNameAndUserId(String name, Long userId) {
        return jpa.existsByNameAndUserId(name, userId);
    }
}
