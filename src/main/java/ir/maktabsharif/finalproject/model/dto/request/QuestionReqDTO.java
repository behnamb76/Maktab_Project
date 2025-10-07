package ir.maktabsharif.finalproject.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
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
