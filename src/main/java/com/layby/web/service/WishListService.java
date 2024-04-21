package com.layby.web.service;

import com.layby.domain.dto.request.WishItemSaveRequestDto;
import com.layby.domain.dto.response.WishListReferResponseDto;
import com.layby.domain.entity.User;
import com.layby.domain.entity.WishList;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface WishListService {

    void init(Long userId);

    WishList findByUserEntity(User user);

    void register(Long itemId, Authentication authentication, WishItemSaveRequestDto dto);

    ResponseEntity<WishListReferResponseDto> referWishlist(Long userId);
}
