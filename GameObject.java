package com.example.nickghost.strategy1;


public abstract class GameObject {
    protected int x;
    protected int y;

    protected int width;
    protected int height;
    protected boolean isSelected;
    protected boolean isSelected1st;
    protected boolean isSelected2nd;
    protected boolean nothingBuilt;
    protected boolean farm;
    protected boolean castle;
    protected int soilders;
    protected int farmlvl;
    protected int troopsChosen;

    protected enum OwnerPlayer{none,player1,player2,player3};
    protected OwnerPlayer ownerPlayer;
    protected int ownerPlayerNum;

    protected boolean hasOwner;
    protected boolean SecondTerritoryDraw;

    protected boolean farmInConstruction;
    protected int turnsToWaitFarm;
    protected int currentTurn;

    protected boolean farmlevel1Finished;
    protected boolean farmlevel2Finished;
    protected boolean farmlevel3Finished;
    protected boolean farmlevel4Finished;
    protected boolean farmlevel5Finished;

    protected boolean ownerChangedStopConstruction;

    public void setOwnerChangedStopConstruction(){this.ownerChangedStopConstruction = true;}


    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }

    public void setIsSelected(boolean b){this.isSelected = b;}

    public boolean getIsSelected(){return isSelected;}

    public boolean getNothingBuilt(){return nothingBuilt;}

    public void setNothingBuilt(boolean b){this.nothingBuilt = b;}

    public void setFarm(boolean b){this.farm = b;}

    public boolean getFarm(){return farm;}

    public void setCastle(boolean b){this.castle = b;}

    public boolean getCastle(){return castle;}

    public void setSoilders(int a){this.soilders = a;}

    public int getSoilders(){return soilders;}


    public int getFarmlvl(){return farmlvl;}

    public void setOwnerPlayer(OwnerPlayer op){this.ownerPlayer = op;}
    public OwnerPlayer getOwnerPlayer(){return ownerPlayer;}

    public void setOwnerPlayerNum(int a){this.ownerPlayerNum = a;}
    public int getOwnerPlayerNum(){return ownerPlayerNum;}

    public boolean getHasOwner(){return hasOwner;}

    public void setIsSelected1st(boolean b){this.isSelected1st = b;}
    public boolean getIsSelected1st(){return isSelected1st;}

    public void setIsSelected2nd(boolean b){this.isSelected2nd = b;}
    public boolean getIsSelected2nd(){return isSelected2nd;}

    public int getTroopsChosen(){return troopsChosen;}
    public void setTroopsChosen(int a){this.troopsChosen = a;}

    public void setSecondTerritoryDraw(boolean b){this.SecondTerritoryDraw = b;}

    public boolean getFarmInConstruction(){return farmInConstruction;}
    public void setFarmInConstruction(boolean b){this.farmInConstruction = b;}

    public void setCurrentTurn(int a){this.currentTurn = a;}

    public boolean getfarmlevel1Finished(){return farmlevel1Finished;}
    public void setfarmlevel1Finished(boolean b){this.farmlevel1Finished = b;}
    public boolean getfarmlevel2Finished(){return farmlevel2Finished;}
    public void setfarmlevel2Finished(boolean b){this.farmlevel2Finished = b;}
    public boolean getfarmlevel3Finished(){return farmlevel3Finished;}
    public void setfarmlevel3Finished(boolean b){this.farmlevel3Finished = b;}
    public boolean getfarmlevel4Finished(){return farmlevel4Finished;}
    public void setfarmlevel4Finished(boolean b){this.farmlevel4Finished = b;}
    public boolean getfarmlevel5Finished(){return farmlevel5Finished;}
    public void setfarmlevel5Finished(boolean b){this.farmlevel5Finished = b;}

    //public int getFarmLevel(){return farmLevel;}

    //=============================================================================//
    //Player Class variables and functions
    protected int resourceFood;
    protected int turn;
    protected boolean endTurnCollectFood;
    protected int farmsBuilt;
    protected boolean troopsBought;
    protected int farmsBuiltLevel1;
    protected int farmsBuiltLevel2;
    protected int farmsBuiltLevel3;
    protected int farmsBuiltLevel4;
    protected int farmsBuiltLevel5;
    protected boolean foodCollected;

    protected boolean endTurn;
    protected boolean dontAddFood;

    public boolean dontAddFood(){return dontAddFood;}

    public void setEndTurn(boolean b){this.endTurn = b;}
    public boolean getEndTurn(){return endTurn;}


    public void setTurn(){this.turn++;}
    public int getTurn(){return turn;}

    public void setFoodCollected(boolean b){this.foodCollected = b;}
    public boolean getFoodCollected(){return foodCollected;}

    public void setFarmsBuilt(int a){this.farmsBuilt = a;}

    public void farmsBuiltLevel1(){this.farmsBuiltLevel1++;}
    public void farmsBuiltLevel2(){this.farmsBuiltLevel2++;}
    public void farmsBuiltLevel3(){this.farmsBuiltLevel3++;}
    public void farmsBuiltLevel4(){this.farmsBuiltLevel4++;}
    public void farmsBuiltLevel5(){this.farmsBuiltLevel5++;}

    public void farmsDestroyLevel1(){this.farmsBuiltLevel1--;}
    public void farmsDestroyLevel2(){this.farmsBuiltLevel2--;}
    public void farmsDestroyLevel3(){this.farmsBuiltLevel3--;}
    public void farmsDestroyLevel4(){this.farmsBuiltLevel4--;}
    public void farmsDestroyLevel5(){this.farmsBuiltLevel5--;}

    public void setFood(){this.endTurnCollectFood = true;}

    public void setTroopsBought(){this.troopsBought = true;}

    public void setResourceFood(){this.resourceFood--;}
    public int getResourceFood(){return resourceFood;}





}
