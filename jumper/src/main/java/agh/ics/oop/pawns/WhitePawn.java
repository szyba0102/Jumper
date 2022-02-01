package agh.ics.oop.pawns;

import agh.ics.oop.Board;
import agh.ics.oop.Vector2d;

public class WhitePawn extends AbstractPawn{

    public WhitePawn(Vector2d position, Board board) {
        super(position,board);
    }

    // functions returning file paths to images representing pawn
    @Override
    public String getPictureFilePath() {
        return "src/main/resources/whitepawn.png";
    }

    @Override
    public String getChosenPictureFilePath() {return "src/main/resources/chosenwhitepawn.png";}

    // white pawns are always played by second player
    @Override
    public int getPlayerNumber() {return 1;}


}
