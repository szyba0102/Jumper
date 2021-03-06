package agh.ics.oop;

import agh.ics.oop.pawns.AbstractPawn;
import agh.ics.oop.pawns.BlackPawn;
import agh.ics.oop.pawns.WhitePawn;
import java.util.HashMap;

public class Board {
    private final HashMap<Vector2d, AbstractPawn> pawnsHashMap = new HashMap<Vector2d,AbstractPawn>(); //hashmap that contains pawns locations on board

    public Board(){
        createBoard();
    }

    // adding pawns at the beginning of the game (to the top and the bottom of board)
    public void createBoard(){
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

    // changing position of a pawn
    public void changeOfPositionOfPawn(AbstractPawn pawn, Vector2d  newPosition){
        pawnsHashMap.remove(pawn.getPosition());
        pawn.move(newPosition);
        pawnsHashMap.put(newPosition,pawn);
    }

    // checking if chosen move by player is correct
    // function returns 1 when move is correct and player can choose another move, 0 is for correct move but last possible and -1 is for wrong move
    public int checkMove(Vector2d lastPosition, Vector2d nextPosition){
        if(pawnsHashMap.containsKey(nextPosition)){
            return -1;
        }
        if(lastPosition.subtract(nextPosition).sumCoords() == 1 && pawnsHashMap.containsKey(lastPosition)){
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

    public AbstractPawn objectAt(Vector2d vector2d){
        return pawnsHashMap.get(vector2d);
    }
    public boolean isOccupied(Vector2d vector2d) {
        return pawnsHashMap.containsKey(vector2d);
    }

}
