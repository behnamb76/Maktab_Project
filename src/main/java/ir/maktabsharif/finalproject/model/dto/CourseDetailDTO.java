package ir.maktabsharif.finalproject.model.dto;

import ir.maktabsharif.finalproject.model.dto.response.ExamRespDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseDetailDTO {
    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<CourseMemberDTO> members;
    private List<ExamRespDTO> exams;
    private int questionBankSize;
}
