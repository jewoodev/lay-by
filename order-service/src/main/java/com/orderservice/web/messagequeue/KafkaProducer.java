package com.orderservice.web.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orderservice.domain.dto.ItemStockDtoList;
import com.orderservice.domain.dto.WishItemDto;
import com.orderservice.domain.dto.WishListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public ItemStockDtoList send(String kafkaTopic, ItemStockDtoList itemStockDtoList) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";

        try {
            jsonInString = mapper.writeValueAsString(itemStockDtoList);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        kafkaTemplate.send(kafkaTopic, jsonInString);
        log.info("Kafka Producer send data from the item-service: {}", jsonInString);

        return itemStockDtoList;
    }
}
