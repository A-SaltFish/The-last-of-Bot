package com.tlb.botrunningsystem.service.impl.utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

//消费者线程，处理Bot请求
public class BotPool extends Thread{

    private static ReentrantLock lock=new ReentrantLock();
    //条件变量
    private Condition condition=lock.newCondition();
    private Queue<Bot> botQueue= new LinkedList<>();


    //添加Bot任务
    public void addBotTask(Integer userId, String botCode, String input){
        lock.lock();
        try{
            botQueue.add(new Bot(userId,botCode,input));
            //唤醒任一一个被阻塞的线程，但是这边condition只阻塞了一个线程，所以无所谓
            condition.signalAll();
        }finally {
            lock.unlock();
        }
    }

    //简单的方式，只能用java代码来进行编译，通过JOOR进行。同时为了时间可控我们把这部分代码放入新的线程中
    private void consumerBot(Bot bot){
        BotTaskConsumer consumer=new BotTaskConsumer();
        consumer.startTimeout(2000,bot);
    }

    @Override
    public void run() {
        super.run();
        while(true){
            lock.lock();
            if(botQueue.isEmpty()){
                try{
                    condition.await();
                }catch (InterruptedException e){
                    e.printStackTrace();
                    lock.unlock();
                    break;
                }
            }else{
                Bot bot=botQueue.remove();
                lock.unlock();
                //耗时，所以加到unlock后面；同时锁是因为队列的读写冲突导致的，所以Bot已经取出来了，这里解锁不影响
                consumerBot(bot);
            }
        }
    }
}
