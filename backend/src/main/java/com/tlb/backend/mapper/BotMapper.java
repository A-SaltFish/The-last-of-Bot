package com.tlb.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tlb.backend.pojo.Bot;
import com.tlb.backend.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BotMapper extends BaseMapper<Bot> {

}
