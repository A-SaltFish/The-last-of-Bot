package com.tlb.matchingsystem.service.impl.utils;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private Integer userId;
    private Integer rating;
    private Integer waitingTime;    //等待时间
    //当前用户所匹配的类型，如果是-1代表是玩家亲自上阵，如果不是代表是人机出战
    private Integer botId;
}
