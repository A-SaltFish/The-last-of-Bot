package com.tlb.botrunningsystem.utils;

import java.util.ArrayList;
import java.util.List;

public class CompileBot implements com.tlb.botrunningsystem.utils.CompileBotInterface {
    static class Cell{
        public int x,y;
        public Cell(int x,int y){
            this.x=x;
            this.y=y;
        }
    }


    //检查蛇什么时候会变长
    private boolean check_tail_increasing(int step){
        if(step<=10) return true;
        return step%3==1;
    }

    public List<Cell> getCells(int sx,int sy,String steps){
        List<Cell> res=new ArrayList<>();
        int[] dx={-1,0,1,0},dy={0,1,0,-1};
        int x=sx,y=sy;
        int step=0;
        res.add(new Cell(x,y));
        for(int i=1;i<steps.length()-1;i++){
            int d=steps.charAt(i)-'0';
            x+=dx[d];
            y+=dy[d];
            res.add(new Cell(x,y));
            //没有增长，说明之前的蛇尾就要移动，也就移除了
            if(!check_tail_increasing(++step)){
                res.remove(0);
            }
        }
        return res;
    }

    @Override
    public Integer nextMove(String input) {
        String [] strs=input.split("#");
        int [][]g=new int[16][15];
        for(int i=0,k=0;i<16;i++){
            for(int j=0;j<15;j++,k++){
                if(strs[0].charAt(k)=='1'){
                    g[i][j]=1;
                }
            }
        }
        int aSx=Integer.parseInt(strs[1]),aSy=Integer.parseInt(strs[2]);
        int bSx=Integer.parseInt(strs[4]),bSy=Integer.parseInt(strs[5]);
        System.out.println(strs.length);

        List<Cell> aCells=getCells(aSx,aSy,strs[3]);
        List<Cell> bCells=getCells(bSx,bSy,strs[6]);
        for(Cell c:aCells) g[c.x][c.y]=1;
        for(Cell c:bCells) g[c.x][c.y]=1;

        int [] dx={-1,0,1,0},dy={0,1,0,-1};
        for(int i=0;i<4;i++){
            int x=aCells.get(aCells.size()-1).x+dx[i];
            int y=aCells.get(aCells.size()-1).y+dy[i];
            if(x>=0&&x<16&&y>=0&&y<15&&g[x][y]==0){
                return i;
            }
        }

        return 0;
    }
}
