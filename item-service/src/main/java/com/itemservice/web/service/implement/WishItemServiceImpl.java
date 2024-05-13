package com.itemservice.web.service.implement;

import com.itemservice.domain.dto.ResponseDto;
import com.itemservice.domain.dto.WishItemDto;
import com.itemservice.domain.dto.WishListDto;
import com.itemservice.domain.entity.Item;
import com.itemservice.domain.entity.WishItem;
import com.itemservice.domain.vo.request.ItemStockControlRequests;
import com.itemservice.web.client.OrderServiceClient;
import com.itemservice.web.exception.DatabaseErrorException;
import com.itemservice.domain.repository.WishItemRepository;
import com.itemservice.web.messagequeue.KafkaProducer;
import com.itemservice.web.service.ItemService;
import com.itemservice.web.service.WishItemService;
import com.itemservice.domain.vo.request.WishItemRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.itemservice.domain.common.ErrorCode.DATABASE_ERROR;
import static org.springframework.http.HttpStatus.*;


@Service @Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishItemServiceImpl implements WishItemService {

    private final WishItemRepository wishItemRepository;
    private final ItemService itemService;
    private final OrderServiceClient orderServiceClient;
    private final KafkaProducer kafkaProducer;

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
     chooseForPurchase 흐름
     1. 클라이언트가 위시리스트에서 구매할 위시 아이템을 고른 후 결제 페이지로 진입한다. 이 때, 각 상품들의 재고가 감소된다.
     2. 구입할 상품들과 주소값을 order-service에 통신을 통해 전달함으로써 Order를 생성한다.
     */
    @Override
    @Transactional
    public ResponseEntity<ResponseDto> chooseForPurchase(Long userId, List<WishItemDto> dtos) {
        // 백엔드 로직만 두고 테스트하기 좋은 로직은 아니므로 우선순위를 미뤄둔다.
//        WishListDto wishListDto = WishListDto.fromWishItemDtos(dtos);
//        orderServiceClient.purchaseWishList(userId, dto.getAddressId(), wishListDto);
//
//        for (WishItemDto wishItemDto : dtos) {
//            wishItemRepository.deleteByWishItemId(wishItemDto.getWishItemId());
//        }
//
//        return ResponseDto.success();
        return null;
    }

    /**
     chooseForPurchaseTest 흐름
     1. 클라이언트가 위시리스트에서 구입할 품목들만 선택하는 것 없이 위시리스트의 아이템들을 모두 선택한다.
     이 때, 각 상품들의 재고가 감소된다.
     2. 구입할 상품들과 주소값을 order-service에 통신을 통해 전달함으로써 Order를 생성한다.
     */
    @Override
    @Transactional
    public ResponseEntity<ResponseDto> chooseForPurchaseTest(Long userId) {
        List<WishItem> wishItems = wishItemRepository.findAllByUserId(userId);

        // 각 상품들을 몇 개씩 주문하길 선택했는지에 대한 VO.
        ItemStockControlRequests itemStockControlRequests = ItemStockControlRequests.fromWishItems(wishItems);
        // VO로 재고 처리를 해준다.
        itemService.decreaseStock(itemStockControlRequests);

        /* 이제 위시리스트 상품들로 Order를 만들기 위해 order-service에 통신을 통해 wishListDto를 보낼 차례다.
            wishListDto를 만들어서,                 */
        WishListDto wishListDto = WishListDto.fromWishItems(wishItems);

        // order-service에 전달해서 order가 생성되게 끔 한다.
        kafkaProducer.send("make-order", wishListDto);

        // 주문한 상품들은 위시리스트에서 삭제한다.
        for (WishItem wishItem : wishItems) {
            Item item = itemService.findByItemId(wishItem.getItemId());
            wishItemRepository.delete(wishItem);
        }

        return ResponseDto.success();
    }
}
