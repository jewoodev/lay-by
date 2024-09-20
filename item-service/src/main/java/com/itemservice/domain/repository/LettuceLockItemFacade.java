package com.itemservice.domain.repository;

import com.itemservice.domain.common.RedisDao;
import com.itemservice.domain.vo.request.ItemStockControlRequest;
import com.itemservice.domain.vo.request.ItemStockControlRequestForRedis;
import com.itemservice.web.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LettuceLockItemFacade {

    private final RedisRepository repository;
    private final RedisDao redisDao;

    public void decrease(ItemStockControlRequestForRedis request) throws InterruptedException {

        Long chooseId = request.getItemId();
        while (!repository.lock(chooseId)) {
            Thread.sleep(100);
        }

        try {
            String itemName = request.getItemName();
            String value = redisDao.getValue(itemName);
            int count = Integer.parseInt(value);
            redisDao.setValue(itemName, String.valueOf(count - request.getCount()));
        } finally {
            repository.unlock(chooseId);
        }
    }
}
