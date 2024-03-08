package com.tlb.backend.service.impl.ranklist;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tlb.backend.mapper.UserMapper;
import com.tlb.backend.pojo.User;
import com.tlb.backend.service.ranklist.GetRankListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetRankListServiceImpl implements GetRankListService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getList(Integer page) {
        IPage<User> userIPage=new Page<>(page,3);
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("rating");
        List<User> users =userMapper.selectPage(userIPage,queryWrapper).getRecords();
        JSONObject res=new JSONObject();
        for(User user:users)
            user.setPassword("");
        res.put("users",users);
        res.put("users_count",userMapper.selectCount(null));
        return res;
    }
}
