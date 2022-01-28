package agh.ics.oop.pawns;

import agh.ics.oop.Board;
import agh.ics.oop.Vector2d;

public class BlackPawn extends AbstractPawn{
    public BlackPawn(Vector2d position, Board board) {
        super(position,board);
    }

    @Override
    public String getPictureFilePath() {
        return "src/main/resources/blackpawn.png";
    }

    @Override
    public String getChosenPictureFilePath() {
        return "src/main/resources/chosenblackpawn.png";
    }

    @Override
    public int getPlayerNumber() {
        return 2;
    }


}
