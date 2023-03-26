package org.ta.store.dto;

import lombok.Data;

import java.util.List;

@Data
public class StorePaginationResponse {

    private Long totalElements;

    private List<StoreDto> productList;

}
