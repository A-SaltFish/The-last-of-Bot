package com.tlb.backend.controller.user.bot;

import com.tlb.backend.service.user.bot.AddService;
import com.tlb.backend.service.user.bot.RemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin
@RestController
public class RemoveBotController {
    @Autowired
    private RemoveService removeService;

    @PostMapping({"/user/bot/remove", "/user/bot/remove/"})
    public Map<String,String> removeBot(@RequestParam Map<String,String>data){
        System.out.println("bot_remove");
        return removeService.remove(data);
    }


}
