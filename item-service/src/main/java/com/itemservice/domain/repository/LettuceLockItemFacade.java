package com.itemservice.domain.repository;

import com.itemservice.domain.vo.request.ItemStockControlRequest;
import com.itemservice.web.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LettuceLockItemFacade {

    private final RedisRepository repository;
    private final ItemService itemService;

    public void decrease(ItemStockControlRequest request) throws InterruptedException {

        Long chooseId = request.getItemId();
        while (!repository.lock(chooseId)) {
            Thread.sleep(100);
        }

        try {
            itemService.decreaseStock(request);
        } finally {
            repository.unlock(chooseId);
        }
    }
}
