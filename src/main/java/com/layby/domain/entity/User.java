package com.layby.domain.entity;

import lombok.*;
import jakarta.persistence.*;

import java.util.Set;

@Setter @Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "`user`")
@Entity
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "username", length = 50, unique = true)
    private String username;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "activated")
    private boolean activated;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Authority> authorities;
}