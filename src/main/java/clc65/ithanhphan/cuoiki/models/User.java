package clc65.ithanhphan.cuoiki.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String fullName;

    private String email;

    private String phone;

    private Boolean enabled;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}