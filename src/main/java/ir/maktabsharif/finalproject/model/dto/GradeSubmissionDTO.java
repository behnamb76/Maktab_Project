package ir.maktabsharif.finalproject.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GradeSubmissionDTO {
    @NotNull
    private Long examId;

    @NotNull
    private Long studentId;

    private Map<Long, Double> grades;
}
