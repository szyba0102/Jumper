package agh.ics.oop.Pawns;

import agh.ics.oop.Board;
import agh.ics.oop.Vector2d;

public class WhitePawn extends AbstractPawn{

    public WhitePawn(Vector2d position, Board board) {
        super(position,board);
    }

    @Override
    public String getPictureFilePath() {
        return "src/main/resources/whitepawn.png";
    }

    @Override
    public String getChosenPictureFilePath() {
        return "src/main/resources/chosenwhitepawn.png";
    }

    @Override
    public int getPlayerNumber() {
        return 1;
    }


}
