package org.ta.store.dto;

import lombok.Data;

import java.util.List;

@Data
final public class StoreReqPageDto {

    private String kvo;//key~value~operator
    private Integer pageNo;
    private Integer pageSize;
    private String sortBy;
    private String sortType;

}
