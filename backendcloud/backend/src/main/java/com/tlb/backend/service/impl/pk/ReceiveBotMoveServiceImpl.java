package com.tlb.backend.service.impl.pk;

import com.tlb.backend.consumer.WebSocketServer;
import com.tlb.backend.consumer.utils.GameUtil;
import com.tlb.backend.service.pk.ReceiveBotMoveService;
import org.springframework.stereotype.Service;

@Service
public class ReceiveBotMoveServiceImpl implements ReceiveBotMoveService {
    @Override
    public String receiveBotMove(Integer userId, Integer direction) {
        System.out.println("receive Bot "+userId+" Move:"+direction);
        //如果用户存在
        if(WebSocketServer.users.get(userId)!=null){
            GameUtil game=WebSocketServer.users.get(userId).game;
            if(game.getPlayerA().getId().equals(userId))
                    game.setNextStepA(direction);
            else if(game.getPlayerB().getId().equals(userId))
                    game.setNextStepB(direction);
        }
        return "receive move success!";
    }
}
