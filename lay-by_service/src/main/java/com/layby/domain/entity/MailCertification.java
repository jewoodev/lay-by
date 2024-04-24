package com.layby.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "certification")
@Entity(name = "certification")
public class MailCertification {

    @Id
    @Column(name = "username")
    private String username;

    private String email;

    private String certificationNumber;
}