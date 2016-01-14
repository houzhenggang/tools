package com.xl.tool.current;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FaultTolerTask implements Runnable{
    private static final Logger log= LoggerFactory.getLogger(FaultTolerTask.class);
    private Runnable task;
    public FaultTolerTask(Runnable task){
        this.task=task;
    }
    @Override
    public void run() {
        try{
            task.run();
        }catch (Throwable e){
            e.printStackTrace();
            log.error("Task execute error ",e);
        }
    }
}