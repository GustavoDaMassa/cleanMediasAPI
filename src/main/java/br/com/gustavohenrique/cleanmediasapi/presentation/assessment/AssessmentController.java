package br.com.gustavohenrique.cleanmediasapi.presentation.assessment;

import br.com.gustavohenrique.cleanmediasapi.application.assessment.AssessmentService;
import br.com.gustavohenrique.cleanmediasapi.application.assessment.dto.AssessmentResponse;
import br.com.gustavohenrique.cleanmediasapi.application.course.CourseService;
import br.com.gustavohenrique.cleanmediasapi.application.course.dto.CourseResponse;
import br.com.gustavohenrique.cleanmediasapi.presentation.assessment.dto.ApplyGradeRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;
    private final CourseService courseService;

    @GetMapping("/api/v1/users/{userId}/courses/{courseId}/projections/{projectionId}/assessments")
    public ResponseEntity<List<AssessmentResponse>> listByProjection(
            @PathVariable Long userId,
            @PathVariable Long courseId,
            @PathVariable Long projectionId) {
        return ResponseEntity.ok(assessmentService.findAllByProjectionId(projectionId));
    }

    @GetMapping("/api/v1/users/{userId}/courses/{courseId}/projections/{projectionId}/assessments/{id}")
    public ResponseEntity<AssessmentResponse> findById(
            @PathVariable Long userId,
            @PathVariable Long courseId,
            @PathVariable Long projectionId,
            @PathVariable Long id) {
        return ResponseEntity.ok(assessmentService.findByIdAndProjectionId(id, projectionId));
    }

    @PatchMapping("/api/v1/users/{userId}/courses/{courseId}/projections/{projectionId}/assessments/{id}/grade")
    public ResponseEntity<AssessmentResponse> applyGrade(
            @PathVariable Long userId,
            @PathVariable Long courseId,
            @PathVariable Long projectionId,
            @PathVariable Long id,
            @Valid @RequestBody ApplyGradeRequest request) {
        CourseResponse course = courseService.findByIdAndUserId(userId, courseId);
        return ResponseEntity.ok(
                assessmentService.applyGrade(projectionId, id, request.grade(), course.averageMethod())
        );
    }
}
