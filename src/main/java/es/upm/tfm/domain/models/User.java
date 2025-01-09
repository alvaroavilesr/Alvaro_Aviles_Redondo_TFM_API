package es.upm.tfm.domain.models;

import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class User {

    private String userName;
    private String userFirstName;
    private String userLastName;
    private String email;
    private String userPassword;
    private Set<Role> role;
    private List<Order> orders;
}
