package ir.maktabsharif.finalproject.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
