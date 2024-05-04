package com.itemservice.web.service;

import com.itemservice.domain.dto.ResponseDto;
import com.itemservice.domain.dto.WishItemDto;
import com.itemservice.domain.dto.WishListDto;
import com.itemservice.domain.entity.WishItem;
import com.itemservice.domain.vo.request.WishItemRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WishItemService {

    ResponseEntity<ResponseDto> save(WishItem wishItem);

    WishItem findByWishItemId(Long wishItemId);

    List<WishItem> findAllByUserId(Long userId);

    ResponseEntity<ResponseDto> increaseCount(Long wishItemId);

    ResponseEntity<ResponseDto> decreaseCount(Long wishItemId);

    ResponseEntity<ResponseDto> delete(Long wishItemId);

    ResponseEntity<ResponseDto> register(Long itemId, Long userId, WishItemRequest request);

    ResponseEntity<WishListDto> referWishList(Long userId);

    ResponseEntity<ResponseDto> chooseForPurchase(Long userId, List<WishItemDto> dtos);

    ResponseEntity<ResponseDto> chooseForPurchaseTest(Long userId);
}
