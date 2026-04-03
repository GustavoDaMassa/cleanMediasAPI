package br.com.gustavohenrique.cleanmediasapi.presentation.course;

import br.com.gustavohenrique.cleanmediasapi.application.course.CourseService;
import br.com.gustavohenrique.cleanmediasapi.application.course.dto.CourseResponse;
import br.com.gustavohenrique.cleanmediasapi.domain.course.CourseNotFoundException;
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

@WebMvcTest(value = CourseController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class CourseControllerTest {

    @Autowired MockMvc mockMvc;
    @MockitoBean CourseService courseService;

    private final CourseResponse courseResponse =
            new CourseResponse(1L, "Cálculo I", "(AV1+AV2)/2", 6.0);

    // --- POST /api/v1/users/{userId}/courses ---

    @Test
    void shouldReturn201WhenCourseCreated() throws Exception {
        when(courseService.create(eq(10L), any())).thenReturn(courseResponse);

        mockMvc.perform(post("/api/v1/users/10/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Cálculo I","averageMethod":"(AV1+AV2)/2","cutOffGrade":6.0}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Cálculo I"))
                .andExpect(jsonPath("$.cutOffGrade").value(6.0));
    }

    @Test
    void shouldReturn400WhenBodyInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/users/10/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"","averageMethod":"","cutOffGrade":-1}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void shouldReturn400WhenNameAlreadyInUse() throws Exception {
        when(courseService.create(eq(10L), any()))
                .thenThrow(new BusinessException("Course name already in use"));

        mockMvc.perform(post("/api/v1/users/10/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Cálculo I","averageMethod":"(AV1+AV2)/2","cutOffGrade":6.0}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Course name already in use"));
    }

    // --- GET /api/v1/users/{userId}/courses ---

    @Test
    void shouldReturn200WithCourseList() throws Exception {
        when(courseService.listAll(10L)).thenReturn(List.of(courseResponse));

        mockMvc.perform(get("/api/v1/users/10/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Cálculo I"));
    }

    // --- PATCH /api/v1/users/{userId}/courses/{id}/name ---

    @Test
    void shouldReturn200WhenCourseNameUpdated() throws Exception {
        CourseResponse updated = new CourseResponse(1L, "Cálculo II", "(AV1+AV2)/2", 6.0);
        when(courseService.updateName(eq(10L), eq(1L), any())).thenReturn(updated);

        mockMvc.perform(patch("/api/v1/users/10/courses/1/name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Cálculo II"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cálculo II"));
    }

    @Test
    void shouldReturn404WhenCourseNotFoundOnUpdateName() throws Exception {
        when(courseService.updateName(eq(10L), eq(99L), any()))
                .thenThrow(new CourseNotFoundException(99L, 10L));

        mockMvc.perform(patch("/api/v1/users/10/courses/99/name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Novo"}
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    // --- PATCH /api/v1/users/{userId}/courses/{id}/method ---

    @Test
    void shouldReturn200WhenAverageMethodUpdated() throws Exception {
        CourseResponse updated = new CourseResponse(1L, "Cálculo I", "(AV1+AV2+AV3)/3", 6.0);
        when(courseService.updateAverageMethod(eq(10L), eq(1L), any())).thenReturn(updated);

        mockMvc.perform(patch("/api/v1/users/10/courses/1/method")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"averageMethod":"(AV1+AV2+AV3)/3"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageMethod").value("(AV1+AV2+AV3)/3"));
    }

    // --- PATCH /api/v1/users/{userId}/courses/{id}/cutoffgrade ---

    @Test
    void shouldReturn200WhenCutOffGradeUpdated() throws Exception {
        CourseResponse updated = new CourseResponse(1L, "Cálculo I", "(AV1+AV2)/2", 7.0);
        when(courseService.updateCutOffGrade(eq(10L), eq(1L), any())).thenReturn(updated);

        mockMvc.perform(patch("/api/v1/users/10/courses/1/cutoffgrade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"cutOffGrade":7.0}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cutOffGrade").value(7.0));
    }

    // --- DELETE /api/v1/users/{userId}/courses/{id} ---

    @Test
    void shouldReturn204WhenCourseDeleted() throws Exception {
        mockMvc.perform(delete("/api/v1/users/10/courses/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404WhenCourseNotFoundOnDelete() throws Exception {
        doThrow(new CourseNotFoundException(99L, 10L)).when(courseService).delete(10L, 99L);

        mockMvc.perform(delete("/api/v1/users/10/courses/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
