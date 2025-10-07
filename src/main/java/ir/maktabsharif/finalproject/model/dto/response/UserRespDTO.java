package ir.maktabsharif.finalproject.model.dto.response;

import ir.maktabsharif.finalproject.model.enums.Status;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRespDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private LocalDate birthday;
    private String role;
    private Status status;
    private int courseCount;
}
