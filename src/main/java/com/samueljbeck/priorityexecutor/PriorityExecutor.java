package com.samueljbeck.priorityexecutor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
 * Created by samuelbeck on 3/2/16.
 */

public class PriorityExecutor extends ThreadPoolExecutor {

    private int queSeq = 1;

    private PriorityExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }
    //Utitlity method to create thread pool easily

    public static ExecutorService newFixedThreadPool(int nThreads) {
        return new PriorityExecutor(7, nThreads, 0L, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<Runnable>());
    }

    public static ExecutorService newFixedThreadPool(int nThreads, int maxThreads) {
        return new PriorityExecutor(maxThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<Runnable>());
    }
    //Submit with New comparable task

    @SuppressWarnings("unchecked")
    private Future<?> submit(Runnable task, int priority) {
        return super.submit(new ComparableFutureTask(task, null, priority));
    }

    public void run(Runnable task) {
        submit(task, 5);
    }
    public void run(Runnable task, int priority) {
        submit(task, priority);
    }

    //execute with New comparable task
    @SuppressWarnings("unchecked")
    public void execute(Runnable command, int priority) {
        super.execute(new ComparableFutureTask(command, null, priority));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return (RunnableFuture<T>) callable;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return (RunnableFuture<T>) runnable;
    }

    class ComparableFutureTask<T> extends FutureTask<T> implements Comparable<ComparableFutureTask<T>> {
        int seq;
        volatile int priority = 0;

        ComparableFutureTask(Runnable runnable, T result, int priority) {
            super(runnable, result);
            if (queSeq + 5 >= Integer.MAX_VALUE) {
                queSeq = 1;
            } else {
                queSeq++;
            }
            seq = queSeq;

            this.priority = priority;

        }

        public ComparableFutureTask(Callable<T> callable, int priority) {
            super(callable);
            if (queSeq + 5 >= Integer.MAX_VALUE) {
                queSeq = 1;
            } else {
                queSeq++;
            }
            seq = queSeq;
            this.priority = priority;
        }

        @Override
        public int compareTo(ComparableFutureTask<T> o) {
            int c;
            c = Integer.valueOf(priority).compareTo(o.priority);
            if (c == 0) {
                c = Integer.valueOf(seq).compareTo(o.seq);
            }
            return c;
        }

    }


}