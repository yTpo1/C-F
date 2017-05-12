package com.example.nickghost.strategy1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


public class AreaSqr extends GameObject {

    private boolean turnsToWaitSet;
    private int turnWhenFinished;
    private int turnsLeftforFarm;

    private Bitmap castleBlue;
    private Bitmap castleRed;
    private Bitmap castleGreen;
    private Bitmap flagBlue;
    private Bitmap flagRed;
    private Bitmap flagGreen;
    private Bitmap farmBlue;
    private Bitmap farmRed;
    private Bitmap farmGreen;


    public AreaSqr(int x, int y, Bitmap imgCast1, Bitmap imgCast2, Bitmap imgCast3,
                   Bitmap imgFlag1, Bitmap imgFlag2, Bitmap imgFlag3,
                   Bitmap imgFarm1, Bitmap imgFarm2, Bitmap imgFarm3){
        this.x = x;
        this.y = y;
        isSelected = false;
        this.width = x+100;
        this.height = y+100;

        nothingBuilt = true;
        farm = false;
        farmlvl = 0;
        castle = false;
        soilders = 1;//0

        ownerPlayer = OwnerPlayer.none;
        hasOwner = false;
        isSelected1st = false;
        isSelected2nd = false;
        troopsChosen = 0;
        SecondTerritoryDraw = false;
        ownerPlayerNum=0;
        farmInConstruction=false;
        turnsToWaitFarm=0;

        turnsToWaitSet = false;
        currentTurn = -1;
        turnWhenFinished = 0;
        turnsLeftforFarm = 0;

        castleBlue = imgCast1;
        castleRed = imgCast2;
        castleGreen = imgCast3;
        flagBlue = imgFlag1;
        flagRed = imgFlag2;
        flagGreen = imgFlag3;
        farmBlue = imgFarm1;
        farmRed = imgFarm2;
        farmGreen = imgFarm3;

        ownerChangedStopConstruction = false;


        farmlevel1Finished = false;
        farmlevel2Finished = false;
        farmlevel3Finished = false;
        farmlevel4Finished = false;
        farmlevel5Finished = false;
        //Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.image);
        dontAddFood=false;
    }

    public void update(){

        if(ownerPlayer==OwnerPlayer.player1 || ownerPlayer==OwnerPlayer.player2 || ownerPlayer==OwnerPlayer.player3){
            hasOwner = true;
        }

        switch (ownerPlayer){
            case none:ownerPlayerNum=0;break;
            case player1:ownerPlayerNum=1;break;
            case player2:ownerPlayerNum=2;break;
            case player3:ownerPlayerNum=3;break;
            default:break;
        }

        if(ownerChangedStopConstruction){
            farmInConstruction = false;
            ownerChangedStopConstruction = false;
        }

        if(farmInConstruction && !turnsToWaitSet){
            turnsToWaitSet=true;

            switch (farmlvl){
                case 0: turnsToWaitFarm = 1;break;
                case 1: turnsToWaitFarm = 2;break;
                case 2: turnsToWaitFarm = 3;break;
                case 3: turnsToWaitFarm = 4;break;
                case 4: turnsToWaitFarm = 5;break;
                case 5: turnsToWaitFarm = 6;break;
                default:break;
            }
            System.out.println("turnsToWaitFarm = "+turnsToWaitFarm);
            System.out.println("currentTurn = "+currentTurn);

            turnWhenFinished = currentTurn + turnsToWaitFarm;
            turnsLeftforFarm = turnWhenFinished - currentTurn;

            System.out.println("turnWhenFinished = "+turnWhenFinished);
        }

        if(farmInConstruction && turnsToWaitSet) {
            turnsLeftforFarm = turnWhenFinished - currentTurn;
        }

        if(turnsLeftforFarm==1){
            dontAddFood=true;
        }

        if(turnWhenFinished==currentTurn && farmInConstruction){
            farmInConstruction=false;
            turnsToWaitSet=false;
            farmlvl++;
            switch (farmlvl){
                case 1:{
                    farmlevel1Finished=true;
                    break;
                }
                case 2:{
                    farmlevel2Finished=true;
                    break;
                }
                case 3:{
                    farmlevel3Finished=true;
                    break;
                }
                case 4:{
                    farmlevel4Finished=true;
                    break;
                }
                case 5:{
                    farmlevel5Finished=true;
                    break;
                }
            }
        }

    }

    public void draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setTextSize(20);
        paint.setColor(Color.BLACK);
        //canvas.drawRect(x,y,width,height,paint);
        //paint.setStyle(Paint.Style.STROKE);

