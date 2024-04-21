package com.layby.web.service.implement;

import com.layby.domain.entity.OrderItem;
import com.layby.domain.entity.WishList;
import com.layby.domain.repository.OrderItemRepository;
import com.layby.web.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.layby.web.service.OrderItemService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ItemService itemService;


    @Override
    public void save(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }

}
