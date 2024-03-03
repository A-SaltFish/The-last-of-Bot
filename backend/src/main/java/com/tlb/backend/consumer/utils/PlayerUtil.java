package com.tlb.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.logging.log4j.util.StringBuilders;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class PlayerUtil {

    private Integer id;
    private Integer sx;
    private Integer sy;
    private List<Integer> steps;

    //检查蛇什么时候会变长
    private boolean check_tail_increasing(int step){
        if(step<=10) return true;
        return step%3==1;
    }

    public List<Cell> getCells(){
        List<Cell> res=new ArrayList<>();
        int[] dx={-1,0,1,0},dy={0,1,0,-1};
        int x=sx,y=sy;
        int step=0;
        res.add(new Cell(x,y));
        for(int d:steps){
            x+=dx[d];
            y+=dy[d];
            res.add(new Cell(x,y));
            //没有增长，说明之前的蛇尾就要移动，也就移除了
            if(!check_tail_increasing(++step)){
                res.remove(0);
            }
        }
        return res;
    }

    public String getStepsString(){
        StringBuilder res=new StringBuilder();
        for(int d:steps){
            res.append(d);
        }
        return res.toString();
    }

}
