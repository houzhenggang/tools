package com.xl.tool.current;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Caedmon on 2015/11/20.
 */
public class TaskExecutor implements Runnable{
    private Thread t;
    private LinkedBlockingQueue<SyncTask> queue=new LinkedBlockingQueue();
    private static final Logger log= LoggerFactory.getLogger(TaskExecutor.class);
    public TaskExecutor(String threadName){
        this.t=new Thread(this);
        this.t.setName(threadName);
    }

    public <T> T submit(SyncTask<T> task) throws Throwable{
        if(Thread.currentThread()==t){
            task.execute();
        }else{
            queue.put(task);
        }
        return task.waitResult();

    }
    public void start(){
        this.t.start();
    }

    @Override
    public void run() {
        SyncTask task=null;
        for(;;) {
            try {
                task = queue.take();
                task.execute();

            } catch (Exception e) {
                e.printStackTrace();
                log.error("TaskExecutor run task error ", e);
            }
        }
    }
}
