package es.upm.tfm.adapters.mysqldb.entity;

import lombok.*;
import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "roles")
public class RoleEntity {

    @Id
    @Column(name = "roleName")
    private String roleName;
    @Column(name = "roleDescription")
    private String roleDescription;

}