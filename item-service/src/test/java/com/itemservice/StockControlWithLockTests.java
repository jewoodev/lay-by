package com.itemservice;

import com.itemservice.domain.entity.Item;
import com.itemservice.domain.repository.ItemRepository;
import com.itemservice.domain.repository.LettuceLockItemFacade;
import com.itemservice.domain.repository.RedissonLockItemFacade;
import com.itemservice.domain.vo.request.ItemStockControlRequest;
import com.itemservice.web.service.ItemService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @Autowired
    private LettuceLockItemFacade lettuceLockItemFacade;

    private Long ITEM_ID = null;

    @BeforeEach
    public void before() {
        Item save = itemRepository.save(new Item(null, "공책", 1000, "튼튼하다", 3000, null, null));
        ITEM_ID = save.getItemId();
    }

    @AfterEach
    public void after() {
        itemRepository.deleteAll();
    }

    @Test
    public void 동시성_제어_비관적락() throws InterruptedException {
        int threadCount = 3000;
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            ItemStockControlRequest request = new ItemStockControlRequest(ITEM_ID, 1);
            executorService.submit(() -> {
                try {
                    itemService.decreaseStockWithPMCLock(request);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        int stockQuantity = itemService.findByItemId(ITEM_ID).getStockQuantity();

        assertEquals(0, stockQuantity);
    }

    @Test
    public void 동시성_제어_레디슨() throws InterruptedException {
        int threadCount = 3000;
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            ItemStockControlRequest request = new ItemStockControlRequest(ITEM_ID, 1);
            executorService.submit(() -> {
                try {
                    redissonLockItemFacade.decrease(request);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        int stockQuantity = itemService.findByItemId(ITEM_ID).getStockQuantity();

        assertEquals(0, stockQuantity);
    }

    @Test
    public void 동시성_제어_레투스() throws InterruptedException {
        int threadCount = 3000;
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            ItemStockControlRequest request = new ItemStockControlRequest(ITEM_ID, 1);
            executorService.submit(() -> {
                try {
                    lettuceLockItemFacade.decrease(request);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
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
