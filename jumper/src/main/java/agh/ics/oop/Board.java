package agh.ics.oop;

import agh.ics.oop.Pawns.AbstractPawn;
import agh.ics.oop.Pawns.BlackPawn;
import agh.ics.oop.Pawns.WhitePawn;

import java.util.HashMap;

public class Board {
    private final int height = 8;
    private final int width = 8;
    private HashMap<Vector2d, AbstractPawn> pawnsHashMap = new HashMap<Vector2d,AbstractPawn>();
    public Board(){
        for(int i=1; i<9; i++){
            for(int j=1; j<3; j++){
                Vector2d vec = new Vector2d(i,j);
                pawnsHashMap.put(vec,new WhitePawn(vec,this));
            }
        }

        for(int i=1; i<9; i++){
            for(int j=7; j<9; j++){
                Vector2d vec = new Vector2d(i,j);
                pawnsHashMap.put(vec,new BlackPawn(vec,this));
            }
        }
    }

    public void changeOfPositionOfPawn(AbstractPawn pawn, Vector2d  newPosition){
        pawnsHashMap.remove(pawn.getPosition());
        pawn.move(newPosition);
        pawnsHashMap.put(newPosition,pawn);
    }

    public int checkMove(Vector2d lastPosition, Vector2d nextPosition){
        if(pawnsHashMap.containsKey(nextPosition)){
            return -1;
        }
        if(nextPosition.getX() > 8 || nextPosition.getX() < 0 || nextPosition.getY() >8 || nextPosition.getY() < 0 ){
            return -1;
        }
        if(lastPosition.subtract(nextPosition).sumCoords() == 1){
            return 0;
        }
        if(lastPosition.subtract(nextPosition).sumCoords() == 2){
            Vector2d subVector = lastPosition.subtract(nextPosition);
            if(subVector.getY() == -2){
                if(pawnsHashMap.containsKey(lastPosition.add(new Vector2d(0,1)))){
                    return 1;
                }
                else return -1;
            }
            else if(subVector.getY() == 2){
                if(pawnsHashMap.containsKey(lastPosition.add(new Vector2d(0,-1)))){
                    return 1;
                }
                else return -1;
            }
            else if(subVector.getX() == -2){
                if(pawnsHashMap.containsKey(lastPosition.add(new Vector2d(1,0)))){
                    return 1;
                }
                else return -1;
            }
            else if(subVector.getX() == 2){
                if(pawnsHashMap.containsKey(lastPosition.add(new Vector2d(-1,0)))){
                    return 1;
                }
                else return -1;
            }
        }
        return -1;
    }

    public AbstractPawn ObjectAt(Vector2d vector2d){
        return pawnsHashMap.get(vector2d);
    }

    public boolean isOccupied(Vector2d vector2d) {
        return pawnsHashMap.containsKey(vector2d);
    }

}
