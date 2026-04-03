package br.com.gustavohenrique.cleanmediasapi.application.course;

import br.com.gustavohenrique.cleanmediasapi.application.course.dto.*;

import java.util.List;

public interface CourseService {

    CourseResponse create(Long userId, CreateCourseRequest request);

    List<CourseResponse> listAll(Long userId);

    CourseResponse updateName(Long userId, Long id, UpdateCourseNameRequest request);

    CourseResponse updateAverageMethod(Long userId, Long id, UpdateAverageMethodRequest request);

    CourseResponse updateCutOffGrade(Long userId, Long id, UpdateCutOffGradeRequest request);

    void delete(Long userId, Long id);

    // Usado pelo ProjectionController para buscar o curso antes de criar uma projeção
    CourseResponse findByIdAndUserId(Long userId, Long id);
}
