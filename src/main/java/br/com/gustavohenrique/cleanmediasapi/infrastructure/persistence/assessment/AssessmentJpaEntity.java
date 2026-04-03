package br.com.gustavohenrique.cleanmediasapi.infrastructure.persistence.assessment;

import br.com.gustavohenrique.cleanmediasapi.domain.assessment.Assessment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "assessments")
@Getter
@NoArgsConstructor
public class AssessmentJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "projection_id", nullable = false)
    private Long projectionId;

    @Column(nullable = false)
    private String identifier;

    @Column(name = "max_value", nullable = false)
    private double maxValue;

    @Column(nullable = false)
    private double grade;

    @Column(name = "required_grade", nullable = false)
    private double requiredGrade;

    @Column(nullable = false)
    private boolean fixed;

    public static AssessmentJpaEntity fromDomain(Assessment a) {
        AssessmentJpaEntity e = new AssessmentJpaEntity();
        e.id = a.getId();
        e.projectionId = a.getProjectionId();
        e.identifier = a.getIdentifier();
        e.maxValue = a.getMaxValue();
        e.grade = a.getGrade();
        e.requiredGrade = a.getRequiredGrade();
        e.fixed = a.isFixed();
        return e;
    }

    public Assessment toDomain() {
        return new Assessment(id, projectionId, identifier, grade, maxValue, requiredGrade, fixed);
    }
}
