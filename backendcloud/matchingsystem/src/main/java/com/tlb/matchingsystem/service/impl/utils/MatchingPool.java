package com.tlb.matchingsystem.service.impl.utils;

import com.tlb.matchingsystem.config.RestTemplateConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class MatchingPool extends Thread{
    private static List<Player> players=new ArrayList<>();
    private ReentrantLock lock=new ReentrantLock();

    private final static String backendUrl="http://127.0.0.1:3000";

    private final static String startGameUrl=backendUrl+"/pk/start/game/";

    private static RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        MatchingPool.restTemplate=restTemplate;
    }

    public void addPlayer(Integer userId,Integer rating){
        lock.lock();
        try{
            players.add(new Player(userId,rating,0));
        }finally {
            lock.unlock();
        }
    }
    public void removePlayer(Integer userId){
        lock.lock();
        try{
            List<Player> newPlayers=new ArrayList<>();
            for(Player player:players){
                if(!player.getUserId().equals(userId)){
                    newPlayers.add(player);
                }
            }
            players=newPlayers;
        }finally {
            lock.unlock();
        }
    }

    //增加当前等待玩家的等待时间
    private void increaseWaitingTime(){
        for(Player player:players){
            player.setWaitingTime(player.getWaitingTime()+1);
        }
    }

    private boolean checkMatched(Player player1,Player player2){
        int ratingDelta=Math.abs(player1.getRating()-player2.getRating());
        int waitingTime=Math.min(player1.getWaitingTime(),player2.getWaitingTime());
        return ratingDelta<=waitingTime*10;


    }

    //尝试匹配玩家
    private void matchPlayers(){
        System.out.println("match players"+players.toString());
        boolean[] used=new boolean[players.size()];
        for(int i=0;i<players.size();i++){
            if(used[i]) continue;
            for(int j=i+1;j<players.size();j++){
                if(used[j]) continue;
                Player a=players.get(i),b=players.get(j);
                if(checkMatched(a,b)){
                    used[i]=used[j]=true;
                    sendResult(a,b);
                    break;
                }
            }
        }

        List<Player> newPlayers=new ArrayList<>();
        for(int i=0;i<players.size();i++){
            if(!used[i]){
                newPlayers.add(players.get(i));
            }
        }
        players=newPlayers;
    }

    //返回a、b的匹配结果
    private void sendResult(Player a,Player b){
        System.out.println("返回匹配结果a: "+a.getUserId()+" b:"+b.getUserId());
        MultiValueMap<String,String> data=new LinkedMultiValueMap<>();
        data.add("aId",a.getUserId().toString());
        data.add("bId",b.getUserId().toString());
        restTemplate.postForObject(startGameUrl,data,String.class);
    }

    @Override
    public void run() {
        //super.run();
        while(true){
            try{
                Thread.sleep(1000);
                lock.lock();
                try{
                    increaseWaitingTime();
                    matchPlayers();
                }finally {
                    lock.unlock();
                }

            }catch (InterruptedException e){
                e.printStackTrace();
                break;
            }
        }
    }
}
