package com.itemservice;

import com.itemservice.domain.common.RedisDao;
import com.itemservice.domain.entity.Item;
import com.itemservice.domain.repository.ItemRepository;
import com.itemservice.domain.repository.LettuceLockItemFacade;
import com.itemservice.domain.repository.RedissonLockItemFacade;
import com.itemservice.domain.vo.request.ItemStockControlRequest;
import com.itemservice.domain.vo.request.ItemStockControlRequestForRedis;
import com.itemservice.web.service.ItemService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
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
    private RedisDao redisDao;

    @Autowired
    private RedissonLockItemFacade redissonLockItemFacade;

    @Autowired
    private LettuceLockItemFacade lettuceLockItemFacade;

    private Long ITEM_ID = null;

    @BeforeEach
    public void before_mysql() {
        Item save = itemRepository.save(new Item(null, "공책", 1000, "튼튼하다", 3000, null, null));
        ITEM_ID = save.getItemId();
    }

//    @BeforeEach
//    public void before_redis() {
//        redisDao.setValue("공책", "3000", Duration.ofMillis(60 * 60 * 1000));
//    }

    @AfterEach
    public void after_mysql() {
        itemRepository.deleteAll();
    }

//    @AfterEach
//    public void after_redis() {
//        redisDao.deleteValue("공책");
//    }

    @Test
    public void 동시성_제어_비관적락() throws InterruptedException {
        int processingCnt = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(30);
        CountDownLatch latch = new CountDownLatch(processingCnt);

        for (int i = 0; i < processingCnt; i++) {
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
        int processingCnt = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(30);
        CountDownLatch latch = new CountDownLatch(processingCnt);

        for (int i = 0; i < processingCnt; i++) {
            ItemStockControlRequestForRedis request = new ItemStockControlRequestForRedis(1L, "공책", 1);
            executorService.submit(() -> {
                try {
                    redissonLockItemFacade.decrease(request);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        String value = redisDao.getValue("공책");
        int stockQuantity = Integer.parseInt(value);

        assertEquals(0, stockQuantity);
    }

    @Test
    public void 동시성_제어_레투스() throws InterruptedException {
        int processingCnt = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(30);
        CountDownLatch latch = new CountDownLatch(processingCnt);

        for (int i = 0; i < processingCnt; i++) {
            ItemStockControlRequestForRedis request = new ItemStockControlRequestForRedis(1L, "공책", 1);
            executorService.submit(() -> {
                try {
                    lettuceLockItemFacade.decrease(request);
                } catch (InterruptedException e) {
                    System.out.println("예외 발생: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        String value = redisDao.getValue("공책");
        int stockQuantity = Integer.parseInt(value);

        assertEquals(0, stockQuantity);
    }
}
