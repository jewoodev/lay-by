package com.layby.web.service.implement;

import com.layby.domain.common.ErrorCode;
import com.layby.domain.dto.request.WishItemSaveRequestDto;
import com.layby.domain.dto.response.ResponseDto;
import com.layby.domain.dto.response.WishItemResponseDto;
import com.layby.domain.dto.response.WishListReferResponseDto;
import com.layby.domain.entity.*;
import com.layby.domain.repository.WishItemRepository;
import com.layby.web.exception.DatabaseErrorException;
import com.layby.web.exception.InternalServerErrorException;
import com.layby.web.service.*;
import com.layby.web.util.AES256;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service @Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishItemServiceImpl implements WishItemService {

    private final WishItemRepository wishItemRepository;
    private final ItemService itemService;
    private final UserService userService;
    private final OrderService orderService;
    private final AddressService addressService;
    private final DeliveryService deliveryService;
    private final AES256 personalDataEncoder;

    @Override
    @Transactional
    public ResponseEntity<ResponseDto> save(WishItem wishItem) {
        try {
            wishItemRepository.save(wishItem);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseErrorException(ErrorCode.DATABASE_ERROR.getMessage());
        }

        return ResponseDto.success();
    }

    @Override
    public WishItem findByWishItemId(Long wishItemId) {
        return wishItemRepository.findByWishItemId(wishItemId);
    }

    @Override
    public List<WishItem> findAllByUser(User user) {
        return wishItemRepository.findAllByUser(user);
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
    public ResponseEntity<ResponseDto> register(Long itemId, Authentication authentication, WishItemSaveRequestDto dto) {

        String username = authentication.getPrincipal().toString(); // 로그인 된 유저의 네임을 가져온다
        String encodedUsername = null;
        try {
            encodedUsername = personalDataEncoder.encode(username);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        }

        User user = userService.findByUsername(encodedUsername);
        Item item = itemService.findByItemId(itemId);


        WishItem wishItem = WishItem.builder()
                .item(item)
                .price(item.getPrice())
                .count(dto.getCount())
                .user(user)
                .build();

        wishItemRepository.save(wishItem);

        return ResponseDto.success();
    }

    @Override
    public ResponseEntity<WishListReferResponseDto> referWishList(Long userId) {
        User user = userService.findByUserId(userId);

        // 사용자의 위시 리스트에 담긴 위시 아이템 리스트를 가져온다
        List<WishItem> allByUser = wishItemRepository.findAllByUser(user);

        // 가져온 위시 아이템 리스트를 응답 Dto로 변환한다.
        WishListReferResponseDto wishlistReferResponseDto = new WishListReferResponseDto(allByUser);

        return ResponseEntity.status(HttpStatus.OK).body(wishlistReferResponseDto);
    }

    /**
     purchaseWishList 흐름
     1. 클라이언트가 위시리스트에서 구매할 위시 아이템을 고른 후 결제창으로 넘어간다.
     2. 결제창에서 이번에 배송받을 주소 정보를 선택한다.
     3. user, delivery, orderItems 로 주문을 생성해 저장한다.
     */
    @Override
    @Transactional
    public ResponseEntity<ResponseDto> purchaseWishList(
            Long userId, Long addressId, List<WishItemResponseDto> dtos
    ) {
        User user = userService.findByUserId(userId);
        Address address = addressService.findByAddressId(addressId);
        List<OrderItem> orderItems = new ArrayList<>(); // 오더에 매핑할 오더아이템 리스트

        // 사용자가 고른 위시 아이템들을 orderItems에 넣는 루프
        for (WishItemResponseDto dto : dtos) {

            // 선택한 수량만큼 재고를 감소시킨다.
            Item item = itemService.findByItemId(dto.getItemId());
            item.removeStock(dto.getCount());

            // 선택한 위시 아이템을 가져온다.
            WishItem wishItem = findByWishItemId(dto.getWishItemId());

            // 구입 후 위시 리스트에서 삭제된다.
            wishItemRepository.delete(wishItem);

            // 그 위시 아이템을 오더 아이템으로 변환한다.
            OrderItem orderItem = OrderItem.convertFromWishItem(wishItem);

            // 그 오더아이템을 오더아이템 리스트에 넣는다.
            orderItems.add(orderItem);
        }

        // 사용자가 선택한 주소값으로 Delivery를 생성한다.
        Delivery delivery = new Delivery(address);

        // 해당 결제에 대한 오더를 생성한 후 저장한다.
        Order order = Order.createOrder(user, delivery, orderItems);
        orderService.save(order);

        return ResponseDto.success();
    }

    /**
     purchaseWishListTest 흐름
     1. 클라이언트가 위시리스트에서 구입할 품목들만 선택하는 것 없이 위시리스트의 아이템들을 모두 구매한다.
     2. 사용자가 갖고 있는 주소 중 처음으로 조회되는 곳으로 주문한다.
     3. user, delivery, orderItems 로 주문을 생성해 저장한다.
     */
    @Override
    @Transactional
    public ResponseEntity<ResponseDto> purchaseWishListTest(Long userId) {
        List<WishItem> wishItems = wishItemRepository.findAllByUserIdWithItem(userId);
        User user = userService.findByUserId(userId);
        Address address = addressService.findAllByUser(user).get(0);
        List<OrderItem> orderItems = new ArrayList<>(); // 오더에 매핑할 오더아이템 리스트

        // 각 상품의 재고를 위시리스트에 담은 갯수만큼 감소시키고, 처리가 끝난 상품은 위시리스트에서 제거한다.
        for (WishItem wishItem : wishItems) {

            // 선택한 수량만큼 재고를 감소시킨다.
            Item item = itemService.findByItemId(wishItem.getItem().getItemId());
            item.removeStock(wishItem.getCount());

            // 구입한 상품은 위시 리스트에서 삭제된다.
            wishItemRepository.delete(wishItem);

            // 위시 아이템을 오더 아이템으로 변환한다.
            OrderItem orderItem = OrderItem.convertFromWishItem(wishItem);

            // 그 오더아이템을 오더아이템 리스트에 넣는다.
            orderItems.add(orderItem);
        }

        // 사용자가 선택한 주소값으로 Delivery를 생성한다.
        Delivery delivery = new Delivery(address);

        // 해당 결제에 대한 오더를 생성한 후 배송 정보에 매핑하고
        Order order = Order.createOrder(user, delivery, orderItems);

        deliveryService.save(delivery);
        orderService.save(order);

        return ResponseDto.success();
    }
}
