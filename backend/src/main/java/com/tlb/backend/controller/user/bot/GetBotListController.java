package com.tlb.backend.controller.user.bot;

import com.tlb.backend.pojo.Bot;
import com.tlb.backend.service.user.bot.GetListService;
import com.tlb.backend.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
public class GetBotListController {
    @Autowired
    private GetListService getListService;

    @GetMapping({"/user/bot/getlist", "/user/bot/getlist/"})
    public List<Bot> getList(){
        System.out.println("bot_getList");
        return getListService.getList();
    }


}
