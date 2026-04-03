package br.com.gustavohenrique.cleanmediasapi.domain.assessment;

import br.com.gustavohenrique.cleanmediasapi.domain.exception.BusinessException;

// Entidade de domínio pura — sem @Entity, sem Spring, sem Lombok.
//
// Invariante central: o campo `grade` não tem setter público.
// O único caminho para atribuir uma nota é via applyGrade(), que valida
// o valor e marca fixed=true. Isso garante que a regra de negócio
// nunca seja burlada — é encapsulamento real, não apenas convenção.
public class Assessment {

    private final Long id;
    private final Long projectionId;
    private final String identifier;   // ex: "AV1", "AT1" — imutável após criação
    private final double maxValue;     // imutável após criação
    private double grade;
    private double requiredGrade;
    private boolean fixed;

    // Construtor para reconstituição do banco (todos os campos)
    public Assessment(Long id, Long projectionId, String identifier,
                      double grade, double maxValue, double requiredGrade, boolean fixed) {
        this.id = id;
        this.projectionId = projectionId;
        this.identifier = identifier;
        this.grade = grade;
        this.maxValue = maxValue;
        this.requiredGrade = requiredGrade;
        this.fixed = fixed;
    }

    // Construtor para novo assessment (criado ao parsear a fórmula)
    public Assessment(Long projectionId, String identifier, double maxValue) {
        this(null, projectionId, identifier, 0.0, maxValue, 0.0, false);
    }

    // Invariante: grade só pode ser atribuída aqui.
    // Valida range e marca o assessment como fixo (não pode ser alterado depois).
    public void applyGrade(double grade) {
        if (grade < 0) {
            throw new BusinessException("Grade cannot be negative");
        }
        if (grade > maxValue) {
            throw new BusinessException(
                    "Grade " + grade + " exceeds max value " + maxValue + " for " + identifier);
        }
        this.grade = grade;
        this.fixed = true;
    }

    public void updateRequiredGrade(double requiredGrade) {
        this.requiredGrade = requiredGrade;
    }

    public Long getId() { return id; }
    public Long getProjectionId() { return projectionId; }
    public String getIdentifier() { return identifier; }
    public double getMaxValue() { return maxValue; }
    public double getGrade() { return grade; }
    public double getRequiredGrade() { return requiredGrade; }
    public boolean isFixed() { return fixed; }
}
