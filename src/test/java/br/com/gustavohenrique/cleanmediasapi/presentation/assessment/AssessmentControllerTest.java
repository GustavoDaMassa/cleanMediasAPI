package br.com.gustavohenrique.cleanmediasapi.presentation.assessment;

import br.com.gustavohenrique.cleanmediasapi.application.assessment.AssessmentService;
import br.com.gustavohenrique.cleanmediasapi.application.assessment.dto.AssessmentResponse;
import br.com.gustavohenrique.cleanmediasapi.application.course.CourseService;
import br.com.gustavohenrique.cleanmediasapi.application.course.dto.CourseResponse;
import br.com.gustavohenrique.cleanmediasapi.domain.assessment.AssessmentNotFoundException;
import br.com.gustavohenrique.cleanmediasapi.domain.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = AssessmentController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class AssessmentControllerTest {

    @Autowired MockMvc mockMvc;
    @MockitoBean AssessmentService assessmentService;
    @MockitoBean CourseService courseService;

    private final CourseResponse courseResponse =
            new CourseResponse(1L, "Cálculo I", "(AV1+AV2)/2", 6.0);

    private final AssessmentResponse assessmentResponse =
            new AssessmentResponse(1L, 5L, "AV1", 10.0, 0.0, 0.0, false);

    private final AssessmentResponse gradedResponse =
            new AssessmentResponse(1L, 5L, "AV1", 10.0, 8.0, 0.0, true);

    // --- GET /assessments ---

    @Test
    void shouldReturn200WithAssessmentList() throws Exception {
        when(assessmentService.findAllByProjectionId(5L)).thenReturn(List.of(assessmentResponse));

        mockMvc.perform(get("/api/v1/users/10/courses/1/projections/5/assessments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].identifier").value("AV1"));
    }

    // --- GET /assessments/{id} ---

    @Test
    void shouldReturn200WithSingleAssessment() throws Exception {
        when(assessmentService.findByIdAndProjectionId(1L, 5L)).thenReturn(assessmentResponse);

        mockMvc.perform(get("/api/v1/users/10/courses/1/projections/5/assessments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.identifier").value("AV1"));
    }

    @Test
    void shouldReturn404WhenAssessmentNotFound() throws Exception {
        when(assessmentService.findByIdAndProjectionId(99L, 5L))
                .thenThrow(new AssessmentNotFoundException(99L, 5L));

        mockMvc.perform(get("/api/v1/users/10/courses/1/projections/5/assessments/99"))
                .andExpect(status().isNotFound());
    }

    // --- PATCH /assessments/{id}/grade ---

    @Test
    void shouldReturn200WhenGradeApplied() throws Exception {
        when(courseService.findByIdAndUserId(10L, 1L)).thenReturn(courseResponse);
        when(assessmentService.applyGrade(eq(5L), eq(1L), eq(8.0), any()))
                .thenReturn(gradedResponse);

        mockMvc.perform(patch("/api/v1/users/10/courses/1/projections/5/assessments/1/grade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"grade":8.0}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grade").value(8.0))
                .andExpect(jsonPath("$.fixed").value(true));
    }

    @Test
    void shouldReturn400WhenGradeExceedsMax() throws Exception {
        when(courseService.findByIdAndUserId(10L, 1L)).thenReturn(courseResponse);
        when(assessmentService.applyGrade(eq(5L), eq(1L), eq(15.0), any()))
                .thenThrow(new BusinessException("Grade 15.0 exceeds max value 10.0 for AV1"));

        mockMvc.perform(patch("/api/v1/users/10/courses/1/projections/5/assessments/1/grade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"grade":15.0}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenGradeBodyInvalid() throws Exception {
        mockMvc.perform(patch("/api/v1/users/10/courses/1/projections/5/assessments/1/grade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
