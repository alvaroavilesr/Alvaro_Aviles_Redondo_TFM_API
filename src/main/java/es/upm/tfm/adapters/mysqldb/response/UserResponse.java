package es.upm.tfm.adapters.mysqldb.response;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserResponse {
    private String userName;
    private String userFirstName;
    private String userLastName;
    private String email;
    private Set<RoleResponse> role;
}
