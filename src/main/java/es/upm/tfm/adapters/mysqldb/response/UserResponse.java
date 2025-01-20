package es.upm.tfm.adapters.mysqldb.response;

import lombok.*;

import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponse that = (UserResponse) o;
        return Objects.equals(userName, that.userName) &&
                Objects.equals(userFirstName, that.userFirstName) &&
                Objects.equals(userLastName, that.userLastName) &&
                Objects.equals(email, that.email) &&
                Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, userFirstName, userLastName, email, role);
    }
}
