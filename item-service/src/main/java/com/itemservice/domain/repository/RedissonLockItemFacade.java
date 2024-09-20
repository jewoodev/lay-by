package com.itemservice.domain.repository;

import com.itemservice.domain.vo.request.ItemStockControlRequest;
import com.itemservice.web.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedissonLockItemFacade {

    private final RedissonClient redissonClient;
    private final ItemService itemService;

    public void decrease(ItemStockControlRequest request) {

        RLock lock = redissonClient.getLock(request.getItemId().toString());

        try {
            lock.lock(20, TimeUnit.SECONDS);
            itemService.decreaseStock(request);
        } finally {
            lock.unlock();
        }
    }

}
