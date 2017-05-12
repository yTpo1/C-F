package com.example.nickghost.strategy1;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread{
    private int FPS = 30;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    public static Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamepanel){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamepanel;

    }
    @Override
    public void run(){
        long startTime;
        long timeMills;
        long waitTime;
        long totalTime = 0;
        long frameCount = 0;
        long targetTime = 1000/FPS;

        while(running) {
            startTime = System.nanoTime();
            canvas = null;

            //try locking the canvas for pixel editing
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }

            }catch (Exception e){}

            //how many seconds it took to update and draw game once
            timeMills = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime-timeMills;

            try{
                this.sleep(waitTime);
            }catch (Exception e){}

            finally {
                if(canvas!=null){
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);

                    }catch (Exception e){e.printStackTrace();}
                }
            }

            totalTime += System.nanoTime()-startTime;
            frameCount++;
            //if happens 30 times
            if(frameCount == FPS){
                averageFPS = 1000/(totalTime/frameCount/1000000);
                frameCount = 0;
                totalTime = 0;
                //System.out.println(averageFPS);
            }
        }
    }
    public void setRunning(boolean b){
        running=b;
    }
}