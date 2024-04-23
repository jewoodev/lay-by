package com.layby.domain.repository.implement;

import com.layby.domain.entity.QWishItem;
import com.layby.domain.entity.User;
import com.layby.domain.entity.WishItem;
import com.layby.domain.repository.WishItemRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.layby.domain.entity.QItem.item;
import static com.layby.domain.entity.QWishItem.*;

@RequiredArgsConstructor
public class WishItemRepositoryImpl implements WishItemRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<WishItem> findAllByUserIdWithItem(Long userId) {
        return query
                .selectFrom(wishItem)
                .join(wishItem.item, item).fetchJoin()
                .where(wishItem.user.userId.eq(userId))
                .fetch();
    }
}
