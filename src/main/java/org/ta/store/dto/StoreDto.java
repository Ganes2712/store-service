package org.ta.store.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class StoreDto {

    private Long id;
    private String countryCode;
    private Long sid;
    private String sku;
    private String prodName;
    private Long price;
    private LocalDateTime date;
    private Long createdBy;
   // private Set<Long> storeIdList;

}
