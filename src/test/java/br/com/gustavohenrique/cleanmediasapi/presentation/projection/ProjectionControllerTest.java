package br.com.gustavohenrique.cleanmediasapi.presentation.projection;

import br.com.gustavohenrique.cleanmediasapi.application.course.CourseService;
import br.com.gustavohenrique.cleanmediasapi.application.course.dto.CourseResponse;
import br.com.gustavohenrique.cleanmediasapi.application.projection.ProjectionService;
import br.com.gustavohenrique.cleanmediasapi.application.projection.dto.ProjectionResponse;
import br.com.gustavohenrique.cleanmediasapi.domain.course.CourseNotFoundException;
import br.com.gustavohenrique.cleanmediasapi.domain.projection.ProjectionNotFoundException;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ProjectionController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class ProjectionControllerTest {

    @Autowired MockMvc mockMvc;
    @MockitoBean ProjectionService projectionService;
    @MockitoBean CourseService courseService;

    private final CourseResponse courseResponse =
            new CourseResponse(1L, "Cálculo I", "(AV1+AV2)/2", 6.0);
    private final ProjectionResponse projectionResponse =
            new ProjectionResponse(1L, 1L, "Cálculo I", 0.0);

    // --- POST /api/v1/users/{userId}/courses/{courseId}/projections ---

    @Test
    void shouldReturn201WhenProjectionCreated() throws Exception {
        when(courseService.findByIdAndUserId(10L, 1L)).thenReturn(courseResponse);
        when(projectionService.create(any(), any())).thenReturn(projectionResponse);

        mockMvc.perform(post("/api/v1/users/10/courses/1/projections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Projeção Extra"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Cálculo I"));
    }

    @Test
    void shouldReturn404WhenCourseNotFoundOnCreate() throws Exception {
        when(courseService.findByIdAndUserId(10L, 99L))
                .thenThrow(new CourseNotFoundException(99L, 10L));

        mockMvc.perform(post("/api/v1/users/10/courses/99/projections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Projeção Extra"}
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void shouldReturn400WhenProjectionNameAlreadyInUse() throws Exception {
        when(courseService.findByIdAndUserId(10L, 1L)).thenReturn(courseResponse);
        when(projectionService.create(any(), any()))
                .thenThrow(new BusinessException("Projection name already in use"));

        mockMvc.perform(post("/api/v1/users/10/courses/1/projections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Cálculo I"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Projection name already in use"));
    }

    // --- GET /api/v1/users/{userId}/courses/{courseId}/projections ---

    @Test
    void shouldReturn200WithProjectionList() throws Exception {
        when(projectionService.listByCourse(1L)).thenReturn(List.of(projectionResponse));

        mockMvc.perform(get("/api/v1/users/10/courses/1/projections"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    // --- GET /api/v1/users/{userId}/projections ---

    @Test
    void shouldReturn200WithAllUserProjections() throws Exception {
        when(projectionService.listAllByUser(10L)).thenReturn(List.of(projectionResponse));

        mockMvc.perform(get("/api/v1/users/10/projections"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    // --- PATCH /api/v1/users/{userId}/courses/{courseId}/projections/{id} ---

    @Test
    void shouldReturn200WhenProjectionNameUpdated() throws Exception {
        ProjectionResponse updated = new ProjectionResponse(1L, 1L, "Nova", 0.0);
        when(projectionService.updateName(eq(1L), eq(1L), any())).thenReturn(updated);

        mockMvc.perform(patch("/api/v1/users/10/courses/1/projections/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Nova"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nova"));
    }

    @Test
    void shouldReturn404WhenProjectionNotFoundOnUpdate() throws Exception {
        when(projectionService.updateName(eq(1L), eq(99L), any()))
                .thenThrow(new ProjectionNotFoundException(99L, 1L));

        mockMvc.perform(patch("/api/v1/users/10/courses/1/projections/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Nova"}
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    // --- DELETE /api/v1/users/{userId}/courses/{courseId}/projections/{id} ---

    @Test
    void shouldReturn204WhenProjectionDeleted() throws Exception {
        mockMvc.perform(delete("/api/v1/users/10/courses/1/projections/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404WhenProjectionNotFoundOnDelete() throws Exception {
        doThrow(new ProjectionNotFoundException(99L, 1L))
                .when(projectionService).delete(1L, 99L);

        mockMvc.perform(delete("/api/v1/users/10/courses/1/projections/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
