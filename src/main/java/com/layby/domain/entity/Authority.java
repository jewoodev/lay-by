package com.layby.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "authority")
@Entity
public class Authority {

    @Column(name = "authority_id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "authority_name", length = 50)
    private String authorityName;
}
