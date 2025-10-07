package ir.maktabsharif.finalproject.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "exam_question")
public class ExamQuestion extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    private Double score;

    @OneToMany(mappedBy = "examQuestion")
    private Set<Answer> answers = new HashSet<>();
}
