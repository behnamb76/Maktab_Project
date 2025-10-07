package ir.maktabsharif.finalproject.model.dto;

import ir.maktabsharif.finalproject.model.dto.response.UserRespDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseMemberDTO {
    private Long id;
    private String role;
    private UserRespDTO user;
}
