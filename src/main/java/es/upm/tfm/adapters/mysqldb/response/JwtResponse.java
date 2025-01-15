package es.upm.tfm.adapters.mysqldb.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {
    private UserResponse userResponse;
    private String jwtToken;

}
