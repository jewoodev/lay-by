package com.layby.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Setter @Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "`certification`")
@Entity
public class CertificationEntity {

    @Id
    @Column(name = "cf_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long certificationId;

    private String email;
    private String certificationNumber;
}