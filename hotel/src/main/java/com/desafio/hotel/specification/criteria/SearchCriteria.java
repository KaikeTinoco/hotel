package com.desafio.hotel.specification.criteria;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;


}
