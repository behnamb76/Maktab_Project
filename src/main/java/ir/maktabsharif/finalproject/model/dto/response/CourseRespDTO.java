package ir.maktabsharif.finalproject.model.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseRespDTO {
    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private int studentCount;
}
