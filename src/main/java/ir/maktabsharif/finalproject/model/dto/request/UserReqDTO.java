package ir.maktabsharif.finalproject.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserReqDTO {
    @NotBlank(message = "First name is required!")
    @Size(max = 50, message = "firstname must be {max} characters maximum!")
    private String firstName;

    @NotBlank(message = "Last name is required!")
    @Size(max = 50, message = "lastname must be {max} characters maximum!")
    private String lastName;

    @NotBlank(message = "Username is required!")
    @Size(min = 3, max = 10, message = "username must be between {min} to {max} characters!")
    private String username;

    @NotBlank(message = "Password is required!")
    @Size(min = 8, max = 20, message = "password must be between {min} and {max} characters!")
    private String password;

    private LocalDate birthday;

    @NotBlank(message = "Role is required!")
    private String role;
}
