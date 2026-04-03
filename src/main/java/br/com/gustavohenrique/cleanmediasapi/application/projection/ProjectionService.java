package br.com.gustavohenrique.cleanmediasapi.application.projection;

import br.com.gustavohenrique.cleanmediasapi.application.projection.dto.*;
import br.com.gustavohenrique.cleanmediasapi.domain.course.Course;

import java.util.List;

public interface ProjectionService {

    // Contrato mínimo usado pelo CourseService
    void initializeForCourse(Course course);
    void deleteAllByCourse(Long courseId);

    // CRUD completo
    ProjectionResponse create(Course course, CreateProjectionRequest request);
    List<ProjectionResponse> listByCourse(Long courseId);
    List<ProjectionResponse> listAllByUser(Long userId);
    ProjectionResponse updateName(Long courseId, Long id, UpdateProjectionNameRequest request);
    void delete(Long courseId, Long id);
}
