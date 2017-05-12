package com.example.nickghost.strategy1;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class BackgroundStartMenu {
    private Bitmap spritesheet;
    //private int x, y, dx;
    private Animation animation = new Animation();
    private long startTime;

    public BackgroundStartMenu(Bitmap res, int numFrames){
        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for(int i=0;i<image.length;i++){
            image[i] = Bitmap.createBitmap(spritesheet, i*1200,0, 1200,700);//540
        }

        animation.setFrames(image);
        animation.setDelay(100);
        startTime = System.nanoTime();
    }

    public void update(){
        /*
        long elapsed = (System.nanoTime()-startTime)/1000000;
        if(elapsed>100){//every 100 miliseconds score goes up(1/10 of a sec score +1
            score++;
            startTime = System.nanoTime();
        }
         */
        animation.update();
    }
    public void draw(Canvas canvas){
        Paint paint = new Paint();
        canvas.drawBitmap(animation.getImage(),0,0,null);

        paint.setColor(Color.LTGRAY);
        canvas.drawRect(450,200,680,350,paint);

        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(450,200,680,350,paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(65);
        canvas.drawText("Play",480,290, paint);
    }

}
