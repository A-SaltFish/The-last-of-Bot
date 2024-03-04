package com.tlb.backend.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tlb.backend.mapper.BotMapper;
import com.tlb.backend.pojo.Bot;
import com.tlb.backend.pojo.User;
import com.tlb.backend.service.impl.utils.UserDetailsImpl;
import com.tlb.backend.service.user.bot.AddService;
import com.tlb.backend.service.user.bot.GetListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetListServiceImpl implements GetListService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public List<Bot> getList() {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=(UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser=(UserDetailsImpl) usernamePasswordAuthenticationToken.getPrincipal();
        User user=loginUser.getUser();
        QueryWrapper<Bot> queryWrapper=new QueryWrapper<>();
        //querywrapper里面的字段需要与数据库一致，pojo里面的需要驼峰命名法
        queryWrapper.eq("user_id",user.getId());
        return botMapper.selectList(queryWrapper);
    }
}
