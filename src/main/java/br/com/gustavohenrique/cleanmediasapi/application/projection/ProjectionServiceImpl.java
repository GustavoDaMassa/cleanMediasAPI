package br.com.gustavohenrique.cleanmediasapi.application.projection;

import br.com.gustavohenrique.cleanmediasapi.application.assessment.AssessmentService;
import br.com.gustavohenrique.cleanmediasapi.application.projection.dto.*;
import br.com.gustavohenrique.cleanmediasapi.domain.course.Course;
import br.com.gustavohenrique.cleanmediasapi.domain.exception.BusinessException;
import br.com.gustavohenrique.cleanmediasapi.domain.projection.Projection;
import br.com.gustavohenrique.cleanmediasapi.domain.projection.ProjectionNotFoundException;
import br.com.gustavohenrique.cleanmediasapi.domain.projection.ProjectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectionServiceImpl implements ProjectionService {

    private final ProjectionRepository projectionRepository;
    private final AssessmentService assessmentService;

    @Override
    public void initializeForCourse(Course course) {
        Projection saved = projectionRepository.save(new Projection(course.getId(), course.getName()));
        assessmentService.initializeForProjection(saved, course.getAverageMethod());
    }

    @Override
    public void deleteAllByCourse(Long courseId) {
        projectionRepository.deleteAllByCourseId(courseId);
    }

    @Override
    public ProjectionResponse create(Course course, CreateProjectionRequest request) {
        requireNameAvailable(request.name(), course.getId());
        Projection saved = projectionRepository.save(new Projection(course.getId(), request.name()));
        assessmentService.initializeForProjection(saved, course.getAverageMethod());
        return ProjectionResponse.from(saved);
    }

    @Override
    public List<ProjectionResponse> listByCourse(Long courseId) {
        return projectionRepository.findAllByCourseId(courseId).stream()
                .map(ProjectionResponse::from)
                .toList();
    }

    @Override
    public List<ProjectionResponse> listAllByUser(Long userId) {
        return projectionRepository.findAllByUserId(userId).stream()
                .map(ProjectionResponse::from)
                .toList();
    }

    @Override
    public ProjectionResponse updateName(Long courseId, Long id, UpdateProjectionNameRequest request) {
        Projection existing = requireExists(id, courseId);
        requireNameAvailable(request.name(), courseId);
        return ProjectionResponse.from(projectionRepository.save(
                new Projection(existing.getId(), existing.getCourseId(),
                        request.name(), existing.getFinalGrade())
        ));
    }

    @Override
    public void delete(Long courseId, Long id) {
        requireExists(id, courseId);
        projectionRepository.deleteByIdAndCourseId(id, courseId);
    }

    // --- helpers privados ---

    private Projection requireExists(Long id, Long courseId) {
        return projectionRepository.findByIdAndCourseId(id, courseId)
                .orElseThrow(() -> new ProjectionNotFoundException(id, courseId));
    }

    private void requireNameAvailable(String name, Long courseId) {
        if (projectionRepository.existsByNameAndCourseId(name, courseId)) {
            throw new BusinessException("Projection name already in use");
        }
    }
}
