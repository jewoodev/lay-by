package com.layby.web.service;

import com.layby.domain.dto.response.WishItemSaveResponseDto;
import com.layby.domain.entity.WishItem;
import com.layby.domain.entity.WishList;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WishItemService {

    ResponseEntity<WishItemSaveResponseDto> save(WishItem wishItem);

    List<WishItem> findAllByWishListEntity(WishList wishList);
}
