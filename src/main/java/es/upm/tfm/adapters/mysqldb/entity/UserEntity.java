package es.upm.tfm.adapters.mysqldb.entity;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(name = "userName")
    private String userName;
    @Column(name = "userFirstName")
    private String userFirstName;
    @Column(name = "userLastName")
    private String userLastName;
    @Email
    @Column(name = "email")
    private String email;
    @Column(name = "userPassword")
    private String userPassword;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLE",
            joinColumns = {
                    @JoinColumn(name = "USER_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "ROLE_ID")
            }
    )
    private Set<RoleEntity> role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderEntity> orders;

    public UserEntity(String userName, String userFirstName, String userLastName) {
        this.userName = userName;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
    }

    public UserEntity(String userName, String userFirstName, String userLastName, Set<RoleEntity> role) {
        this.userName = userName;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.role = role;
    }


}
