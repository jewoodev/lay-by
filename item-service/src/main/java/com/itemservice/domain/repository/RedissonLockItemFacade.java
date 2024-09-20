package com.itemservice.domain.repository;

import com.itemservice.domain.common.RedisDao;
import com.itemservice.domain.vo.request.ItemStockControlRequestForRedis;
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
    private final RedisDao redisDao;

    public void decrease(ItemStockControlRequestForRedis request) {

        RLock lock = redissonClient.getLock(request.getItemId().toString());

        try {
            lock.lock(20, TimeUnit.SECONDS);

            String itemName = request.getItemName();
            String value = redisDao.getValue(itemName);
            int count = Integer.parseInt(value);
            redisDao.setValue(itemName, String.valueOf(count - request.getCount()));
        } finally {
            lock.unlock();
        }
    }

}
