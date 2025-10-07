package ir.maktabsharif.finalproject.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptionReqDTO {
    @NotBlank(message = "Option text is required!")
    private String text;

    private boolean correct;
}
