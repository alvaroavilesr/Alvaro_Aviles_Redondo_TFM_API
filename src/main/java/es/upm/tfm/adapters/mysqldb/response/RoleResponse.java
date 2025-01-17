package es.upm.tfm.adapters.mysqldb.response;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RoleResponse {
    private String roleName;
    private String roleDescription;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleResponse that = (RoleResponse) o;
        return Objects.equals(roleName, that.roleName) &&
                Objects.equals(roleDescription, that.roleDescription);
    }
}
