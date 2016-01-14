package com.xl.tool.current;

import java.util.concurrent.*;

/**
 * Created by Caedmon on 2015/11/20.
 */
public abstract class SyncTask<V> implements Callable<V>{
    private long timeout;
    private TimeUnit unit;
    private FutureTask<V> future;
    public SyncTask(long timeout,TimeUnit unit){
        this.timeout=timeout;
        this.unit=unit;
        this.future=new FutureTask<V>(this);
    }
    public V waitResult() throws Throwable{
        try{
            return future.get(timeout,unit);
        }catch (ExecutionException e){
            throw e.getCause();
        }

    }
    public void execute() throws Exception{
        future.run();
    }

}
