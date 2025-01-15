package es.upm.tfm.adapters.mysqldb.dto;

import lombok.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtRequestDTO {

    @NotBlank(message = "UserName field can not be empty")
    private String userName;
    @NotBlank(message = "UserPassword field can not be empty")
    private String userPassword;
}
