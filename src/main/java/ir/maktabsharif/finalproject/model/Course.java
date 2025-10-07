package ir.maktabsharif.finalproject.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course extends BaseModel {
    private String title;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
    private LocalDate startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private LocalDate endDate;

    @OneToMany(mappedBy = "course")
    private Set<CourseMember> courseMembers;

    @OneToMany(mappedBy = "course")
    private Set<Exam> exams;

    @OneToOne
    private QuestionBank questionBank;
}
