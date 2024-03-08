package com.tlb.backend.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tlb.backend.mapper.BotMapper;
import com.tlb.backend.pojo.Bot;
import com.tlb.backend.pojo.User;
import com.tlb.backend.service.impl.utils.UserDetailsImpl;
import com.tlb.backend.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddServiceImpl implements AddService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> add(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken=(UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser=(UserDetailsImpl) authenticationToken.getPrincipal();
        User user=loginUser.getUser();

        String title=data.get("title");
        String description=data.get("description");
        String content=data.get("content");

        Map<String,String> map =new HashMap<>();
        if(title==null||title.length()==0){
            map.put("error","标题不能为空 ");
            return map;
        }
        if(title.length()>100){
            map.put("error","标题过长 不能高于一百 ");
            return map;
        }
        if(description==null||description.length()==0){
            description="这个用户很懒，什么也没留下 ";
        }
        if(description!=null&&description.length()>300){
            map.put("error","bot描述过多 不能大于300 ");
            return map;
        }
        if(content==null||content.length()==0){
            map.put("error","bot代码不为空 ");
            return map;
        }
        if(content.length()>=10000){
            map.put("error","bot代码过长 ");
            return map;
        }
        QueryWrapper<Bot> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getId());
        if(botMapper.selectCount(queryWrapper)>=6){
            map.put("error","每个用户只能创建六个Bot!");
            return map;
        }

        Date now=new Date();
        Bot bot=new Bot(null,user.getId(),title,description,content,0.0f,now,now);
        botMapper.insert(bot);
        map.put("error","success");
        return map;
    }
}
