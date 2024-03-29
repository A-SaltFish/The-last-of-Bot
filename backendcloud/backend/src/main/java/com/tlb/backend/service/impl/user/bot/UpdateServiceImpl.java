package com.tlb.backend.service.impl.user.bot;

import com.tlb.backend.mapper.BotMapper;
import com.tlb.backend.pojo.Bot;
import com.tlb.backend.pojo.User;
import com.tlb.backend.service.impl.utils.UserDetailsImpl;
import com.tlb.backend.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateServiceImpl implements UpdateService {

    @Autowired
    BotMapper botMapper;
    @Override
    public Map<String, String> update(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken=(UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser=(UserDetailsImpl) authenticationToken.getPrincipal();
        User user=loginUser.getUser();

        int bot_id=Integer.parseInt(data.get("bot_id"));
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
        Bot bot=botMapper.selectById(bot_id);
        if(bot==null){
            map.put("error","Bot不存在或者已被删除");
             return  map;
        }
        if(!bot.getUserId().equals(user.getId())){
            map.put("error","没有权限修改该Bot");
            return map;
        }
        Bot new_bot=new Bot(bot.getId(),user.getId(),title,description,content,bot.getWinRate() ,bot.getCreateTime(),new Date());
        botMapper.updateById(new_bot);
        map.put("error","success");
        return map;
    }
}
