package com.tlb.backend.consumer.utils;

import com.alibaba.fastjson2.JSONObject;
import com.tlb.backend.consumer.WebSocketServer;
import com.tlb.backend.pojo.Bot;
import com.tlb.backend.pojo.Record;
import com.tlb.backend.pojo.User;
import org.apache.logging.log4j.util.StringBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class GameUtil extends Thread {
    private final Integer rows;
    private final Integer cols;
    private final Integer inner_walls_count;

    //四个方向，用来辅助泄洪算法
    private final static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
    private final int[][] walls;
    private final PlayerUtil player1, player2;

    private Integer nextStepA = null;
    private Integer nextStepB = null;
    private String status = "playing";    //playing->finished
    private String loser = ""; //all平局，a输，b输
    private int gameSteps=0;
    private Bot botA;
    private Bot botB;
    private final String botRunningUrl="http://127.0.0.1:3002";
    private final String addBotUrl=botRunningUrl+"/bot/add/";

    private ReentrantLock lock = new ReentrantLock();

    public GameUtil(Integer rows, Integer cols, Integer inner_walls_count, Integer idA, Bot botA, Integer idB,Bot botB) {
        Integer botIdA=-1;
        Integer botIdB=-1;
        String botCodeA="";
        String botCodeB="";
        if(botA!=null){
            botIdA=botA.getId();
            botCodeA=botA.getContent();
        }
        if(botB!=null){
            botIdB=botB.getId();
            botCodeB=botB.getContent();
        }
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.walls = new int[rows][cols];
        player1 = new PlayerUtil(idA,botIdA,botCodeA, rows - 2, 1, new ArrayList<>());
        player2 = new PlayerUtil(idB,botIdB,botCodeB,  1, cols - 2, new ArrayList<>());
    }


    public PlayerUtil getPlayerA() {
        return this.player1;
    }

    public PlayerUtil getPlayerB() {
        return this.player2;
    }

    public int[][] getWall() {
        return walls;
    }

    private boolean checkConnectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) return true;
        walls[sx][sy] = 1;
        for (int i = 0; i < 4; i++) {
            int x = sx + dx[i], y = sy + dy[i];
            if (x >= 0 && x < this.rows && y >= 0 && y < this.cols && walls[x][y] == 0) {
                if (checkConnectivity(x, y, tx, ty)) {
                    walls[sx][sy] = 0;
                    return true;
                }

            }
        }

        walls[sx][sy] = 0;
        return false;
    }


    //画地图
    private boolean drawMap() {
        for (int i = 0; i < this.rows; i++)
            for (int j = 0; j < this.cols; j++)
                this.walls[i][j] = 0;

        //给四周加墙
        for (int r = 0; r < this.rows; r++) {
            walls[r][0] = walls[r][this.cols - 1] = 1;
        }
        for (int c = 0; c < this.cols; c++) {
            walls[0][c] = walls[this.rows - 1][c] = 1;
        }
        Random random = new Random();
        for (int i = 0; i < this.inner_walls_count / 2; i++) {
            //随机生成墙体
            for (int j = 0; j < 1000; j++) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);
                if (walls[r][c] == 1 || walls[c][r] == 1) continue;
                if ((r == this.rows - 2 && c == 1) || (r == 1 && c == this.cols - 2)) continue;
                walls[r][c] = walls[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }
        return this.checkConnectivity(this.rows - 2, 1, 1, this.cols - 2);


    }

    public void createMap() {
        for (int i = 0; i < 1000; i++) {
            if (drawMap()) break;
        }
    }

    //获取当前对局状态
    private String getInput(PlayerUtil player){
        //地图信息，自身蛇的坐标和操作，对方蛇的坐标和操作
        PlayerUtil me,you;
        if(player1.getId().equals(player.getId())){
            me=player1;
            you=player2;
        }else{
            me=player2;
            you=player1;
        }
        String input=getMapString()+"#"+me.getSx()+"#"+me.getSy()+"#("+me.getStepsString()+")#"
                +you.getSx()+"#"+you.getSy()+"#("+you.getStepsString()+")";
        System.out.println("input:"+input);
        return input;
    }

    private void sendBotCode(PlayerUtil player){
        if(player.getBotId().equals(-1)) return;
        //是机器就需要执行代码
        MultiValueMap<String,String> data=new LinkedMultiValueMap<>();
        data.add("user_id",player.getId().toString());
        data.add("bot_code",player.getBotCode());
        data.add("input",getInput(player));
        WebSocketServer.restTemplate.postForObject(addBotUrl,data,String.class);
    }

    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }

    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }
    }


    //等待两名玩家的下一步操作
    private boolean nextStep() {
        //因为蛇一秒可以走五个格子，代表着一个格子差不多要走200ms。所以我们要等蛇走完这一步以后，再获取新的输入
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //根据player1和player2是否是机器人，判断怎么进行nextStep
        sendBotCode(player1);
        sendBotCode(player2);
        //等待5s获取输入
        for (int i = 0; i < 60; i++) {
            try {
                Thread.sleep(150);
                lock.lock();
                try {
                    if (nextStepA != null && nextStepB != null) {
                        player1.getSteps().add(nextStepA);
                        player2.getSteps().add(nextStepB);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //向两名玩家广播结果
    private void sendAllMsg(String msg){
        System.out.println(msg);
        if(WebSocketServer.users.get(player1.getId())!=null)
            WebSocketServer.users.get(player1.getId()).sendMessage(msg);
        if(WebSocketServer.users.get(player2.getId())!=null)
            WebSocketServer.users.get(player2.getId()).sendMessage(msg);
    }

    //传递移动信息
    private void sendMove(){
        lock.lock();
        try{
            JSONObject res=new JSONObject();
            res.put("event","move");
            res.put("a_direction",nextStepA);
            res.put("b_direction",nextStepB);
            sendAllMsg(res.toJSONString());
            nextStepB=nextStepA=null;
        }finally {
            lock.unlock();
        }

    }

    private void sendResult(){
        JSONObject res=new JSONObject();
        res.put("event","result");
        res.put("loser",loser);
        saveToDataBase();
        sendAllMsg(res.toJSONString());
    }

    //判断两名玩家下一步是否合法
    private void judge(){
        List<Cell> cellsA=player1.getCells();
        List<Cell> cellsB=player2.getCells();
        //判断墙是否重复
        boolean validA=check_valid(cellsA,cellsB);
        boolean validB=check_valid(cellsB,cellsA);
        if(!validA||!validB){
            status="finished";
            if(!validA&&!validB){
                loser="all";
            }else if(!validA){
                loser="a";
            }else if(!validB){
                loser="b";
            }
        }
    }

    private boolean check_valid(List<Cell> cellsA,List<Cell> cellsB){
        int n=cellsA.size();
        Cell cell=cellsA.get(n-1);
        if(walls[cell.cx][cell.cy]==1) return false;
        //判断自身重叠
        for(int i=0;i<n-1;i++){
            if(cellsA.get(i).cx==cell.cx&&cellsA.get(i).cy==cell.cy){
                return false;
            }
        }
        for(int i=0;i<n-1;i++){
            if(cellsB.get(i).cx==cell.cx&&cellsB.get(i).cy==cell.cy){
                return false;
            }
        }
        return true;

    }

    private String getMapString(){
        StringBuilder res= new StringBuilder();
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                res.append(walls[i][j]);
            }
        }
        return res.toString();
    }

    private void updateUserRating(PlayerUtil player,Integer rating){
        User user=WebSocketServer.userMapper.selectById(player.getId());
        user.setRating(rating);
        WebSocketServer.userMapper.updateById(user);
    }


    private void saveToDataBase(){
        Integer ratingA=WebSocketServer.userMapper.selectById(player1.getId()).getRating();
        Integer ratingB=WebSocketServer.userMapper.selectById(player2.getId()).getRating();

        if("a".equals(loser)){
            if(gameSteps>=10){
                ratingA-=2;
                ratingB+=10;
            } else{
                ratingA-=3;
                ratingB+=5;
            }
        }else if("b".equals(loser)){
            if(gameSteps>=10){
                ratingB-=2;
                ratingA+=10;
            } else{
                ratingB-=3;
                ratingA+=5;
            }
        }else{
            ratingA-=1;
            ratingB-=1;
        }
        System.out.println("存储");

        updateUserRating(player1,ratingA);
        updateUserRating(player2,ratingB);

        Record record=new Record(
                null,
                player1.getId(),
                player1.getSx(),
                player1.getSy(),
                player1.getStepsString(),
                player2.getId(),
                player2.getSx(),
                player2.getSy(),
                player2.getStepsString(),
                getMapString(),
                loser,
                new Date()
        );
        WebSocketServer.recordMapper.insert(record);

    }



    @Override
    public void run() {
        super.run();
        //最多结束步数，不代表是1000，只是用1000来循环
        for (int i = 0; i < 1000; i++) {
            if (nextStep()) {
                gameSteps++;
                judge();
                if(status.equals("playing")){
                    sendMove();
                }else{
                    sendMove();
                    sendResult();
                    break;
                }

            } else {
                status = "finished";
                lock.lock();
                try{
                    if (nextStepA == null && nextStepB == null) {
                        loser = "all";
                    } else if (nextStepA == null) {
                        loser = "a";
                    } else if (nextStepB == null) {
                        loser = "b";
                    }
                }finally {
                    lock.unlock();
                }
                sendResult();
                break;
            }
        }
    }


}
