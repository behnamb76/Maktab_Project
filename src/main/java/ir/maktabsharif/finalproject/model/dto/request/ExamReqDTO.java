package ir.maktabsharif.finalproject.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamReqDTO {
    @NotBlank(message = "Exam title is required!")
    private String title;

    private String description;

    @NotNull(message = "Duration is required!")
    @Positive(message = "Duration must be positive!")
    private Integer duration;
}
