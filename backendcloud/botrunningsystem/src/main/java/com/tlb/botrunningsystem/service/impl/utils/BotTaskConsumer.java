package com.tlb.botrunningsystem.service.impl.utils;

import com.tlb.botrunningsystem.utils.CompileBotInterface;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class BotTaskConsumer extends Thread {
    private Bot bot;
    private static RestTemplate restTemplate;

    private final static String receiveBotMoveUrl="http://127.0.0.1:3000//pk/receive/bot/move/";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        BotTaskConsumer.restTemplate=restTemplate;
    }

    public void startTimeout(long timeout,Bot bot){
        this.bot=bot;
        this.start();
        try {
            this.join(timeout); //最多等待timeout秒
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            //不管是否结束，打断线程；
            this.interrupt();
        }
    }

    private String addUid(String code,String uid){
        //在code中的Bot类名后添加uid
        int k=code.indexOf(" implements com.tlb.botrunningsystem.utils.CompileBotInterface");
        return code.substring(0,k)+uid+code.substring(k);

    }

    @Override
    public void run(){
        //动态编译代码，
        UUID uuid=UUID.randomUUID();
        String uid=uuid.toString().substring(0,8);

        CompileBotInterface botInterface= Reflect.compile(
            "com.tlb.botrunningsystem.utils.CompileBot"+uid,
                addUid(bot.getBotCode(),uid)
        ).create().get();
        Integer direction=botInterface.nextMove(bot.getPreInput());
        System.out.println(bot.getUserId()+" move : "+direction);

        //结果返回
        MultiValueMap<String,String> data=new LinkedMultiValueMap<>();
        data.add("user_id",bot.getUserId().toString());
        data.add("direction",direction.toString());
        restTemplate.postForObject(receiveBotMoveUrl,data,String.class);

    }
}
