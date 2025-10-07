package ir.maktabsharif.finalproject.model.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TemporaryAnswerReqDTO {
    @NotNull
    private Long examQuestionId;

    @NotBlank
    private String answer;
}
