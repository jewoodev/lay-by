package com.itemservice.domain.repository;

import com.itemservice.domain.entity.Item;
import com.itemservice.domain.vo.request.ItemStockControlRequest;
import com.itemservice.domain.vo.request.ItemStockControlRequests;
import com.itemservice.web.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedissonLockItemFacade {

    private final RedissonClient redissonClient;
    private final ItemService itemService;

    @Transactional
    public void decrease(ItemStockControlRequests itemStockControlRequests) {
        List<ItemStockControlRequest> requestList = itemStockControlRequests.getItemStockControlRequests();
        for (ItemStockControlRequest request : requestList) {
            RLock lock = redissonClient.getLock(request.getItemId().toString());

            try {
                boolean available = lock.tryLock(20, 3, TimeUnit.SECONDS);

                if (!available) {
                    log.info("lock 획득 실패");
                    return;
                }

                log.info("락 획득, 로직 수행");
                Item item = itemService.findByItemId(request.getItemId());
                item.removeStock(request.getCount());
            } catch (InterruptedException ex) {
                log.info("에러 발생");
                throw new RuntimeException(ex);
            } finally {
                log.info("락 해제");
                lock.unlock();
            }
        }
    }
}
