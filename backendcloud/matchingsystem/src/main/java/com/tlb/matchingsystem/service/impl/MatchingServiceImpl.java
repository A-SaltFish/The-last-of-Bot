package com.tlb.matchingsystem.service.impl;

import com.tlb.matchingsystem.service.MatchingService;
import com.tlb.matchingsystem.service.impl.utils.MatchingPool;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@Service
@CrossOrigin
public class MatchingServiceImpl  implements MatchingService {
    public final static MatchingPool matchingPool=new MatchingPool();

    @Override
    public String addPlayer(Integer userId, Integer rating) {
        System.out.println("add player"+userId+" "+rating);
        matchingPool.addPlayer(userId,rating);
        return "add success!";
    }

    @Override
    public String removePlayer(Integer userId) {
        System.out.println("remove "+userId);
        matchingPool.removePlayer(userId);
        return "remove success!";
    }
}
