package com.itemservice.web.service.implement;

import com.itemservice.domain.dto.AddressDto;
import com.itemservice.domain.dto.ResponseDto;
import com.itemservice.domain.dto.WishItemDto;
import com.itemservice.domain.dto.WishListDto;
import com.itemservice.domain.entity.Address;
import com.itemservice.domain.entity.Item;
import com.itemservice.domain.entity.WishItem;
import com.itemservice.web.client.OrderServiceClient;
import com.itemservice.web.exception.DatabaseErrorException;
import com.itemservice.domain.repository.WishItemRepository;
import com.itemservice.web.service.AddressService;
import com.itemservice.web.service.ItemService;
import com.itemservice.web.service.WishItemService;
import com.itemservice.domain.vo.WishItemRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.itemservice.domain.common.ErrorCode.DATABASE_ERROR;
import static org.springframework.http.HttpStatus.*;


@Service @Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishItemServiceImpl implements WishItemService {

    private final WishItemRepository wishItemRepository;
    private final ItemService itemService;
    private final AddressService addressService;
    private final OrderServiceClient orderServiceClient;

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> save(WishItem wishItem) {
        try {
            wishItemRepository.save(wishItem);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseErrorException(DATABASE_ERROR.getMessage());
        }

        return ResponseDto.success();
    }

    @Override
    public WishItem findByWishItemId(Long wishItemId) {
        return wishItemRepository.findByWishItemId(wishItemId);
    }

    public List<WishItem> findAllByUserId(Long userId) {
        return wishItemRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> increaseCount(Long wishItemId) {
        WishItem wishItem = findByWishItemId(wishItemId);
        wishItem.increaseCount();

        return ResponseDto.success();
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> decreaseCount(Long wishItemId) {
        WishItem wishItem = findByWishItemId(wishItemId);
        wishItem.decreaseCount();

        return ResponseDto.success();
    }

    @Override
    public ResponseEntity<ResponseDto> delete(Long wishItemId) {
        WishItem forDelete = findByWishItemId(wishItemId);
        wishItemRepository.delete(forDelete);

        return ResponseDto.success();
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> register(Long itemId, Long userId, WishItemRequest request) {

        Item item = itemService.findByItemId(itemId);


        WishItem wishItem = WishItem.builder()
                .itemId(itemId)
                .itemName(item.getItemName())
                .price(item.getPrice())
                .count(request.getCount())
                .userId(userId)
                .build();

        wishItemRepository.save(wishItem);

        return ResponseDto.success();
    }

    @Override
    public ResponseEntity<WishListDto> referWishList(Long userId) {

        // 사용자의 위시 리스트에 담긴 위시 아이템 리스트를 가져온다
        List<WishItem> allByUser = wishItemRepository.findAllByUserId(userId);

        // 가져온 위시 아이템 리스트를 응답 Dto로 변환한다.
        WishListDto wishListDto = WishListDto.fromWishItems(allByUser);

        return ResponseEntity.status(OK).body(wishListDto);
    }

    /**
     purchaseWishList 흐름
     1. 클라이언트가 위시리스트에서 구매할 위시 아이템을 고른 후 결제 창으로 넘어간다.
     2. 결제창에서 이번에 배송받을 주소 정보를 선택한다.
     3. 만든 주문 정보를 order-service에 넘겨준다.
     */
    @Override
    @Transactional
    public ResponseEntity<ResponseDto> purchaseWishList(Long userId, AddressDto dto, List<WishItemDto> dtos) {
        WishListDto wishListDto = WishListDto.fromWishItemDtos(dtos);
        orderServiceClient.purchaseWishList(userId, dto.getAddressId(), wishListDto);

        for (WishItemDto wishItemDto : dtos) {
            wishItemRepository.deleteByWishItemId(wishItemDto.getWishItemId());
        }

        return ResponseDto.success();
    }

    /**
     purchaseWishListTest 흐름
     1. 클라이언트가 위시리스트에서 구입할 품목들만 선택하는 것 없이 위시리스트의 아이템들을 모두 선택한다.
     2. 사용자가 갖고 있는 주소 중 처음으로 조회되는 곳으로 선택한다.
     3. 만든 주문 정보를 order-service에 넘겨준다.
     */
    @Override
    @Transactional
    public ResponseEntity<ResponseDto> purchaseWishListTest(Long userId) {
        List<WishItem> wishItems = wishItemRepository.findAllByUserId(userId);
        Long addressId = addressService.findAllByUserId(userId).get(0).getAddressId();

        WishListDto wishListDto = WishListDto.fromWishItems(wishItems);

        orderServiceClient.purchaseWishList(userId, addressId, wishListDto);

        for (WishItem wishItem : wishItems) {
            Item item = itemService.findByItemId(wishItem.getItemId());
            item.removeStock(wishItem.getCount());
            wishItemRepository.delete(wishItem);
        }

        return ResponseDto.success();
    }
}
