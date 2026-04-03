package br.com.gustavohenrique.cleanmediasapi.infrastructure.persistence.projection;

import br.com.gustavohenrique.cleanmediasapi.domain.projection.Projection;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "projections")
@Getter
@NoArgsConstructor
public class ProjectionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(nullable = false)
    private String name;

    @Column(name = "final_grade", nullable = false)
    private double finalGrade;

    public static ProjectionJpaEntity fromDomain(Projection projection) {
        ProjectionJpaEntity e = new ProjectionJpaEntity();
        e.id = projection.getId();
        e.courseId = projection.getCourseId();
        e.name = projection.getName();
        e.finalGrade = projection.getFinalGrade();
        return e;
    }

    public Projection toDomain() {
        return new Projection(id, courseId, name, finalGrade);
    }
}
