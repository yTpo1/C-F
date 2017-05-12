package com.example.nickghost.strategy1;

public class Player extends GameObject {

    private int turnWas;

    public Player(){
        resourceFood = 0;
        turn = 1;
        endTurnCollectFood = false;
        farmsBuiltLevel1 = 0;
        farmsBuiltLevel2 = 0;
        farmsBuiltLevel3 = 0;
        farmsBuiltLevel4 = 0;
        farmsBuiltLevel5 = 0;
        troopsBought = false;

        foodCollected = false;
        turnWas = 0;
    }

    public void update(){

        if(endTurnCollectFood){
                for (int j = 0; j < farmsBuiltLevel1; j++) {
                    System.out.println("resourceFood++");
                    resourceFood++;
                }
                for (int q = 0; q < farmsBuiltLevel2; q++) {
                    System.out.println("resourceFood++2");
                    resourceFood += 2;
                }
                for (int w = 0; w < farmsBuiltLevel3; w++) {
                    System.out.println("resourceFood++3");
                    resourceFood += 3;
                }
                for (int q = 0; q < farmsBuiltLevel4; q++) {
                    System.out.println("resourceFood++4");
                    resourceFood += 4;
                }
                for (int e = 0; e < farmsBuiltLevel5; e++) {
                    System.out.println("resourceFood++5");
                    resourceFood += 5;
                }


            System.out.println("resourceFood: "+resourceFood);
            //}
            //System.out.println("resourceFood: "+resourceFood);
            foodCollected = true;
            turnWas = turn;
            endTurnCollectFood = false;
        }

        if(turnWas!=turn){
            foodCollected = false;
        }

        if(troopsBought){
            resourceFood--;

            troopsBought = false;
        }

        /*
        if built troop: -1 food;
         */
    }

}
