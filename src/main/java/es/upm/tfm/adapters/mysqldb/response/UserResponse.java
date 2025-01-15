package es.upm.tfm.adapters.mysqldb.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String userName;
    private String userFirstName;
    private String userLastName;
    private String email;
    //private Set<Role> role; --> TODO: Ver como se haria esto si una respuesta de rol o como
}
