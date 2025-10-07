package ir.maktabsharif.finalproject.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Answer extends BaseModel {
    private String answer;
    private Double score;

    @ManyToOne
    @JoinColumn(name = "exam_question_id")
    private ExamQuestion examQuestion;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;
}
