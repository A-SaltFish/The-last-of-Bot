package com.tlb.backend.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tlb.backend.consumer.utils.GameUtil;
import com.tlb.backend.consumer.utils.JwtAuthentication;
import com.tlb.backend.mapper.BotMapper;
import com.tlb.backend.mapper.RecordMapper;
import com.tlb.backend.mapper.UserMapper;
import com.tlb.backend.pojo.Bot;
import com.tlb.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
    private Session session=null;
    private User user;

    public  static RecordMapper recordMapper;

    //static 全局变量，所有WebSocket实例都维护同一份；
    //存有的socket用户
    final public static ConcurrentHashMap<Integer,WebSocketServer> users=new ConcurrentHashMap<>();
    //匹配池

    private static UserMapper userMapper;

    private static BotMapper botMapper;

    public GameUtil game=null;

    public static RestTemplate restTemplate;

    private final static String matchingUrl="http://127.0.0.1:3001";
    private final static String addPlayerUrl=matchingUrl+"/player/add/";
    private final static String removePlayerUrl=matchingUrl+"/player/remove/";

    @Autowired
    public void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper=userMapper;
    }

    @Autowired
    public void setBotMapper(BotMapper botMapper){
        WebSocketServer.botMapper=botMapper;
    }

    @Autowired
    public void setRecordMapper(RecordMapper recordMapper){
        WebSocketServer.recordMapper=recordMapper;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        WebSocketServer.restTemplate=restTemplate;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        this.session=session;
        System.out.println("connected");
        Integer userId= JwtAuthentication.getUserId(token);
        this.user=userMapper.selectById(userId);
        if(this.user!=null){
            users.put(userId,this);
            System.out.println(user.getUsername());
        }else{
            this.session.close();
        }
        // 建立连接
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("close");
        if(this.user!=null){
            this.stopMatching();
            users.remove(this.user.getId());
        }
    }


    public static void startGame(Integer aId,Integer aBotId,Integer bId,Integer bBotId){
        User a=userMapper.selectById(aId);
        User b=userMapper.selectById(bId);
        Bot bot_a=botMapper.selectById(aBotId);
        Bot bot_b=botMapper.selectById(bBotId);
        //把botI赋值进去
        GameUtil game=new GameUtil(16,
                15,
                40,
                a.getId(),
                bot_a,
                b.getId(),
                bot_b);
        game.createMap();
        //将两位玩家的地图赋值
        if(users.get(aId)!=null)
            users.get(aId).game=game;
        if(users.get(bId)!=null)
            users.get(bId).game=game;
        //开启游戏
        game.start();

        //游戏相关的json
        JSONObject resGame=new JSONObject();
        resGame.put("a_id",game.getPlayerA().getId());
        resGame.put("a_sx",game.getPlayerA().getSx());
        resGame.put("a_sy",game.getPlayerA().getSy());
        resGame.put("b_id",game.getPlayerB().getId());
        resGame.put("b_sx",game.getPlayerB().getSx());
        resGame.put("b_sy",game.getPlayerB().getSy());
        resGame.put("map",game.getWall());

        //通知websocket两侧
        JSONObject resA=new JSONObject();
        resA.put("event","start-matching");
        resA.put("opponent_username",b.getUsername());
        resA.put("opponent_photo",b.getPhoto());
        resA.put("opponent_rating",b.getRating());
        resA.put("game",resGame);
        if(users.get(aId)!=null)
            users.get(a.getId()).sendMessage(resA.toJSONString());

        JSONObject resB=new JSONObject();
        resB.put("event","start-matching");
        resB.put("opponent_username",a.getUsername());
        resB.put("opponent_photo",a.getPhoto());
        resB.put("opponent_rating",a.getRating());
        resB.put("game",resGame);
        if(users.get(bId)!=null)
            users.get(b.getId()).sendMessage(resB.toJSONString());
    }

    private void startMatching(Integer botId){
        System.out.println("startMatching");
        //向匹配系统发送请求；
        MultiValueMap<String,String > data=new LinkedMultiValueMap<>();
        data.add("userId",this.user.getId().toString());
        data.add("rating",this.user.getRating().toString());
        //先把botid发过去，再在matching system判断是否处理botid
        data.add("botId",botId.toString());
        //这里的String.class利用了java的反射机制
        restTemplate.postForObject(addPlayerUrl,data,String.class);
    }

    private void stopMatching(){
        System.out.println("stopMatching");
        //向匹配系统发送请求；
        MultiValueMap<String,String > data=new LinkedMultiValueMap<>();
        data.add("userId",this.user.getId().toString());
        restTemplate.postForObject(removePlayerUrl,data,String.class);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        System.out.println("receive:"+message);
        JSONObject data=JSONObject.parseObject(message);
        String event=data.getString("event");
        if("start-matching".equals(event)){
            startMatching(data.getInteger("bot_id"));
        }else if("stop-matching".equals(event)){
            stopMatching();
        }else if("move".equals(event)){
            move(data.getInteger("direction"));
        }

    }

    //设置移动方向
    private void move(int d){
        //如果是蛇A
        if(game.getPlayerA().getId().equals(user.getId())){
            //亲自出马的时候，才接收人的输入
            if(game.getPlayerA().getBotId().equals(-1))
                game.setNextStepA(d);
        }else if(game.getPlayerB().getId().equals(user.getId())){
            if(game.getPlayerB().getBotId().equals(-1))
                game.setNextStepB(d);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
        if(this.user!=null){
            //根据ID在Hashmap里面删除对应user
            users.remove(this.user.getId());
        }
    }

    public void sendMessage(String msg){
        synchronized (this.session){
            try{
                //从后端向当前链接发送信息
                this.session.getBasicRemote().sendText(msg);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}

