package br.com.gustavohenrique.cleanmediasapi.presentation.course;

import br.com.gustavohenrique.cleanmediasapi.application.course.CourseService;
import br.com.gustavohenrique.cleanmediasapi.application.course.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseResponse> create(@PathVariable Long userId,
                                                 @Valid @RequestBody CreateCourseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.create(userId, request));
    }

    @GetMapping
    public ResponseEntity<List<CourseResponse>> listAll(@PathVariable Long userId) {
        return ResponseEntity.ok(courseService.listAll(userId));
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<CourseResponse> updateName(@PathVariable Long userId,
                                                     @PathVariable Long id,
                                                     @Valid @RequestBody UpdateCourseNameRequest request) {
        return ResponseEntity.ok(courseService.updateName(userId, id, request));
    }

    @PatchMapping("/{id}/method")
    public ResponseEntity<CourseResponse> updateAverageMethod(@PathVariable Long userId,
                                                              @PathVariable Long id,
                                                              @Valid @RequestBody UpdateAverageMethodRequest request) {
        return ResponseEntity.ok(courseService.updateAverageMethod(userId, id, request));
    }

    @PatchMapping("/{id}/cutoffgrade")
    public ResponseEntity<CourseResponse> updateCutOffGrade(@PathVariable Long userId,
                                                            @PathVariable Long id,
                                                            @Valid @RequestBody UpdateCutOffGradeRequest request) {
        return ResponseEntity.ok(courseService.updateCutOffGrade(userId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long userId, @PathVariable Long id) {
        courseService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }
}
