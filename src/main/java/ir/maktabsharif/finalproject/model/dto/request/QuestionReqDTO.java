package ir.maktabsharif.finalproject.model.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionReqDTO {
    @Max(value = 20, message = "Title must be 20 characters maximum!")
    private String title;

    @NotBlank(message = "Question content is required!")
    private String content;

    @Positive(message = "Default score must be a positive number!")
    private Double defaultScore;

    @NotBlank(message = "Question type is required!")
    private String type;

    private List<OptionReqDTO> options;
}
