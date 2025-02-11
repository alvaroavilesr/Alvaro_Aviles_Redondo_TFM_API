package es.upm.tfm.adapters.mysqldb.response;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JwtResponse {
    private UserResponse userResponse;
    private String jwtToken;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JwtResponse that = (JwtResponse) o;
        return Objects.equals(userResponse, that.userResponse) &&
                Objects.equals(jwtToken, that.jwtToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userResponse, jwtToken);
    }
}
