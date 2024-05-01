package com.itemservice.domain.vo.request;


import com.itemservice.domain.vo.request.ItemStockControlRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemStockControlRequests {

    List<ItemStockControlRequest> itemStockControlRequests;
}
