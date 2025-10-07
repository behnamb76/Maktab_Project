package ir.maktabsharif.finalproject.model;

import ir.maktabsharif.finalproject.model.enums.ExamStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamAttempt extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "student_id")

    private User student;
    @Enumerated(EnumType.STRING)
    private ExamStatus examStatus;
    private LocalDateTime attemptAt;
    private LocalDateTime endAt;
}
