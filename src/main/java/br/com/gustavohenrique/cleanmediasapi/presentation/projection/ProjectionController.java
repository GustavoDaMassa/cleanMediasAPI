package br.com.gustavohenrique.cleanmediasapi.presentation.projection;

import br.com.gustavohenrique.cleanmediasapi.application.course.CourseService;
import br.com.gustavohenrique.cleanmediasapi.application.course.dto.CourseResponse;
import br.com.gustavohenrique.cleanmediasapi.application.projection.ProjectionService;
import br.com.gustavohenrique.cleanmediasapi.application.projection.dto.*;
import br.com.gustavohenrique.cleanmediasapi.domain.course.Course;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectionController {

    private final ProjectionService projectionService;
    private final CourseService courseService;

    @PostMapping("/api/v1/users/{userId}/courses/{courseId}/projections")
    public ResponseEntity<ProjectionResponse> create(@PathVariable Long userId,
                                                     @PathVariable Long courseId,
                                                     @Valid @RequestBody CreateProjectionRequest request) {
        CourseResponse courseResponse = courseService.findByIdAndUserId(userId, courseId);
        Course course = new Course(courseResponse.id(), userId, courseResponse.name(),
                courseResponse.averageMethod(), courseResponse.cutOffGrade());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectionService.create(course, request));
    }

    @GetMapping("/api/v1/users/{userId}/courses/{courseId}/projections")
    public ResponseEntity<List<ProjectionResponse>> listByCourse(@PathVariable Long userId,
                                                                 @PathVariable Long courseId) {
        return ResponseEntity.ok(projectionService.listByCourse(courseId));
    }

    @GetMapping("/api/v1/users/{userId}/projections")
    public ResponseEntity<List<ProjectionResponse>> listAllByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(projectionService.listAllByUser(userId));
    }

    @PatchMapping("/api/v1/users/{userId}/courses/{courseId}/projections/{id}")
    public ResponseEntity<ProjectionResponse> updateName(@PathVariable Long userId,
                                                         @PathVariable Long courseId,
                                                         @PathVariable Long id,
                                                         @Valid @RequestBody UpdateProjectionNameRequest request) {
        return ResponseEntity.ok(projectionService.updateName(courseId, id, request));
    }

    @DeleteMapping("/api/v1/users/{userId}/courses/{courseId}/projections/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long userId,
                                       @PathVariable Long courseId,
                                       @PathVariable Long id) {
        projectionService.delete(courseId, id);
        return ResponseEntity.noContent().build();
    }
}
