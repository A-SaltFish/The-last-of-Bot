package com.tlb.backend.controller.user.bot;

import com.tlb.backend.service.user.bot.RemoveService;
import com.tlb.backend.service.user.bot.UpdateService;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin
@RestController
public class UpdateBotController {
    @Autowired
    private UpdateService updateService;

    @PostMapping({"/user/bot/update", "/user/bot/update/"})
    public Map<String,String> updateBot(@RequestParam Map<String,String>data){
        System.out.println("bot_update");
        return updateService.update(data);
    }


}
