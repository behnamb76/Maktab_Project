package ir.maktabsharif.finalproject.model.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
class ExamQuestionRespDTO {
    private Long id;
    private String questionTitle;
    private String questionContent;
    private Double score;
    private String questionType;
}
