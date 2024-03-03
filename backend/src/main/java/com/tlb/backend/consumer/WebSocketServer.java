package com.tlb.backend.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tlb.backend.consumer.utils.GameUtil;
import com.tlb.backend.consumer.utils.JwtAuthentication;
import com.tlb.backend.mapper.RecordMapper;
import com.tlb.backend.mapper.UserMapper;
import com.tlb.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
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
    final private static CopyOnWriteArraySet<User> matchpool=new CopyOnWriteArraySet<>();

    private static UserMapper userMapper;

    private GameUtil game=null;

    @Autowired
    public void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper=userMapper;
    }

    @Autowired
    public void setRecordMapper(RecordMapper recordMapper){
        WebSocketServer.recordMapper=recordMapper;
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
            users.remove(this.user.getId());
            matchpool.remove(this.user);
        }
    }

    private void startMatching(){
        System.out.println("startMatching");
        matchpool.add(this.user);
        while(matchpool.size()>=2){
            Iterator<User> it=matchpool.iterator();
            User a=it.next(),b=it.next();
            matchpool.remove(a);
            matchpool.remove(b);

            game=new GameUtil(16,15,40,a.getId(),b.getId());
            game.createMap();
            //开启游戏
            game.start();
            users.get(a.getId()).game=game;
            users.get(b.getId()).game=game;


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
            users.get(a.getId()).sendMessage(resA.toJSONString());

            JSONObject resB=new JSONObject();
            resB.put("event","start-matching");
            resB.put("opponent_username",a.getUsername());
            resB.put("opponent_photo",a.getPhoto());
            resB.put("opponent_rating",a.getRating());
            resB.put("game",resGame);
            users.get(b.getId()).sendMessage(resB.toJSONString());
        }
    }

    private void stopMatching(){
        System.out.println("stopMatching");
        matchpool.remove(this.user);
        //临时调试
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        System.out.println("receive:"+message);
        JSONObject data=JSONObject.parseObject(message);
        String event=data.getString("event");
        if("start-matching".equals(event)){
            startMatching();
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
            game.setNextStepA(d);
        }else if(game.getPlayerB().getId().equals(user.getId())){
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

