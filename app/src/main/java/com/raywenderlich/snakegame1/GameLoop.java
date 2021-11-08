package com.raywenderlich.snakegame1;

import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.atomic.AtomicBoolean;

public class GameLoop implements Runnable{
    private Thread worker = null ;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final MutableLiveData<Boolean> liveData;

    public GameLoop(MutableLiveData<Boolean> liveData) {
        this.liveData = liveData;
    }

    @Override
    public void run() {
        running.set(true);
        while (running.get()){
            System.out.println("THREAD" + Thread.currentThread().getName());
            liveData.postValue(true);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

    public void stop(){
        running.set(false);
        worker.interrupt();
        worker = null;
    }

    public void start(){
        worker = new Thread(this);
        worker.start();
    }

}
