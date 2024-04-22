package com.layby.web.service;

import com.layby.domain.dto.request.WishItemSaveRequestDto;
import com.layby.domain.dto.response.ResponseDto;
import com.layby.domain.dto.response.WishItemResponseDto;
import com.layby.domain.dto.response.WishListReferResponseDto;
import com.layby.domain.entity.User;
import com.layby.domain.entity.WishItem;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface WishItemService {

    ResponseEntity<ResponseDto> save(WishItem wishItem);

    WishItem findById(Long wishItemId);

    List<WishItem> findAllByUser(User user);

    ResponseEntity<ResponseDto> increaseCount(Long wishItemId);

    ResponseEntity<ResponseDto> decreaseCount(Long wishItemId);

    ResponseEntity<ResponseDto> delete(Long wishItemId);

    ResponseEntity<ResponseDto> register(Long itemId, Authentication authentication, WishItemSaveRequestDto dto);

    ResponseEntity<WishListReferResponseDto> referWishList(Long userId);

    ResponseEntity<ResponseDto> purchaseWishList(
            Long userId, Long addressId, List<WishItemResponseDto> dtos);
}
