package com.itemservice;

import com.itemservice.domain.entity.Item;
import com.itemservice.domain.repository.ItemRepository;
import com.itemservice.domain.repository.RedissonLockItemFacade;
import com.itemservice.domain.vo.request.ItemStockControlRequest;
import com.itemservice.domain.vo.request.ItemStockControlRequests;
import com.itemservice.web.service.ItemService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class StockControlWithLockTests {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private RedissonLockItemFacade redissonLockItemFacade;

    private Long ITEM_ID = null;

    @BeforeEach
    public void before() {
        Item save = itemRepository.save(new Item(null, "공책", 1000, "튼튼하다", 1000, null, null));
        ITEM_ID = save.getItemId();
    }

    @AfterEach
    public void after() {
        itemRepository.deleteAll();
    }

    // 10sec 64ms
    @Test
    public void 동시에_100개_요청_비관적락() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            ItemStockControlRequest request = new ItemStockControlRequest(ITEM_ID, 1);
            List<ItemStockControlRequest> requests = new ArrayList<>();
            requests.add(request);
            executorService.submit(() -> {
                try {
                    itemService.decreaseStock(new ItemStockControlRequests(requests));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        int stockQuantity = itemService.findByItemId(ITEM_ID).getStockQuantity();

        assertEquals(0, stockQuantity);
    }

    // 19sec 599ms
    @Test
    public void 동시에_100개_요청_레디슨() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            ItemStockControlRequest request = new ItemStockControlRequest(ITEM_ID, 1);
            List<ItemStockControlRequest> requests = new ArrayList<>();
            requests.add(request);
            executorService.submit(() -> {
                try {
                    redissonLockItemFacade.decrease(new ItemStockControlRequests(requests));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        int stockQuantity = itemService.findByItemId(ITEM_ID).getStockQuantity();

        assertEquals(0, stockQuantity);
    }
}
