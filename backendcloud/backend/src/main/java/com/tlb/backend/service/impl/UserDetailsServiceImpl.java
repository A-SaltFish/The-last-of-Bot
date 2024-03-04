package com.tlb.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tlb.backend.mapper.UserMapper;
import com.tlb.backend.pojo.User;
import com.tlb.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    //注意如果密码是明文存储，需要在mysql的表里表项＋{noop}。否则则需要编写密码加密器；
    @Autowired
    UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        //querywrapper是用来进行匹配的，第一个参数代表匹配的列，第二个代表匹配值
        //querryWrapper可以进行多次条件的匹配，格式：queryWrapper.eq(x,x).le(x,x).....
        queryWrapper.eq("username",username);
        User user=userMapper.selectOne(queryWrapper);
        if(user==null){
            throw new RuntimeException("用户不存在");
        }
        return new UserDetailsImpl(user);
    }
}
