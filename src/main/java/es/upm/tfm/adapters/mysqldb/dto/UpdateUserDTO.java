package es.upm.tfm.adapters.mysqldb.dto;

import lombok.*;

import javax.validation.constraints.Email;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserDTO {
    private String userFirstName;
    private String userLastName;
    @Email
    private String email;
    private String userPassword;
}
