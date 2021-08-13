package com.trello.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "is_hide")
    private Boolean isHide;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider")
    private AuthenticationProvider authProvider;

    @Column(name = "role_id")
    private Long roleId;

    @OneToMany(mappedBy = "user", targetEntity = BoardUser.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<BoardUser> boardUsers;

    @OneToMany(mappedBy = "user",targetEntity = TaskUser.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<TaskUser> taskUsers;

}
