package com.wzoto.interfaces.vo.learning;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeakPointVO {

    private String knowledgePoint;
    private Integer mistakeCount;
}
