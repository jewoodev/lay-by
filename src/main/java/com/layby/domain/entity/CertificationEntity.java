package com.layby.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Setter @Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "certification")
@Entity(name = "certification")
public class CertificationEntity {

    @Id
    @Column(name = "username")
    private String username;

    private String email;

    private String certificationNumber;
}