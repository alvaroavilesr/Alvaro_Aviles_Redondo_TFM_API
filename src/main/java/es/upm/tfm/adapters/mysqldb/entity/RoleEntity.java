package es.upm.tfm.adapters.mysqldb.entity;

import lombok.*;
import jakarta.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    @Column(name = "roleName")
    private String roleName;
    @Column(name = "roleDescription")
    private String roleDescription;
}