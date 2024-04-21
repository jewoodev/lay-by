package com.layby.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "wish_list")
@Entity(name = "wish_list")
public class WishList extends BaseTimeEntity {

    @Id @Column(name = "wish_list_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishlistId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "wishList", cascade = CascadeType.ALL)
    private List<WishItem> wishItems;
}