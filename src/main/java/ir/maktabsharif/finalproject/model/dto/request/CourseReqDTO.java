package ir.maktabsharif.finalproject.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseReqDTO {
    @Size(max = 100, message = "course title must be {max} characters maximum!")
    private String title;

    @NotNull(message = "Start date is required!")
    private LocalDate startDate;

    @NotNull(message = "Start date is required!")
    private LocalDate endDate;
}
