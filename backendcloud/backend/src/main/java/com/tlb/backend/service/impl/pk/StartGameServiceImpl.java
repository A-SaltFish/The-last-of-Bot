package com.tlb.backend.service.impl.pk;

import com.tlb.backend.consumer.WebSocketServer;
import com.tlb.backend.service.pk.StartGameService;
import org.springframework.stereotype.Service;

@Service
public class StartGameServiceImpl implements StartGameService {
    @Override
    public String startGame(Integer aId, Integer bId) {
        System.out.println("StartGameServiceImpl"+aId+" "+bId);
        WebSocketServer.startGame(aId,bId);
        return "start game success!";
    }

}
