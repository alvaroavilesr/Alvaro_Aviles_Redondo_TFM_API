package es.upm.tfm.adapters.mysqldb.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RoleResponse {
    private String roleName;
    private String roleDescription;
}
