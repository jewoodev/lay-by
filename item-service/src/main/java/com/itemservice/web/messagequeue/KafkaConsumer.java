package com.itemservice.web.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itemservice.domain.dto.ItemStockDto;
import com.itemservice.domain.dto.ItemStockDtoList;
import com.itemservice.domain.entity.Item;
import com.itemservice.web.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaConsumer {

    private final ItemService itemService;

    /** order-service에서 order에 실패했을 때 보상 트랜잭션을 위한 컨슈밍 메서드 **/
    @Transactional
    @KafkaListener(topics = "order-failed")
    public void makeOrder(String kafkaMessage) {
        log.info("Kafka message : {}", kafkaMessage);

        ItemStockDtoList itemStockDtoList = null;
        ObjectMapper mapper = new ObjectMapper();

        try {
            itemStockDtoList = mapper.readValue(kafkaMessage, ItemStockDtoList.class);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        log.info("Convert to : {}", itemStockDtoList);

        List<ItemStockDto> itemStockDtos = itemStockDtoList.getItemStockDtos();

        for (ItemStockDto itemStockDto : itemStockDtos) {
            Item item = itemService.findByItemId(itemStockDto.getItemId());
            item.addStock(itemStockDto.getCount());
        }
    }
}
