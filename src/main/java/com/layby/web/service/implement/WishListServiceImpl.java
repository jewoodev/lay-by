package com.layby.web.service.implement;

import com.layby.domain.common.ErrorCode;
import com.layby.domain.dto.request.WishItemSaveRequestDto;
import com.layby.domain.dto.response.WishListReferResponseDto;
import com.layby.domain.entity.*;
import com.layby.domain.repository.WishListRepository;
import com.layby.web.exception.InternalServerErrorException;
import com.layby.web.service.*;
import com.layby.web.util.AES256;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishlistRepository;
    private final WishItemService wishItemService;
    private final ItemService itemService;
    private final UserService userService;
    private final OrderItemService orderItemService;
    private final AES256 personalDataEncoder;

    @Override
    @Transactional
    public void init(Long userId) {
        User user = userService.findByUserId(userId);
        WishList wishList = wishlistRepository.findByUser(user);

        // 아직 생성되지 않았을 경우 생성해 저장한다.
        if (wishList == null) {
            wishList = WishList.builder()
                    .user(user)
                    .build();

            wishlistRepository.save(wishList);
        }
    }

    @Override
    public WishList findByUserEntity(User user) {
        return wishlistRepository.findByUser(user);
    }

    @Override
    @Transactional
    public void register(Long itemId, Authentication authentication, WishItemSaveRequestDto dto) {

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

        init(user.getUserId()); // 위시 리스트 초기 세팅
        WishList wishList = findByUserEntity(user); // Id 값이 생긴 위시 리스트를 가져온다

        WishItem wishItem = WishItem.builder()
                .item(item)
                .price(item.getPrice())
                .count(dto.getCount())
                .wishList(wishList)
                .build();
    }

    @Override
    public ResponseEntity<WishListReferResponseDto> referWishlist(Long userId) {
        User user = userService.findByUserId(userId);

        // 사용자의 위시 리스트에 담긴 위시 아이템 리스트를 가져온다
        WishList wishlist = wishlistRepository.findByUser(user);
        List<WishItem> allByWishListEntity = wishItemService.findAllByWishListEntity(wishlist);

        // 가져온 위시 아이템 리스트를 응답 Dto로 변환한다.
        WishListReferResponseDto wishlistReferResponseDto = new WishListReferResponseDto(allByWishListEntity);

        return ResponseEntity.status(HttpStatus.OK).body(wishlistReferResponseDto);
    }

}
