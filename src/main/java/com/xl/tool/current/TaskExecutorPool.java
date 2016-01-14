package com.xl.tool.current;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/20.
 */
public class TaskExecutorPool {
    private List<TaskExecutor> executors=new ArrayList<>();
    private int size;
    public TaskExecutorPool(int size,String prefix){
        this.size=size;
        for(int i=0;i<size;i++){
            TaskExecutor executor=new TaskExecutor(prefix+i);
            executors.add(executor);
            executor.start();
        }
    }
    public <T> T submit(Object key,SyncTask<T> task) throws Throwable{
        long hash=key.hashCode();
        int index= (int) (Math.abs(hash)%this.size);
        TaskExecutor executor=executors.get(index);
        return executor.submit(task);
    }
}
