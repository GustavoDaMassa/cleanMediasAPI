package br.com.gustavohenrique.cleanmediasapi.application.projection;

import br.com.gustavohenrique.cleanmediasapi.domain.course.Course;

// Contrato mínimo necessário pelo CourseService.
// Será expandido quando o domínio Projection for implementado.
public interface ProjectionService {

    // Cria a projeção inicial de um curso (chamado ao criar um curso)
    void initializeForCourse(Course course);

    // Deleta todas as projeções de um curso (chamado ao trocar o averageMethod)
    void deleteAllByCourse(Long courseId);
}
