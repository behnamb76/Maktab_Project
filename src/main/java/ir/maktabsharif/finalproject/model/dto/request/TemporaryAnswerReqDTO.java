package ir.maktabsharif.finalproject.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
