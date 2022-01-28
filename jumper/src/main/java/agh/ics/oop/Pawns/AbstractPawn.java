package agh.ics.oop.Pawns;

import agh.ics.oop.Board;
import agh.ics.oop.Vector2d;

public abstract class AbstractPawn {
    //protected String PhotoFilePath;
    protected Vector2d position;
    protected Board board;


    public AbstractPawn(Vector2d position, Board board){
        this.position = position;
        this.board = board;
    }

    public void move(Vector2d vector2d){
        position = vector2d;
    }

    public Vector2d getPosition(){
        return position;
    }

    public abstract String getPictureFilePath();
    public abstract String getChosenPictureFilePath();
    public abstract int getPlayerNumber();



}
