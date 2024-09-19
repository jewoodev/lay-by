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

//                if (!available) {
//                    log.info("lock 획득 실패");
//                    return;
//                }

//            log.info("락 획득, 로직 수행");
            itemService.decreaseStock(request);
//            } catch (InterruptedException ex) {
//                log.info("에러 발생");
//                throw new RuntimeException(ex);
        } finally {
//            log.info("락 해제");
            lock.unlock();
        }
    }

}
