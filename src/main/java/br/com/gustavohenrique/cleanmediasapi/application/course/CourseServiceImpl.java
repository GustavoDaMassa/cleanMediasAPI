package br.com.gustavohenrique.cleanmediasapi.application.course;

import br.com.gustavohenrique.cleanmediasapi.application.course.dto.*;
import br.com.gustavohenrique.cleanmediasapi.application.projection.ProjectionService;
import br.com.gustavohenrique.cleanmediasapi.domain.course.Course;
import br.com.gustavohenrique.cleanmediasapi.domain.course.CourseNotFoundException;
import br.com.gustavohenrique.cleanmediasapi.domain.course.CourseRepository;
import br.com.gustavohenrique.cleanmediasapi.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final ProjectionService projectionService;

    @Override
    public CourseResponse create(Long userId, CreateCourseRequest request) {
        requireNameAvailable(request.name(), userId);
        Course saved = courseRepository.save(
                new Course(userId, request.name(), request.averageMethod(), request.cutOffGrade())
        );
        projectionService.initializeForCourse(saved);
        return CourseResponse.from(saved);
    }

    @Override
    public List<CourseResponse> listAll(Long userId) {
        return courseRepository.findAllByUserId(userId).stream()
                .map(CourseResponse::from)
                .toList();
    }

    @Override
    public CourseResponse updateName(Long userId, Long id, UpdateCourseNameRequest request) {
        Course existing = requireExists(id, userId);
        requireNameAvailable(request.name(), userId);
        return CourseResponse.from(courseRepository.save(
                new Course(existing.getId(), existing.getUserId(), request.name(),
                        existing.getAverageMethod(), existing.getCutOffGrade())
        ));
    }

    @Override
    public CourseResponse updateAverageMethod(Long userId, Long id, UpdateAverageMethodRequest request) {
        Course existing = requireExists(id, userId);
        Course updated = courseRepository.save(
                new Course(existing.getId(), existing.getUserId(), existing.getName(),
                        request.averageMethod(), existing.getCutOffGrade())
        );
        projectionService.deleteAllByCourse(id);
        projectionService.initializeForCourse(updated);
        return CourseResponse.from(updated);
    }

    @Override
    public CourseResponse updateCutOffGrade(Long userId, Long id, UpdateCutOffGradeRequest request) {
        Course existing = requireExists(id, userId);
        return CourseResponse.from(courseRepository.save(
                new Course(existing.getId(), existing.getUserId(), existing.getName(),
                        existing.getAverageMethod(), request.cutOffGrade())
        ));
    }

    @Override
    public void delete(Long userId, Long id) {
        requireExists(id, userId);
        courseRepository.deleteByIdAndUserId(id, userId);
    }

    // --- helpers privados ---

    private Course requireExists(Long id, Long userId) {
        return courseRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new CourseNotFoundException(id, userId));
    }

    private void requireNameAvailable(String name, Long userId) {
        if (courseRepository.existsByNameAndUserId(name, userId)) {
            throw new BusinessException("Course name already in use");
        }
    }
}
