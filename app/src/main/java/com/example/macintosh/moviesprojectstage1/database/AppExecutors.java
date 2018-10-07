package com.example.macintosh.moviesprojectstage1.database;

import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import android.os.*;
public class AppExecutors {

    private static AppExecutors sInstance;
    private Executor diskIO;
    private Executor networkIO;
    private Executor mainThread;
    private static final Object LOCK = new Object();

    private AppExecutors(Executor diskIO, Executor netoworkIO,Executor mainThread){
        this.diskIO = diskIO;
        this.networkIO = netoworkIO;
        this.mainThread = mainThread;
    }

    public static AppExecutors getInstance(){
        if(sInstance == null){
            //create instance
            synchronized (LOCK){
                sInstance = new AppExecutors(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3), new MainThreadExecutor());
            }
        }

        return sInstance;
    }

    public Executor getDiskIO() {
        return diskIO;
    }

    public Executor getNetworkIO() {
        return networkIO;
    }

    public Executor getMainThread() {
        return mainThread;
    }

    private static class  MainThreadExecutor implements Executor{

        private Handler  mainThreadHandler =  new Handler(Looper.getMainLooper());
        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

}
