package es.upm.tfm.domain.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Role {

    private String roleName;
    private String roleDescription;
}
