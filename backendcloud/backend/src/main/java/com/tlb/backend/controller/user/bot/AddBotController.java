package com.tlb.backend.controller.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tlb.backend.mapper.UserMapper;
import com.tlb.backend.pojo.User;
import com.tlb.backend.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
public class AddBotController {
    @Autowired
    private AddService addService;

    @PostMapping({"/user/bot/add", "/user/bot/add/"})
    public Map<String,String> addBot(@RequestParam Map<String,String>data){
        System.out.println("bot_add");
        return addService.add(data);
    }


}
