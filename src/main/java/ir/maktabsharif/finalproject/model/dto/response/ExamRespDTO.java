package ir.maktabsharif.finalproject.model.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamRespDTO {
    private Long id;
    private String title;
    private String description;
    private Integer duration;
    private Long courseId;
    private String courseTitle;
    private Double totalScore;
    private int questionCount;
    private LocalDateTime createdAt;
    private List<ExamQuestionRespDTO> questions;
}
