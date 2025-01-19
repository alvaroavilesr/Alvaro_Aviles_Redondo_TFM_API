package es.upm.tfm.adapters.mysqldb.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class NewUserDTO {
    @NotBlank(message = "UserName field can not be empty")
    private String userName;
    @NotBlank(message = "UserFirstName field can not be empty")
    private String userFirstName;
    @NotBlank(message = "UserLastName field can not be empty")
    private String userLastName;
    @NotBlank(message = "Email field can not be empty")
    private String email;
    @NotBlank(message = "UserPassword field can not be empty")
    private String userPassword;
}
