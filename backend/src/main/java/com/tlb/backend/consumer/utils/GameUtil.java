package com.tlb.backend.consumer.utils;

import java.util.Random;

public class GameUtil {
    final private Integer rows;
    final private Integer cols;
    final private Integer inner_walls_count;

    //四个方向，用来辅助泄洪算法
    final private static int[] dx={-1,0,1,0},dy={0,1,0,-1};
    final int [][] walls;

    public GameUtil(Integer rows, Integer cols, Integer inner_walls_count){
        this.rows=rows;
        this.cols=cols;
        this.inner_walls_count=inner_walls_count;
        this.walls=new int[rows][cols];
    }

    public int[][] getWall(){
        return walls;
    }

    private boolean checkConnectivity(int sx,int sy,int tx,int ty){
        if(sx==tx&&sy==ty) return true;
        walls[sx][sy]=1;
        for(int i=0;i<4;i++){
            int x=sx+dx[i],y=sy+dy[i];
            if(x>=0&&x<this.rows&&y>=0&&y<this.cols&&walls[x][y]==0){
                if(checkConnectivity(x,y,tx,ty)){
                    walls[sx][sy]=0;
                    return true;
                }

            }
        }

        walls[sx][sy]=0;
        return false;
    }


    //画地图
    private boolean drawMap(){
        for(int i=0;i<this.rows;i++)
            for(int j=0;j<this.cols;j++)
                this.walls[i][j]=0;

        //给四周加墙
        for(int r=0;r<this.rows;r++){
            walls[r][0]=walls[r][this.cols-1]=1;
        }
        for(int c=0;c<this.cols;c++){
            walls[0][c]=walls[this.rows-1][c]=1;
        }
        Random random= new Random();
        for(int i=0;i<this.inner_walls_count/2;i++){
            //随机生成墙体
            for(int j=0;j<1000;j++){
                int r=random.nextInt(this.rows);
                int c=random.nextInt(this.cols);
                if(walls[r][c]==1||walls[c][r]==1) continue;
                if((r==this.rows-2&&c==1)||(r==1&&c==this.cols-2)) continue;
                walls[r][c]=walls[this.rows-1-r][this.cols-1-c]=1;
                break;
            }
        }
        return this.checkConnectivity(this.rows-2,1,1,this.cols-2);


    }

    public void createMap(){
        for(int i=0;i<1000;i++){
            if(drawMap()) break;
        }
    }



}
