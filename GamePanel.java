package com.example.nickghost.strategy1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;
import android.graphics.Color;
import android.graphics.Paint;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    private Random rand = new Random();

    public static final int WIDTH = 1200;//856
    public static final int HEIGHT = 700;//480

    private BackgroundStartMenu backgroundStartMenu;
    private MainThread thread;
    private ArrayList<AreaSqr>areasqrs;
    private ArrayList<Player>player;

    private boolean startnewgame = false;

    private int players=2;
    private boolean numPlayersSet=false;
    private boolean CapitalsSet=false;
    private boolean player1 = true;
    private boolean player2 = true;
    private boolean player3 = false;
    private boolean secondTerritory = false;


    private int numTer1 = -1;
    private int numTer2 = -1;
    private int troopsChosenTer1 = 0;
    private int troopsChosenTer2 = 0;
    private int troopsResult = 0;

    private boolean territory1Selected = false;
    private boolean territory2Selected = false;
    private boolean territory1Deselected = false;
    private boolean territory2Deselected = false;

    private boolean buttonNextTurn = false;
    private enum PlayerTurn {player1, player2, player3};
    private PlayerTurn playerTurn = PlayerTurn.player1;
    private int playerNumber = 1;

    private boolean buttonBuildFarm = false;
    private boolean buttonUpgradeFarm = false;
    private boolean buttonBuildCastle = false;
    private boolean buttonBuyTroops = false;
    private boolean buttonIncTroops = false;
    private boolean buttonDecTroops = false;
    private boolean buttonDeployAttack = false;

    private boolean turnCurtain = false;

    Bitmap imageGameBackground;
    Bitmap imageCastleBlue;
    Bitmap imageCastleRed;
    Bitmap imageCastleGreen;
    Bitmap imageFlagBlue;
    Bitmap imageFlagRed;
    Bitmap imageFlagGreen;
    Bitmap imageFarmBlue;
    Bitmap imageFarmRed;
    Bitmap imageFarmGreen;

    public GamePanel(Context context)
    {
        super(context);

        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        //make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        int counter = 0;
        while(retry && counter<1000)
        {
            counter++;
            try{thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;

            }catch(InterruptedException e){e.printStackTrace();}
        }
    }

    //my constructor
    @Override
    public void surfaceCreated(SurfaceHolder holder){
        areasqrs = new ArrayList<AreaSqr>();
        player = new ArrayList<Player>();

        backgroundStartMenu = new BackgroundStartMenu(BitmapFactory.decodeResource(getResources(),R.drawable.windmill),15);

        imageGameBackground = BitmapFactory.decodeResource(getResources(), R.drawable.battlefield);

        imageCastleBlue = BitmapFactory.decodeResource(getResources(), R.drawable.castleblue);
        imageCastleRed = BitmapFactory.decodeResource(getResources(), R.drawable.castlered);
        imageCastleGreen = BitmapFactory.decodeResource(getResources(), R.drawable.castleyellow);

        imageFlagBlue = BitmapFactory.decodeResource(getResources(), R.drawable.flagblue);
        imageFlagRed = BitmapFactory.decodeResource(getResources(), R.drawable.flagred);
        imageFlagGreen = BitmapFactory.decodeResource(getResources(), R.drawable.flagyellow);

        imageFarmBlue = BitmapFactory.decodeResource(getResources(), R.drawable.farmblue);
        imageFarmRed = BitmapFactory.decodeResource(getResources(), R.drawable.farmred);
        imageFarmGreen = BitmapFactory.decodeResource(getResources(), R.drawable.farmyellow);

        thread = new MainThread(getHolder(), this);
        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();

    }

    //to select other(nearby only)
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        final float scaleFactorXE = getWidth()/(WIDTH*1.f);
        final float scaleFactorYE = getHeight()/(HEIGHT*1.f);

        int xE = (int)event.getX();
        int yE = (int)event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                System.out.println("x-"+xE+" y-"+yE);

                if(turnCurtain){
                    turnCurtain=false;
                }
                else {
                    //the game it self
                    if (startnewgame && numPlayersSet) {
                        //go through all 32 sqrs
                        for (int i = 0; i < areasqrs.size(); i++) {
                            //compare coords of each areasq and userclick
                            if (xE >= areasqrs.get(i).getX() * scaleFactorXE && xE <= areasqrs.get(i).getWidth() * scaleFactorXE &&
                                    yE >= areasqrs.get(i).getY() * scaleFactorYE && yE <= areasqrs.get(i).getHeight() * scaleFactorYE) {

                                if (!areasqrs.get(i).getIsSelected()) {
                                    if (areasqrs.get(i).getHasOwner()) {
                                        if (playerNumber == areasqrs.get(i).getOwnerPlayerNum()) {
                                            territory1Selected = true;
                                            numTer1 = i;
                                        }

                                        if (playerNumber != areasqrs.get(i).getOwnerPlayerNum()  && numTer1 != -1)  {//&& areasqrs.get(numTer1).getIsSelected1st()
                                            onTouchTer2Select(i);
                                        }
                                    }

                                    //TO DO: doesnt matter if has owner or not
                                    //if doesn't have owner and is selected 2nd
                                    if (!areasqrs.get(i).getHasOwner() && !areasqrs.get(i).getIsSelected1st() && numTer1 != -1) {//areasqrs.get(i).getIsSelected1st() &&
                                        onTouchTer2Select(i);
                                    }
                                } else {
                                    if (areasqrs.get(i).getHasOwner()) {

                                        if (playerNumber == areasqrs.get(i).getOwnerPlayerNum()) {
                                            territory1Deselected = true;
                                        }
                                        else{
                                            territory2Deselected = true;
                                        }

                                    } else {
                                        territory2Deselected = true;
                                    }
                                }
                            }

                            //coords for building options and has owner
                            if (areasqrs.get(i).getIsSelected() && areasqrs.get(i).getHasOwner() && !secondTerritory &&
                                    xE >= 1 * scaleFactorXE && xE <= 199 * scaleFactorXE &&
                                    yE >= 640 * scaleFactorYE && yE <= 700 * scaleFactorYE) {

                                //if farm is not built
                                if (areasqrs.get(i).getNothingBuilt()) {
                                    buttonBuildFarm = true;
                                }//if it is built
                                else {
                                    buttonUpgradeFarm = true;
                                }

                                //if castle is built and another territory is not selected
                                if (areasqrs.get(i).getCastle() && areasqrs.get(i).getIsSelected1st() && !secondTerritory) {
                                    buttonBuyTroops = true;
                                }
                            }

                            //coords for building castle
                            if (areasqrs.get(i).getIsSelected() && areasqrs.get(i).getNothingBuilt() &&
                                    areasqrs.get(i).getIsSelected1st() && !secondTerritory &&
                                    xE >= 200 * scaleFactorXE && xE <= 460 * scaleFactorXE &&
                                    yE >= 640 * scaleFactorYE && yE <= 700 * scaleFactorYE) {
                                buttonBuildCastle = true;
                            }

                            //coords for choosing amout of troops to attack +++
                            if (areasqrs.get(i).getIsSelected() && secondTerritory &&
                                    xE >= 230 * scaleFactorXE && xE <= 329 * scaleFactorXE &&
                                    yE >= 640 * scaleFactorYE && yE <= 700 * scaleFactorYE) {
                                buttonIncTroops = true;
                                System.out.println("buttonIncTroops = true;");
                            }

                            //coords for choosing amout of troops to attack ---
                            if (areasqrs.get(i).getIsSelected() && secondTerritory &&
                                    xE >= 330 * scaleFactorXE && xE <= 429 * scaleFactorXE &&
                                    yE >= 640 * scaleFactorYE && yE <= 700 * scaleFactorYE) {
                                buttonDecTroops = true;
                            }

                            //coords for Deploy attack
                            if (areasqrs.get(i).getIsSelected() && areasqrs.get(i).getIsSelected2nd() &&
                                    xE >= 430 * scaleFactorXE && xE <= 550 * scaleFactorXE &&
                                    yE >= 640 * scaleFactorYE && yE <= 700 * scaleFactorYE) {
                                if(troopsChosenTer1>0) {
                                    buttonDeployAttack = true;
                                }
                            }

                        }

                        //Next turn button
                        if (xE >= 1000 * scaleFactorXE && xE <= 1200 * scaleFactorXE && yE >= 640 * scaleFactorYE && yE <= 700 * scaleFactorYE) {
                            buttonNextTurn = true;
                        }
                    }
                }

                //first menu: start game, options, exit
                if(!startnewgame){
                    if (xE >= 450 * scaleFactorXE && xE <= 680 * scaleFactorXE
                            && yE >= 200 * scaleFactorYE && yE <= 350 * scaleFactorYE) {
                        startnewgame = true;
                        System.out.println("start new game - true");
                    }
                }

                //menu for selecting how many players
                if(startnewgame && !numPlayersSet){
                    //+ - players; start button
                    //+ button
                    if (xE >= 100 * scaleFactorXE && xE <= 300 * scaleFactorXE
                            && yE >= 200 * scaleFactorYE && yE <= 300 * scaleFactorYE) {
                        if(players<=2) {
                            players++;
                        }
                        if(players==3){
                            player3=true;
                        }

                    }
                    //- button
                    if (xE >= 300 * scaleFactorXE && xE <= 500 * scaleFactorXE
                            && yE >= 200 * scaleFactorYE && yE <= 300 * scaleFactorYE) {
                        if(players>2){
                            players--;
                        }
                        if(players<3){
                            player3=false;
                        }
                    }

                    //start button
                    if (xE >= 100 * scaleFactorXE && xE <= 300 * scaleFactorXE
                            && yE >= 300 * scaleFactorYE && yE <= 400 * scaleFactorYE) {
                        for(int i=0;i<=players;i++) {
                            player.add(new Player());
                        }
                        numPlayersSet=true;
                    }
                }
            }break;
            case MotionEvent.ACTION_MOVE:{}break;
            case MotionEvent.ACTION_UP:{}break;

        }
        return false;
    }

    public void update(){

        if(!startnewgame){
            backgroundStartMenu.update();
            //drawStartBackground(canvas);
        }

        //initialize all the area squares for the game
        if(areasqrs.isEmpty() && startnewgame && numPlayersSet) {
            initBattleField();
        }

        //initialize capitals for each player
        if(!CapitalsSet && numPlayersSet){
            setCapitals();
            CapitalsSet = true;
        }

        //update function for each territory AND send currentTurn to block with FarmsInConstruction
        updateFarmsAndConstruction();


        //update function for each player
        for(int i=0;i<player.size();i++) {
            player.get(i).update();
        }

        //Territory Selection LOGIC//
        updateTerritorySelection();

        //BUTTONS LOGIC//
        updateButtonLogic();

        //Button next turn//
        updateButtonNextTurn();
    }

    public boolean collision(GameObject a, GameObject b)
    {
        return false;
    }

    @Override
    public void draw(Canvas canvas)
    {
        final float scaleFactorX = getWidth()/(WIDTH*1.f);
        final float scaleFactorY = getHeight()/(HEIGHT*1.f);

        if(canvas!=null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);

            if(startnewgame && numPlayersSet){
                if(turnCurtain) {
                    drawTurnCurtain(canvas);
                }
                else {
                    drawMapBackground(canvas);

                    for (AreaSqr as : areasqrs) {
                        as.draw(canvas);
                    }

                    for (AreaSqr as : areasqrs) {
                        as.drawChoise(canvas);
                    }

                    drawNextTurnButton(canvas);
                }
            }

            if(!startnewgame){
                backgroundStartMenu.draw(canvas);
                //drawStartBackground(canvas);
            }

            if(startnewgame && !numPlayersSet){
                drawSetPlayersMenu(canvas);
            }

            canvas.restoreToCount(savedState);
        }
    }

    public void drawSetPlayersMenu(Canvas canvas){
        Paint paint = new Paint();

        paint.setColor(Color.LTGRAY);
        canvas.drawRect(0,0,1200,700,paint);

        paint.setTextSize(40);
        paint.setColor(Color.WHITE);
        canvas.drawText("Enter how many players: "+players,100,100,paint);

        paint.setTextSize(60);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(100,200,300,300,paint);
        canvas.drawRect(300,200,500,300,paint);

        paint.setTextSize(100);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText("+",200,270,paint);
        canvas.drawText("-",400,270,paint);

        paint.setTextSize(60);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(100,300,300,400,paint);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText("Start",150,350,paint);

    }

    public void drawMapBackground(Canvas canvas){
        canvas.drawBitmap(imageGameBackground,0,0,null);
    }

    public void drawNextTurnButton(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
        canvas.drawText("Next turn",1050,680,paint);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(1000,640,1200,700,paint);

        switch (playerNumber){
            case 1: {
                canvas.drawText("Food: "+player.get(0).getResourceFood(),800,680,paint);
                break;
            }
            case 2:{
                canvas.drawText("Food: "+player.get(1).getResourceFood(),800,680,paint);
                break;
            }
            case 3:{
                canvas.drawText("Food: "+player.get(2).getResourceFood(),800,680,paint);
                break;
            }
            default:break;
        }
    }

    public void setCapitals(){
        //size is the max number and 1 is our minimum

        int n1 = rand.nextInt(areasqrs.size());//int
        int n2=0;
        int n3=0;
        n1=0;
        System.out.println("rand1-"+n1);
        areasqrs.get(n1).setOwnerPlayer(GameObject.OwnerPlayer.player1);
        areasqrs.get(n1).setCastle(true);
        areasqrs.get(n1).setSoilders(5);
        areasqrs.get(n1).setNothingBuilt(false);

        if(player2) {
            do {
                n2 = rand.nextInt(areasqrs.size());
                System.out.println("rand2-" + n2);
            }while(n2==n1);
            areasqrs.get(n2).setOwnerPlayer(GameObject.OwnerPlayer.player2);
            areasqrs.get(n2).setCastle(true);
            areasqrs.get(n2).setSoilders(5);
            areasqrs.get(n2).setNothingBuilt(false);
        }

        if(player3) {
            do {
                n3 = rand.nextInt(areasqrs.size());
                System.out.println("rand3-" + n3);
            }while(n3==n1 || n3==n2);
            areasqrs.get(n3).setOwnerPlayer(GameObject.OwnerPlayer.player3);
            areasqrs.get(n3).setCastle(true);
            areasqrs.get(n3).setSoilders(5);
            areasqrs.get(n3).setNothingBuilt(false);
        }

        turnCurtain=true;
    }

    public void drawTurnCurtain(Canvas canvas){
        Paint paint = new Paint();

        paint.setTextSize(70);
        if(playerNumber==1) {
            paint.setColor(Color.WHITE);

            canvas.drawRect(0,0,1200,700,paint);
            paint.setColor(Color.BLUE);
            canvas.drawText("Player 1 Turn", 250, 200, paint);
        }
        else if(playerNumber==2){
            paint.setColor(Color.WHITE);

            canvas.drawRect(0,0,1200,700,paint);
            paint.setColor(Color.RED);
            canvas.drawText("Player 2 Turn", 250, 200, paint);
        }
        else if(playerNumber==3){
            paint.setColor(Color.WHITE);

            canvas.drawRect(0,0,1200,700,paint);
            paint.setColor(Color.GREEN);
            canvas.drawText("Player 3 Turn", 250, 200, paint);
        }
    }

    public void initBattleField(){
        /*
            int i,j;
            int rows=800,cols=400;
            for (i=0; i < rows; i+=100) {
                for (j = 0; j < cols; j+=100) {
                    //System.out.println(matrix[i][j] + " ");
                    areasqrs.add(new AreaSqr(0+i, 0+j, imageCastleBlue, imageCastleRed, imageCastleGreen,
                            imageFlagBlue, imageFlagRed, imageFlagGreen,
                            imageFarmBlue,imageFarmRed,imageFarmGreen));
                }
            }
            */
        //row1
        //x dist btween 263
        areasqrs.add(new AreaSqr(160, 65, imageCastleBlue, imageCastleRed, imageCastleGreen,
                imageFlagBlue, imageFlagRed, imageFlagGreen,
                imageFarmBlue,imageFarmRed,imageFarmGreen));
        areasqrs.add(new AreaSqr(423, 65, imageCastleBlue, imageCastleRed, imageCastleGreen,
                imageFlagBlue, imageFlagRed, imageFlagGreen,
                imageFarmBlue,imageFarmRed,imageFarmGreen));
        areasqrs.add(new AreaSqr(686, 65, imageCastleBlue, imageCastleRed, imageCastleGreen,
                imageFlagBlue, imageFlagRed, imageFlagGreen,
                imageFarmBlue,imageFarmRed,imageFarmGreen));
        areasqrs.add(new AreaSqr(949, 65, imageCastleBlue, imageCastleRed, imageCastleGreen,
                imageFlagBlue, imageFlagRed, imageFlagGreen,
                imageFarmBlue,imageFarmRed,imageFarmGreen));
        //row2
        //x dist btween 271
        areasqrs.add(new AreaSqr(281, 119, imageCastleBlue, imageCastleRed, imageCastleGreen,
                imageFlagBlue, imageFlagRed, imageFlagGreen,
                imageFarmBlue,imageFarmRed,imageFarmGreen));
        areasqrs.add(new AreaSqr(552, 119, imageCastleBlue, imageCastleRed, imageCastleGreen,
                imageFlagBlue, imageFlagRed, imageFlagGreen,
                imageFarmBlue,imageFarmRed,imageFarmGreen));
        areasqrs.add(new AreaSqr(823, 119, imageCastleBlue, imageCastleRed, imageCastleGreen,
                imageFlagBlue, imageFlagRed, imageFlagGreen,
                imageFarmBlue,imageFarmRed,imageFarmGreen));
        //row3
        //x dist btween 277
        areasqrs.add(new AreaSqr(136, 176, imageCastleBlue, imageCastleRed, imageCastleGreen,
                imageFlagBlue, imageFlagRed, imageFlagGreen,
                imageFarmBlue,imageFarmRed,imageFarmGreen));
        areasqrs.add(new AreaSqr(413, 176, imageCastleBlue, imageCastleRed, imageCastleGreen,
                imageFlagBlue, imageFlagRed, imageFlagGreen,
                imageFarmBlue,imageFarmRed,imageFarmGreen));
        areasqrs.add(new AreaSqr(690, 176, imageCastleBlue, imageCastleRed, imageCastleGreen,
                imageFlagBlue, imageFlagRed, imageFlagGreen,
                imageFarmBlue,imageFarmRed,imageFarmGreen));
        areasqrs.add(new AreaSqr(967, 176, imageCastleBlue, imageCastleRed, imageCastleGreen,
                imageFlagBlue, imageFlagRed, imageFlagGreen,
                imageFarmBlue,imageFarmRed,imageFarmGreen));

        //row4
        //x dist btween 287
        areasqrs.add(new AreaSqr(263, 237, imageCastleBlue, imageCastleRed, imageCastleGreen,
                imageFlagBlue, imageFlagRed, imageFlagGreen,
                imageFarmBlue,imageFarmRed,imageFarmGreen));
        areasqrs.add(new AreaSqr(550, 237, imageCastleBlue, imageCastleRed, imageCastleGreen,
                imageFlagBlue, imageFlagRed, imageFlagGreen,
                imageFarmBlue,imageFarmRed,imageFarmGreen));
        areasqrs.add(new AreaSqr(837, 237, imageCastleBlue, imageCastleRed, imageCastleGreen,
                imageFlagBlue, imageFlagRed, imageFlagGreen,
                imageFarmBlue,imageFarmRed,imageFarmGreen));

        //row5
        //x dist btween 293
        areasqrs.add(new AreaSqr(108, 302, imageCastleBlue, imageCastleRed, imageCastleGreen,
                imageFlagBlue, imageFlagRed, imageFlagGreen,
                imageFarmBlue,imageFarmRed,imageFarmGreen));
        areasqrs.add(new AreaSqr(401, 302, imageCastleBlue, imageCastleRed, imageCastleGreen,
                imageFlagBlue, imageFlagRed, imageFlagGreen,
                imageFarmBlue,imageFarmRed,imageFarmGreen));
        areasqrs.add(new AreaSqr(694, 302, imageCastleBlue, imageCastleRed, imageCastleGreen,
                imageFlagBlue, imageFlagRed, imageFlagGreen,
                imageFarmBlue,imageFarmRed,imageFarmGreen));
        areasqrs.add(new AreaSqr(987, 302, imageCastleBlue, imageCastleRed, imageCastleGreen,
                imageFlagBlue, imageFlagRed, imageFlagGreen,
                imageFarmBlue,imageFarmRed,imageFarmGreen));

        //row6
        //x dist btween 304
        areasqrs.add(new AreaSqr(243, 372, imageCastleBlue, imageCastleRed, imageCastleGreen,
                imageFlagBlue, imageFlagRed, imageFlagGreen,
                imageFarmBlue,imageFarmRed,imageFarmGreen));
        areasqrs.add(new AreaSqr(547, 372, imageCastleBlue, imageCastleRed, imageCastleGreen,
                imageFlagBlue, imageFlagRed, imageFlagGreen,
                imageFarmBlue,imageFarmRed,imageFarmGreen));
        areasqrs.add(new AreaSqr(851, 372, imageCastleBlue, imageCastleRed, imageCastleGreen,
                imageFlagBlue, imageFlagRed, imageFlagGreen,
                imageFarmBlue,imageFarmRed,imageFarmGreen));

        //row7
        //x dist btween 314
        areasqrs.add(new AreaSqr(75, 445, imageCastleBlue, imageCastleRed, imageCastleGreen,
                imageFlagBlue, imageFlagRed, imageFlagGreen,
                imageFarmBlue,imageFarmRed,imageFarmGreen));
        areasqrs.add(new AreaSqr(389, 445, imageCastleBlue, imageCastleRed, imageCastleGreen,
                imageFlagBlue, imageFlagRed, imageFlagGreen,
                imageFarmBlue,imageFarmRed,imageFarmGreen));
        areasqrs.add(new AreaSqr(703, 445, imageCastleBlue, imageCastleRed, imageCastleGreen,
                imageFlagBlue, imageFlagRed, imageFlagGreen,
                imageFarmBlue,imageFarmRed,imageFarmGreen));
        areasqrs.add(new AreaSqr(1017, 445, imageCastleBlue, imageCastleRed, imageCastleGreen,
                imageFlagBlue, imageFlagRed, imageFlagGreen,
                imageFarmBlue,imageFarmRed,imageFarmGreen));

    }

    public void updateTerritorySelection() {
        if (territory1Selected) {
            //if there was another sqr WITH an owner selected before, deselect it
            for (int j = 0; j < areasqrs.size(); j++) {
                if (j != numTer1) {
                    areasqrs.get(j).setIsSelected(false);
                    areasqrs.get(numTer1).setIsSelected1st(false);
                }
            }
            areasqrs.get(numTer1).setIsSelected(true);
            areasqrs.get(numTer1).setIsSelected1st(true);
            System.out.println("ter1 selected");

            territory1Selected = false;
        }

        if (territory2Selected) {
            //TO DO: doesnt matter if has owner or not

            //if there was another sqr without an owner selected before, deselect it
            for (int j = 0; j < areasqrs.size(); j++) {
                if (j != numTer2) {//&& !areasqrs.get(j).getHasOwner()
                    if (playerNumber != areasqrs.get(j).getOwnerPlayerNum()) {
                        areasqrs.get(j).setIsSelected(false);
                        areasqrs.get(numTer2).setIsSelected2nd(false);
                    }
                }
            }
            areasqrs.get(numTer2).setIsSelected(true);
            areasqrs.get(numTer2).setIsSelected2nd(true);

            areasqrs.get(numTer1).setSecondTerritoryDraw(true);
            areasqrs.get(numTer2).setSecondTerritoryDraw(true);
            secondTerritory = true;
            System.out.println("secondTerritory = true;");

            territory2Selected = false;
        }

        if (territory1Deselected) {
            for (int j = 0; j < areasqrs.size(); j++) {
                areasqrs.get(j).setIsSelected(false);
                areasqrs.get(j).setIsSelected2nd(false);
            }

            areasqrs.get(numTer1).setIsSelected1st(false);

            areasqrs.get(numTer1).setSecondTerritoryDraw(false);
            areasqrs.get(numTer2).setSecondTerritoryDraw(false);
            secondTerritory = false;

            numTer1 = -1;
/*
            if(turnCurtainOn){
                turnCurtain=true;
                System.out.println("turnCurtain=true;");
                turnCurtainOn=false;
            }
*/
            territory1Deselected = false;
        }

        if (territory2Deselected) {
            areasqrs.get(numTer2).setIsSelected(false);
            areasqrs.get(numTer2).setIsSelected2nd(false);

            areasqrs.get(numTer1).setSecondTerritoryDraw(false);
            areasqrs.get(numTer2).setSecondTerritoryDraw(false);
            secondTerritory = false;

            territory2Deselected = false;
        }
    }

    public void updateFarmsAndConstruction() {
        for (int i = 0; i < areasqrs.size(); i++) {
            areasqrs.get(i).update();

            if (player2 && !player3) {
                switch (areasqrs.get(i).getOwnerPlayerNum()) {
                    case 1: {
                        if (areasqrs.get(i).getfarmlevel1Finished()) {
                            System.out.println("if(areasqrs.get(i).getfarmlevel1Finished()){");
                            player.get(0).farmsBuiltLevel1();
                            player.get(0).setFood();
                            areasqrs.get(i).setfarmlevel1Finished(false);
                        } else if (areasqrs.get(i).getfarmlevel2Finished()) {
                            System.out.println(" else if(areasqrs.get(i).getfarmlevel2Finished()){");
                            player.get(0).farmsDestroyLevel1();
                            player.get(0).farmsBuiltLevel2();
                            player.get(0).setFood();
                            areasqrs.get(i).setfarmlevel2Finished(false);
                        } else if (areasqrs.get(i).getfarmlevel3Finished()) {
                            player.get(0).farmsBuiltLevel3();
                            player.get(0).farmsDestroyLevel2();
                            player.get(0).setFood();
                            areasqrs.get(i).setfarmlevel3Finished(false);
                        } else if (areasqrs.get(i).getfarmlevel4Finished()) {
                            player.get(0).farmsBuiltLevel4();
                            player.get(0).farmsDestroyLevel3();
                            player.get(0).setFood();
                            areasqrs.get(i).setfarmlevel4Finished(false);
                        } else if (areasqrs.get(i).getfarmlevel5Finished()) {
                            player.get(0).farmsBuiltLevel5();
                            player.get(0).farmsDestroyLevel4();
                            player.get(0).setFood();
                            areasqrs.get(i).setfarmlevel5Finished(false);
                        } else if (player.get(1).getEndTurn() && !player.get(0).getFoodCollected()) {//if this turn food was not set
                            System.out.println("if(endTurn) {");
                            player.get(0).setFood();
                            player.get(1).setEndTurn(false);
                        }


                        break;
                    }
                    case 2: {
                        if (areasqrs.get(i).getfarmlevel1Finished()) {
                            player.get(1).farmsBuiltLevel1();
                            player.get(1).setFood();
                            areasqrs.get(i).setfarmlevel1Finished(false);
                        } else if (areasqrs.get(i).getfarmlevel2Finished()) {
                            player.get(1).farmsDestroyLevel1();
                            player.get(1).farmsBuiltLevel2();
                            player.get(1).setFood();
                            areasqrs.get(i).setfarmlevel2Finished(false);
                        } else if (areasqrs.get(i).getfarmlevel3Finished()) {
                            player.get(1).farmsBuiltLevel3();
                            player.get(1).farmsDestroyLevel2();
                            player.get(1).setFood();
                            areasqrs.get(i).setfarmlevel3Finished(false);
                        } else if (areasqrs.get(i).getfarmlevel4Finished()) {
                            player.get(1).farmsBuiltLevel4();
                            player.get(1).farmsDestroyLevel3();
                            player.get(1).setFood();
                            areasqrs.get(i).setfarmlevel4Finished(false);
                        } else if (areasqrs.get(i).getfarmlevel5Finished()) {
                            player.get(1).farmsBuiltLevel5();
                            player.get(1).farmsDestroyLevel4();
                            player.get(1).setFood();
                            areasqrs.get(i).setfarmlevel5Finished(false);
                        } else if (player.get(0).getEndTurn() && !player.get(1).getFoodCollected()) {//if this turn food was not set
                            System.out.println("if(endTurn) {");
                            player.get(1).setFood();
                            player.get(0).setEndTurn(false);
                        }
                        break;
                    }
                    default:
                        break;
                }
            }

            if (player2 && player3) {
                switch (areasqrs.get(i).getOwnerPlayerNum()) {
                    case 1: {
                        if (areasqrs.get(i).getfarmlevel1Finished()) {
                            System.out.println("if(areasqrs.get(i).getfarmlevel1Finished()){");
                            player.get(0).farmsBuiltLevel1();
                            player.get(0).setFood();
                            areasqrs.get(i).setfarmlevel1Finished(false);
                        } else if (areasqrs.get(i).getfarmlevel2Finished()) {
                            System.out.println(" else if(areasqrs.get(i).getfarmlevel2Finished()){");
                            player.get(0).farmsDestroyLevel1();
                            player.get(0).farmsBuiltLevel2();
                            player.get(0).setFood();
                            areasqrs.get(i).setfarmlevel2Finished(false);
                        } else if (areasqrs.get(i).getfarmlevel3Finished()) {
                            player.get(0).farmsBuiltLevel3();
                            player.get(0).farmsDestroyLevel2();
                            player.get(0).setFood();
                            areasqrs.get(i).setfarmlevel3Finished(false);
                        } else if (areasqrs.get(i).getfarmlevel4Finished()) {
                            player.get(0).farmsBuiltLevel4();
                            player.get(0).farmsDestroyLevel3();
                            player.get(0).setFood();
                            areasqrs.get(i).setfarmlevel4Finished(false);
                        } else if (areasqrs.get(i).getfarmlevel5Finished()) {
                            player.get(0).farmsBuiltLevel5();
                            player.get(0).farmsDestroyLevel4();
                            player.get(0).setFood();
                            areasqrs.get(i).setfarmlevel5Finished(false);
                        } else if (player.get(1).getEndTurn() && !player.get(0).getFoodCollected()) {//if this turn food was not set
                            System.out.println("if(endTurn) {");
                            player.get(0).setFood();
                            player.get(1).setEndTurn(false);
                        }


                        break;
                    }
                    case 2: {
                        if (areasqrs.get(i).getfarmlevel1Finished()) {
                            player.get(1).farmsBuiltLevel1();
                            player.get(1).setFood();
                            areasqrs.get(i).setfarmlevel1Finished(false);
                        } else if (areasqrs.get(i).getfarmlevel2Finished()) {
                            player.get(1).farmsDestroyLevel1();
                            player.get(1).farmsBuiltLevel2();
                            player.get(1).setFood();
                            areasqrs.get(i).setfarmlevel2Finished(false);
                        } else if (areasqrs.get(i).getfarmlevel3Finished()) {
                            player.get(1).farmsBuiltLevel3();
                            player.get(1).farmsDestroyLevel2();
                            player.get(1).setFood();
                            areasqrs.get(i).setfarmlevel3Finished(false);
                        } else if (areasqrs.get(i).getfarmlevel4Finished()) {
                            player.get(1).farmsBuiltLevel4();
                            player.get(1).farmsDestroyLevel3();
                            player.get(1).setFood();
                            areasqrs.get(i).setfarmlevel4Finished(false);
                        } else if (areasqrs.get(i).getfarmlevel5Finished()) {
                            player.get(1).farmsBuiltLevel5();
                            player.get(1).farmsDestroyLevel4();
                            player.get(1).setFood();
                            areasqrs.get(i).setfarmlevel5Finished(false);
                        } else if (player.get(2).getEndTurn() && !player.get(1).getFoodCollected()) {//if this turn food was not set
                            System.out.println("if(endTurn) {");
                            player.get(1).setFood();
                            player.get(2).setEndTurn(false);
                        }
                        break;
                    }
                    case 3: {
                        if (areasqrs.get(i).getfarmlevel1Finished()) {
                            System.out.println("if(areasqrs.get(i).getfarmlevel1Finished()){");
                            player.get(2).farmsBuiltLevel1();
                            player.get(2).setFood();
                            areasqrs.get(i).setfarmlevel1Finished(false);
                        } else if (areasqrs.get(i).getfarmlevel2Finished()) {
                            System.out.println(" else if(areasqrs.get(i).getfarmlevel2Finished()){");
                            player.get(2).farmsDestroyLevel1();
                            player.get(2).farmsBuiltLevel2();
                            player.get(2).setFood();
                            areasqrs.get(i).setfarmlevel2Finished(false);
                        } else if (areasqrs.get(i).getfarmlevel3Finished()) {
                            player.get(2).farmsBuiltLevel3();
                            player.get(2).farmsDestroyLevel2();
                            player.get(2).setFood();
                            areasqrs.get(i).setfarmlevel3Finished(false);
                        } else if (areasqrs.get(i).getfarmlevel4Finished()) {
                            player.get(2).farmsBuiltLevel4();
                            player.get(2).farmsDestroyLevel3();
                            player.get(2).setFood();
                            areasqrs.get(i).setfarmlevel4Finished(false);
                        } else if (areasqrs.get(i).getfarmlevel5Finished()) {
                            player.get(2).farmsBuiltLevel5();
                            player.get(2).farmsDestroyLevel4();
                            player.get(2).setFood();
                            areasqrs.get(i).setfarmlevel5Finished(false);
                        } else if (player.get(0).getEndTurn() && !player.get(2).getFoodCollected()) {//if this turn food was not set
                            System.out.println("if(endTurn) {");
                            player.get(2).setFood();
                            player.get(0).setEndTurn(false);
                        }
                        break;
                    }
                    default:
                        break;
                }
            }
            ////////////////END OF SWITCH/////////////////////

            if (areasqrs.get(i).getFarmInConstruction()) {//&& areasqrs.get(i).getOwnerPlayerNum()==1)
                switch (areasqrs.get(i).getOwnerPlayerNum()) {
                    case 1: {
                        areasqrs.get(i).setCurrentTurn(player.get(0).getTurn());
                        break;
                    }
                    case 2: {
                        areasqrs.get(i).setCurrentTurn(player.get(1).getTurn());
                        break;
                    }
                    case 3: {
                        areasqrs.get(i).setCurrentTurn(player.get(2).getTurn());
                        break;
                    }
                    default:
                        break;
                }
            }
        }
    }

    public void updateButtonNextTurn() {
        if (buttonNextTurn) {
            if (player2 && !player3) {
                switch (playerTurn) {
                    case player1: {
                        playerTurn = PlayerTurn.player2;
                        player.get(0).setTurn();
                        player.get(0).setEndTurn(true);
                        playerNumber = 2;
                        break;
                    }
                    case player2: {
                        playerTurn = PlayerTurn.player1;
                        player.get(1).setTurn();
                        player.get(1).setEndTurn(true);
                        playerNumber = 1;
                        break;
                    }
                    default:
                        break;
                }
            }


            if (player2 && player3) {
                switch (playerTurn) {
                    case player1: {
                        playerTurn = PlayerTurn.player2;
                        player.get(0).setTurn();
                        player.get(0).setEndTurn(true);
                        playerNumber = 2;
                        break;
                    }
                    case player2: {
                        playerTurn = PlayerTurn.player3;
                        player.get(1).setTurn();
                        player.get(1).setEndTurn(true);
                        playerNumber = 3;
                        break;
                    }
                    case player3: {
                        playerTurn = PlayerTurn.player1;
                        player.get(2).setTurn();
                        player.get(2).setEndTurn(true);
                        playerNumber = 1;
                        break;
                    }
                    default:
                        break;
                }
            }

            if (territory1Selected) {//!territory1Deselected
                territory1Deselected = true;
                System.out.println("territory1Deselected = true");
            }


            turnCurtain = true;

            buttonNextTurn = false;
        }
    }

    public void updateButtonLogic() {
        if (buttonBuildFarm) {
            areasqrs.get(numTer1).setFarm(true);
            areasqrs.get(numTer1).setNothingBuilt(false);

            buttonUpgradeFarm = true;

            buttonBuildFarm = false;
        }

        if (buttonUpgradeFarm) {
            if (areasqrs.get(numTer1).getFarmlvl() < 5 && !areasqrs.get(numTer1).getFarmInConstruction()) {
                switch (playerTurn) {
                    case player1: {
                        areasqrs.get(numTer1).setCurrentTurn(player.get(0).getTurn());
                        areasqrs.get(numTer1).setFarmInConstruction(true);
                        System.out.println("In construction = " + areasqrs.get(numTer1).getFarmInConstruction());
                        break;
                    }
                    case player2: {
                        areasqrs.get(numTer1).setCurrentTurn(player.get(1).getTurn());
                        areasqrs.get(numTer1).setFarmInConstruction(true);
                        break;
                    }
                    case player3: {
                        areasqrs.get(numTer1).setCurrentTurn(player.get(2).getTurn());
                        areasqrs.get(numTer1).setFarmInConstruction(true);
                        break;
                    }
                    default:
                        break;
                }
                //check if works whith out switches == less code
                //areasqrs.get(numTer1).setFarmInConstruction(true);
            }

            buttonUpgradeFarm = false;
        }

        if (buttonBuildCastle) {
            areasqrs.get(numTer1).setCastle(true);
            areasqrs.get(numTer1).setNothingBuilt(false);

            buttonBuildCastle = false;
        }

        if (buttonBuyTroops) {
            //if enought food buy troop
            switch (playerNumber) {
                case 1: {
                    if (player.get(0).getResourceFood() > 0) {
                        areasqrs.get(numTer1).setSoilders(areasqrs.get(numTer1).getSoilders() + 1);
                        System.out.println("soilder: " + areasqrs.get(numTer1).getSoilders());

                        player.get(0).setTroopsBought();
                    }
                    break;
                }
                case 2: {
                    if (player.get(1).getResourceFood() > 0) {
                        areasqrs.get(numTer1).setSoilders(areasqrs.get(numTer1).getSoilders() + 1);
                        System.out.println("soilder: " + areasqrs.get(numTer1).getSoilders());

                        player.get(1).setTroopsBought();
                    }
                    break;
                }
                case 3: {
                    if (player.get(2).getResourceFood() > 0) {
                        areasqrs.get(numTer1).setSoilders(areasqrs.get(numTer1).getSoilders() + 1);
                        System.out.println("soilder: " + areasqrs.get(numTer1).getSoilders());

                        player.get(2).setTroopsBought();
                    }
                    break;
                }
                default:
                    break;
            }

            buttonBuyTroops = false;
        }

        //buttonIncTroops
        if (buttonIncTroops) {
            if (troopsChosenTer1 < areasqrs.get(numTer1).getSoilders()) {
                troopsChosenTer1++;
                areasqrs.get(numTer2).setTroopsChosen(troopsChosenTer1);
            }
            buttonIncTroops = false;
        }

        if (buttonDecTroops) {
            if (troopsChosenTer1 > 0) {
                troopsChosenTer1--;
                areasqrs.get(numTer2).setTroopsChosen(troopsChosenTer1);
            }
            buttonDecTroops = false;
        }

        if (buttonDeployAttack) {
            System.out.println("troopsChosenTer1 " + troopsChosenTer1);
            int troopsLeft = areasqrs.get(numTer1).getSoilders() - troopsChosenTer1;
            areasqrs.get(numTer1).setSoilders(troopsLeft);
            System.out.println("troopsLeft " + troopsLeft);

            troopsChosenTer2 = areasqrs.get(numTer2).getSoilders();
            System.out.println("troopsChosenTer2 " + troopsChosenTer2);

            int numbTroopsFromTer1 = troopsChosenTer1;
            int numbTroopsFromTer2 = troopsChosenTer2;

            ///*
            boolean winner = false;
            boolean winnerPlayer1 = false;
            boolean winnerPlayer2 = false;
            if (numbTroopsFromTer2 == 0) {
                areasqrs.get(numTer2).setOwnerChangedStopConstruction();
                troopsResult = numbTroopsFromTer1;
                if (playerNumber == 1) {
                    areasqrs.get(numTer2).setOwnerPlayer(GameObject.OwnerPlayer.player1);
                } else if (playerNumber == 2) {
                    areasqrs.get(numTer2).setOwnerPlayer(GameObject.OwnerPlayer.player2);
                } else if (playerNumber == 3) {
                    areasqrs.get(numTer2).setOwnerPlayer(GameObject.OwnerPlayer.player3);
                }
            } else {
                while (!winner) {
                    //rand.nextInt(areasqrs.size());
                    int cube1 = rand.nextInt(6) + 1;
                    int cube2 = rand.nextInt(6) + 1;
                    System.out.println("cube1:  " + cube1);
                    System.out.println("cube2:  " + cube2);

                    if (cube1 >= cube2) {
                        numbTroopsFromTer2--;
                    } else {
                        numbTroopsFromTer1--;
                    }

                    if (numbTroopsFromTer1 == 0) {
                        winner = true;
                        winnerPlayer2 = true;
                    }
                    if (numbTroopsFromTer2 == 0) {
                        winner = true;
                        winnerPlayer1 = true;
                    }
                }

                if (winnerPlayer1) {
                    troopsResult = numbTroopsFromTer1;
                    if (playerNumber == 1) {
                        if (areasqrs.get(numTer2).getFarm()) {
                            if (areasqrs.get(numTer2).getFarmlvl() == 1) {
                                if (areasqrs.get(numTer2).getOwnerPlayerNum() == 2) {
                                    player.get(1).farmsDestroyLevel1();
                                }
                                if (areasqrs.get(numTer2).getOwnerPlayerNum() == 3) {
                                    player.get(2).farmsDestroyLevel1();
                                }
                                player.get(0).farmsBuiltLevel1();
                            }
                            if (areasqrs.get(numTer2).getFarmlvl() == 2) {
                                if (areasqrs.get(numTer2).getOwnerPlayerNum() == 2) {
                                    player.get(1).farmsDestroyLevel2();
                                }
                                if (areasqrs.get(numTer2).getOwnerPlayerNum() == 3) {
                                    player.get(2).farmsDestroyLevel2();
                                }
                                player.get(0).farmsBuiltLevel2();
                            }
                            if (areasqrs.get(numTer2).getFarmlvl() == 3) {
                                if (areasqrs.get(numTer2).getOwnerPlayerNum() == 2) {
                                    player.get(1).farmsDestroyLevel3();
                                }
                                if (areasqrs.get(numTer2).getOwnerPlayerNum() == 3) {
                                    player.get(2).farmsDestroyLevel3();
                                }
                                player.get(0).farmsBuiltLevel3();
                            }
                            if (areasqrs.get(numTer2).getFarmlvl() == 4) {
                                if (areasqrs.get(numTer2).getOwnerPlayerNum() == 2) {
                                    player.get(1).farmsDestroyLevel4();
                                }
                                if (areasqrs.get(numTer2).getOwnerPlayerNum() == 3) {
                                    player.get(2).farmsDestroyLevel4();
                                }
                                player.get(0).farmsBuiltLevel4();
                            }
                            if (areasqrs.get(numTer2).getFarmlvl() == 5) {
                                if (areasqrs.get(numTer2).getOwnerPlayerNum() == 2) {
                                    player.get(1).farmsDestroyLevel5();
                                }
                                if (areasqrs.get(numTer2).getOwnerPlayerNum() == 3) {
                                    player.get(2).farmsDestroyLevel5();
                                }
                                player.get(0).farmsBuiltLevel5();
                            }
                        }
                        areasqrs.get(numTer2).setOwnerPlayer(GameObject.OwnerPlayer.player1);
                    } else if (playerNumber == 2) {

                        if (areasqrs.get(numTer2).getFarmlvl() == 1) {
                            if (areasqrs.get(numTer2).getOwnerPlayerNum() == 1) {
                                player.get(0).farmsDestroyLevel1();
                            }
                            if (areasqrs.get(numTer2).getOwnerPlayerNum() == 3) {
                                player.get(2).farmsDestroyLevel1();
                            }
                            player.get(1).farmsBuiltLevel1();
                        }
                        if (areasqrs.get(numTer2).getFarmlvl() == 2) {
                            if (areasqrs.get(numTer2).getOwnerPlayerNum() == 1) {
                                player.get(0).farmsDestroyLevel2();
                            }
                            if (areasqrs.get(numTer2).getOwnerPlayerNum() == 3) {
                                player.get(2).farmsDestroyLevel2();
                            }
                            player.get(1).farmsBuiltLevel2();
                        }
                        if (areasqrs.get(numTer2).getFarmlvl() == 3) {
                            if (areasqrs.get(numTer2).getOwnerPlayerNum() == 1) {
                                player.get(0).farmsDestroyLevel3();
                            }
                            if (areasqrs.get(numTer2).getOwnerPlayerNum() == 3) {
                                player.get(2).farmsDestroyLevel3();
                            }
                            player.get(1).farmsBuiltLevel3();
                        }
                        if (areasqrs.get(numTer2).getFarmlvl() == 4) {
                            if (areasqrs.get(numTer2).getOwnerPlayerNum() == 1) {
                                player.get(0).farmsDestroyLevel4();
                            }
                            if (areasqrs.get(numTer2).getOwnerPlayerNum() == 3) {
                                player.get(2).farmsDestroyLevel4();
                            }
                            player.get(1).farmsBuiltLevel4();
                        }
                        if (areasqrs.get(numTer2).getFarmlvl() == 5) {
                            if (areasqrs.get(numTer2).getOwnerPlayerNum() == 1) {
                                player.get(0).farmsDestroyLevel5();
                            }
                            if (areasqrs.get(numTer2).getOwnerPlayerNum() == 3) {
                                player.get(2).farmsDestroyLevel5();
                            }
                            player.get(1).farmsBuiltLevel5();
                        }

                        areasqrs.get(numTer2).setOwnerPlayer(GameObject.OwnerPlayer.player2);
                    } else if (playerNumber == 3) {

                        if (areasqrs.get(numTer2).getFarmlvl() == 1) {
                            if (areasqrs.get(numTer2).getOwnerPlayerNum() == 2) {
                                player.get(1).farmsDestroyLevel1();
                            }
                            if (areasqrs.get(numTer2).getOwnerPlayerNum() == 1) {
                                player.get(0).farmsDestroyLevel1();
                            }
                            player.get(2).farmsBuiltLevel1();
                        }
                        if (areasqrs.get(numTer2).getFarmlvl() == 2) {
                            if (areasqrs.get(numTer2).getOwnerPlayerNum() == 2) {
                                player.get(1).farmsDestroyLevel2();
                            }
                            if (areasqrs.get(numTer2).getOwnerPlayerNum() == 1) {
                                player.get(0).farmsDestroyLevel2();
                            }
                            player.get(2).farmsBuiltLevel2();
                        }
                        if (areasqrs.get(numTer2).getFarmlvl() == 3) {
                            if (areasqrs.get(numTer2).getOwnerPlayerNum() == 2) {
                                player.get(1).farmsDestroyLevel3();
                            }
                            if (areasqrs.get(numTer2).getOwnerPlayerNum() == 1) {
                                player.get(0).farmsDestroyLevel3();
                            }
                            player.get(2).farmsBuiltLevel3();
                        }
                        if (areasqrs.get(numTer2).getFarmlvl() == 4) {
                            if (areasqrs.get(numTer2).getOwnerPlayerNum() == 2) {
                                player.get(1).farmsDestroyLevel4();
                            }
                            if (areasqrs.get(numTer2).getOwnerPlayerNum() == 1) {
                                player.get(0).farmsDestroyLevel4();
                            }
                            player.get(2).farmsBuiltLevel4();
                        }
                        if (areasqrs.get(numTer2).getFarmlvl() == 5) {
                            if (areasqrs.get(numTer2).getOwnerPlayerNum() == 2) {
                                player.get(1).farmsDestroyLevel5();
                            }
                            if (areasqrs.get(numTer2).getOwnerPlayerNum() == 1) {
                                player.get(0).farmsDestroyLevel5();
                            }
                            player.get(2).farmsBuiltLevel5();
                        }

                        areasqrs.get(numTer2).setOwnerPlayer(GameObject.OwnerPlayer.player3);
                    }
                    //spread the troops onto the territories
                    if (troopsResult == 1 || areasqrs.get(numTer1).getSoilders() > 0) {
                        System.out.println("troopsResult " + troopsResult);
                        areasqrs.get(numTer2).setSoilders(troopsResult);
                    } else if (troopsResult > 1 && areasqrs.get(numTer1).getSoilders() == 0) {
                        System.out.println("troopsResult " + troopsResult);
                        areasqrs.get(numTer1).setSoilders(1);
                        areasqrs.get(numTer2).setSoilders(troopsResult - 1);
                    }
                }

                if (winnerPlayer2) {
                    troopsResult = numbTroopsFromTer2;
                    System.out.println("troopsResult " + troopsResult);
                    areasqrs.get(numTer2).setSoilders(troopsResult);
                }
            }

            areasqrs.get(numTer2).setTroopsChosen(0);
            troopsChosenTer1 = 0;

            //System.out.println("troopsResult "+troopsResult);
            //areasqrs.get(numTer2).setSoilders(troopsResult);

            //deselect territories
            territory1Deselected = true;

            buttonDeployAttack = false;
        }
    }

    public void onTouchTer2Select(int i) {
        switch (numTer1) {
            case 0: {
                if (i == 4 || i == 7) {
                    territory2Selected = true;
                    numTer2 = i;
                    System.out.println("eeeeeeeeeeeeee");
                }
            }
            break;
            case 1: {
                if (i == 4 || i == 5 || i == 8) {
                    territory2Selected = true;
                    numTer2 = i;
                    System.out.println("eeeeeeeeeeeeee");
                }
            }
            break;
            case 2: {
                if (i == 5 || i == 6 || i == 9) {
                    territory2Selected = true;
                    numTer2 = i;
                    System.out.println("eeeeeeeeeeeeee");
                }
            }
            break;
            case 3: {
                if (i == 6 || i == 10) {
                    territory2Selected = true;
                    numTer2 = i;
                    System.out.println("eeeeeeeeeeeeee");
                }
            }
            break;
            case 4: {
                if (i == 0 || i == 1 || i == 7 || i == 11 || i == 8) {
                    territory2Selected = true;
                    numTer2 = i;
                    System.out.println("eeeeeeeeeeeeee");
                }
            }
            break;
            case 5: {
                if (i == 1 || i == 8 || i == 12 || i == 9 || i == 2) {
                    territory2Selected = true;
                    numTer2 = i;
                    System.out.println("eeeeeeeeeeeeee");
                }
            }
            break;
            case 6: {
                if (i == 2 || i == 9 || i == 13 || i == 10 || i == 3) {
                    territory2Selected = true;
                    numTer2 = i;
                    System.out.println("eeeeeeeeeeeeee");
                }
            }
            break;
            case 7: {
                if (i == 0 || i == 4 || i == 11 || i == 14) {
                    territory2Selected = true;
                    numTer2 = i;
                    System.out.println("eeeeeeeeeeeeee");
                }
            }
            break;
            case 8: {
                if (i == 4 || i == 1 || i == 5 || i == 11 || i == 12 || i == 15) {
                    territory2Selected = true;
                    numTer2 = i;
                    System.out.println("eeeeeeeeeeeeee");
                }
            }
            break;
            case 9: {
                if (i == 5 || i == 2 || i == 6 || i == 13 || i == 16 || i == 12) {
                    territory2Selected = true;
                    numTer2 = i;
                    System.out.println("eeeeeeeeeeeeee");
                }
            }
            break;
            case 10: {
                if (i == 3 || i == 6 || i == 13 || i == 17) {
                    territory2Selected = true;
                    numTer2 = i;
                    System.out.println("eeeeeeeeeeeeee");
                }
            }
            break;
            case 11: {
                if (i == 7 || i == 4 || i == 8 || i == 15 || i == 18 || i == 14) {
                    territory2Selected = true;
                    numTer2 = i;
                    System.out.println("eeeeeeeeeeeeee");
                }
            }
            break;
            case 12: {
                if (i == 8 || i == 5 || i == 9 || i == 16 || i == 19 || i == 15) {
                    territory2Selected = true;
                    numTer2 = i;
                    System.out.println("eeeeeeeeeeeeee");
                }
            }
            break;
            case 13: {
                if (i == 6 || i == 10 || i == 17 || i == 20 || i == 16 || i == 9) {
                    territory2Selected = true;
                    numTer2 = i;
                    System.out.println("eeeeeeeeeeeeee");
                }
            }
            break;
            case 14: {
                if (i == 7 || i == 11 || i == 18 || i == 21) {
                    territory2Selected = true;
                    numTer2 = i;
                    System.out.println("eeeeeeeeeeeeee");
                }
            }
            break;
            case 15: {
                if (i == 8 || i == 12 || i == 19 || i == 22 || i == 18 || i == 11) {
                    territory2Selected = true;
                    numTer2 = i;
                    System.out.println("eeeeeeeeeeeeee");
                }
            }
            break;
            case 16: {
                if (i == 9 || i == 13 || i == 20 || i == 23 || i == 19 || i == 12) {
                    territory2Selected = true;
                    numTer2 = i;
                    System.out.println("eeeeeeeeeeeeee");
                }
            }
            break;
            case 17: {
                if (i == 10 || i == 13 || i == 20 || i == 24) {
                    territory2Selected = true;
                    numTer2 = i;
                    System.out.println("eeeeeeeeeeeeee");
                }
            }
            break;
            case 18: {
                if (i == 21 || i == 14 || i == 11 || i == 15 || i == 22) {
                    territory2Selected = true;
                    numTer2 = i;
                    System.out.println("eeeeeeeeeeeeee");
                }
            }
            break;
            case 19: {
                if (i == 22 || i == 15 || i == 12 || i == 16 || i == 23) {
                    territory2Selected = true;
                    numTer2 = i;
                    System.out.println("eeeeeeeeeeeeee");
                }
            }
            break;
            case 20: {
                if (i == 23 || i == 16 || i == 13 || i == 17 || i == 24) {
                    territory2Selected = true;
                    numTer2 = i;
                    System.out.println("eeeeeeeeeeeeee");
                }
            }
            break;
            case 21: {
                if (i == 14 || i == 18) {
                    territory2Selected = true;
                    numTer2 = i;
                    System.out.println("eeeeeeeeeeeeee");
                }
            }
            break;
            case 22: {
                if (i == 18 || i == 15 || i == 19) {
                    territory2Selected = true;
                    numTer2 = i;
                    System.out.println("eeeeeeeeeeeeee");
                }
            }
            break;
            case 23: {
                if (i == 19 || i == 16 || i == 20) {
                    territory2Selected = true;
                    numTer2 = i;
                    System.out.println("eeeeeeeeeeeeee");
                }
            }
            break;
            case 24: {
                if (i == 20 || i == 17) {
                    territory2Selected = true;
                    numTer2 = i;
                    System.out.println("eeeeeeeeeeeeee");
                }
            }
            break;
            default:
                break;
        }


    /*
    int distXBetweenBlocks = areasqrs.get(numTer1).getX() - areasqrs.get(i).getX();
    int distYBetweenBlocks = areasqrs.get(numTer1).getY() - areasqrs.get(i).getY();
    if (distXBetweenBlocks >= -100 && distXBetweenBlocks <= 100 && distYBetweenBlocks >= -100 && distYBetweenBlocks <= 100) {
        territory2Selected = true;
        numTer2 = i;

    }
    */
    }

}