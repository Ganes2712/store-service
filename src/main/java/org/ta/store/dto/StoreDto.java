package org.ta.store.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class StoreDto {

    private Long id;
    private String countryCode;
    private Long sid;
    private String sku;
    private String prodName;
    @Min(0)
    private Long price;
    private LocalDateTime date;
    private Long createdBy;
   // private Set<Long> storeIdList;

}