        //when using try catch FPS is alot higher when rendering without it
        try{
            //if farm is built color in
            if(farm){
                //paint.setColor(Color.YELLOW);
                //paint.setStyle(Paint.Style.FILL);
                //canvas.drawRect(x,y,x+100,y+100,paint);
                if(getOwnerPlayerNum()==1) {
                    canvas.drawBitmap(farmBlue, x+20, y+2, null);
                }
                if(getOwnerPlayerNum()==2) {
                    canvas.drawBitmap(farmRed, x+20, y+2, null);
                }
                if(getOwnerPlayerNum()==3) {
                    canvas.drawBitmap(farmGreen, x+20, y+2, null);
                }
            }
            //if castle is built color in
            if(castle){
                //paint.setStyle(Paint.Style.FILL);
                //paint.setColor(Color.RED);
                //canvas.drawRect(x,y,x+100,y+100,paint);
                if(getOwnerPlayerNum()==1) {
                    canvas.drawBitmap(castleBlue, x, y+2, null);
                }
                if(getOwnerPlayerNum()==2) {
                    canvas.drawBitmap(castleRed, x, y+2, null);
                }
                if(getOwnerPlayerNum()==3) {
                    canvas.drawBitmap(castleGreen, x, y+2, null);
                }

            }
            //in anycase draw the areasqr
            //canvas.drawRect(x,y,x+100,y+100,paint);
            //by adding text FPS drops dramaticaly
            if(ownerPlayer==OwnerPlayer.none) {
                paint.setColor(Color.WHITE);
                canvas.drawText("T: " + soilders, x, y + 60, paint);
            }
            //if farm is built draw text
            if(farm){
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.BLACK);
                if(getOwnerPlayerNum()==1) {
                    paint.setColor(Color.RED);
                }
                if(getOwnerPlayerNum()==2) {
                    paint.setColor(Color.WHITE);
                }
                canvas.drawText("Farm lvl: "+farmlvl,x,y+50,paint);
                if(farmInConstruction) {
                    canvas.drawText("TL: "+turnsLeftforFarm,x,y+20,paint);
                }
            }


            //if areasqr has owner for circle
            if(ownerPlayer == OwnerPlayer.player1 && nothingBuilt){
                //paint.setStyle(Paint.Style.FILL);
                //paint.setColor(Color.BLUE);
                //canvas.drawCircle(x+40,y+40,20,paint);
                canvas.drawBitmap(flagBlue, x, y+2, null);
            }
            if(ownerPlayer == OwnerPlayer.player2 && nothingBuilt){
                //paint.setStyle(Paint.Style.FILL);
                //paint.setColor(Color.RED);
                //canvas.drawCircle(x+40,y+40,20,paint);
                canvas.drawBitmap(flagRed, x, y+2, null);
            }
            if(ownerPlayer == OwnerPlayer.player3 && nothingBuilt){
                //paint.setStyle(Paint.Style.FILL);
                //paint.setColor(Color.GREEN);
                //canvas.drawCircle(x+40,y+40,20,paint);
                canvas.drawBitmap(flagGreen, x, y+2, null);
            }

            //if castle is built draw text
            if(hasOwner){
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.BLACK);
                canvas.drawText("Troops: "+soilders,x,y+90,paint);
            }

            //if is seleted by player draw cyan sqr
            if(isSelected){
                paint.setColor(Color.CYAN);
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawRect(x+10,y+10,x+80,y+80,paint);
            }

        }catch (Exception e){}
    }

    public void drawChoise(Canvas canvas){
        Paint paint = new Paint();
        paint.setTextSize(30);

        if(isSelected){
            try{
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.GRAY);

                if(!SecondTerritoryDraw) {
                    canvas.drawRect(0, 640, 1200, 700, paint);
                }

                if (SecondTerritoryDraw && isSelected2nd) {
                    canvas.drawRect(0, 640, 1200, 700, paint);
                }

                paint.setColor(Color.WHITE);

                if(nothingBuilt && isSelected1st && !isSelected2nd && !SecondTerritoryDraw) {
                    canvas.drawText("Farm", 20, 680, paint);
                    canvas.drawText("Castle", 200, 680, paint);
                }

                if(castle && isSelected1st && !isSelected2nd && !SecondTerritoryDraw) {
                    canvas.drawText("Buy Soilder", 20, 680, paint);
                }

                if(isSelected2nd ){//&& ownerPlayer==OwnerPlayer.none
                    canvas.drawText("Attack with: "+troopsChosen,20,680,paint);
                    canvas.drawText("Deploy!",430,680,paint);
                    paint.setTextSize(60);
                    canvas.drawText("+",230,680,paint);
                    canvas.drawText("-",330,680,paint);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawRect(230,640,329,700,paint);
                    canvas.drawRect(330,640,429,700,paint);
                    canvas.drawRect(430,640,550,700,paint);
                    paint.setTextSize(20);
                }

                if(farm && isSelected1st && !isSelected2nd && !SecondTerritoryDraw){
                    canvas.drawText("Upgrade farm",20,680,paint);
                }

            }catch (Exception e){}
        }
    }
}
