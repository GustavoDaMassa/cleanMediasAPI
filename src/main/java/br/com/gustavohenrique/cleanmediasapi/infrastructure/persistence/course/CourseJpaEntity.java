package br.com.gustavohenrique.cleanmediasapi.infrastructure.persistence.course;

import br.com.gustavohenrique.cleanmediasapi.domain.course.Course;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "courses")
@Getter
@NoArgsConstructor
public class CourseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(name = "average_method", nullable = false, length = 512)
    private String averageMethod;

    @Column(name = "cut_off_grade", nullable = false)
    private double cutOffGrade;

    public static CourseJpaEntity fromDomain(Course course) {
        CourseJpaEntity e = new CourseJpaEntity();
        e.id = course.getId();
        e.userId = course.getUserId();
        e.name = course.getName();
        e.averageMethod = course.getAverageMethod();
        e.cutOffGrade = course.getCutOffGrade();
        return e;
    }

    public Course toDomain() {
        return new Course(id, userId, name, averageMethod, cutOffGrade);
    }
}
