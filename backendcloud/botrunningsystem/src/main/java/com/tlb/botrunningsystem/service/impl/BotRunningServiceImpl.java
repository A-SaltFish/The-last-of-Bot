package com.tlb.botrunningsystem.service.impl;

import com.tlb.botrunningsystem.service.BotRunningService;
import com.tlb.botrunningsystem.service.impl.utils.BotPool;
import org.springframework.stereotype.Service;

@Service
public class BotRunningServiceImpl implements BotRunningService {
    public final static BotPool botPool=new BotPool();

    @Override
    public String addBot(Integer userId, String botCode, String input) {
        //System.out.println("addbot: "+userId+" "+botCode+" "+input);
        botPool.addBotTask(userId,botCode,input);
        return "add success!";
    }
}
