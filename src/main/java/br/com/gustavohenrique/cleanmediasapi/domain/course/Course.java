package br.com.gustavohenrique.cleanmediasapi.domain.course;

// Entidade de domínio pura — sem @Entity, sem Spring, sem Lombok.
public class Course {

    private final Long id;
    private final Long userId;
    private final String name;
    private final String averageMethod;
    private final double cutOffGrade;

    public Course(Long id, Long userId, String name, String averageMethod, double cutOffGrade) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.averageMethod = averageMethod;
        this.cutOffGrade = cutOffGrade;
    }

    // Construtor para novo curso (id ainda não existe)
    public Course(Long userId, String name, String averageMethod, double cutOffGrade) {
        this(null, userId, name, averageMethod, cutOffGrade);
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getName() { return name; }
    public String getAverageMethod() { return averageMethod; }
    public double getCutOffGrade() { return cutOffGrade; }
}
