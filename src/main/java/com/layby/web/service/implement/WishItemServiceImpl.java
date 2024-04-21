package com.layby.web.service.implement;

import com.layby.domain.common.ErrorCode;
import com.layby.domain.dto.response.WishItemSaveResponseDto;
import com.layby.domain.entity.WishItem;
import com.layby.domain.entity.WishList;
import com.layby.domain.repository.WishItemRepository;
import com.layby.web.exception.DatabaseErrorException;
import com.layby.web.service.WishItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishItemServiceImpl implements WishItemService {

    private final WishItemRepository wishItemRepository;

    @Override
    public ResponseEntity<WishItemSaveResponseDto> save(WishItem wishItem) {
        try {
            wishItemRepository.save(wishItem);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseErrorException(ErrorCode.DATABASE_ERROR.getMessage());
        }

        return WishItemSaveResponseDto.success();
    }

    @Override
    public List<WishItem> findAllByWishListEntity(WishList wishList) {
        return wishItemRepository.findAllByWishList(wishList);
    }
}
