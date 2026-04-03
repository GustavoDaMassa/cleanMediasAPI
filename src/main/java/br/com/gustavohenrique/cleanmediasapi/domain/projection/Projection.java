package br.com.gustavohenrique.cleanmediasapi.domain.projection;

// Entidade de domínio pura — sem @Entity, sem Spring, sem Lombok.
public class Projection {

    private final Long id;
    private final Long courseId;
    private final String name;
    private final double finalGrade;

    public Projection(Long id, Long courseId, String name, double finalGrade) {
        this.id = id;
        this.courseId = courseId;
        this.name = name;
        this.finalGrade = finalGrade;
    }

    // Construtor para nova projeção (id ainda não existe)
    public Projection(Long courseId, String name) {
        this(null, courseId, name, 0.0);
    }

    public Long getId() { return id; }
    public Long getCourseId() { return courseId; }
    public String getName() { return name; }
    public double getFinalGrade() { return finalGrade; }
}
