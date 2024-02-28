package com.tlb.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bot {
    //主键自增的标志
    @TableId(type= IdType.AUTO)
    private Integer id;


    //!!!pojo里面的变量命名不能用下划线，如果数据库里的col是具有下划线，则在这里会切换为驼峰.uiser_id->userId
    private Integer userId;
    private String title;
    private String description;
    private String content;
    private Integer rating;
    private Float winRate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

}
