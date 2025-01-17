package es.upm.tfm.adapters.mysqldb.dto;

import lombok.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RoleDTO {
    @NotBlank(message = "Role name field can not be empty")
    private String roleName;
    @NotBlank(message = "Role description field can not be empty")
    private String roleDescription;
}
