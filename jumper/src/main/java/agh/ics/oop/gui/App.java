package agh.ics.oop.gui;
import agh.ics.oop.*;
import agh.ics.oop.pawns.AbstractPawn;
import agh.ics.oop.pawns.WhitePawn;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    private final GridPane gridPane = new GridPane();
    private final Board board = new Board();
    private AbstractPawn chosenPawn = null;
    private final List<Vector2d> pawnMoveSequence = new ArrayList<>();
    private boolean extraMove = true;
    private final Text text = new Text();
    private int currentPlayer = 1;

    @Override
    public void init(){

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        createMap();
        Button buttonStart = new Button("Start new game");
        /*buttonStart.setOnAction((event) ->{
            createMap();
        });*/
        Button buttonMove = new Button("Make move");
        buttonMove.setOnAction((event) -> {
            if(chosenPawn == null){
                text.setText("Chose pawn and sequence of move first!");
            }
            else if(pawnMoveSequence.size() == 0){
                text.setText("Chose move!");
            }
            else{
            board.changeOfPositionOfPawn(chosenPawn,pawnMoveSequence.get(pawnMoveSequence.size()-1));
            if(checkIfPlayerWon()){
                Text won = new Text("PLAYER " + currentPlayer + " WON!!");
                //won.set
                VBox vbox = new VBox();
                vbox.getChildren().add(won);
                Scene scene = new Scene(vbox,800, 900);
                primaryStage.setScene(scene);

                primaryStage.show();
            }
            chosenPawn = null;
            pawnMoveSequence.clear();
            extraMove = true;
            currentPlayer = (currentPlayer % 2) + 1;
            createMap();
            text.setText("Player " + currentPlayer);
            }
        });
        Button clearButtton = new Button("clear chosen sequence");
        clearButtton.setOnAction((event) -> {
            chosenPawn = null;
            pawnMoveSequence.clear();
            extraMove = true;
            createMap();
        });
        HBox hbox = new HBox();
        hbox.setSpacing(40);
        buttonMove.setPadding(new Insets(20,20,20,20));
        clearButtton.setPadding(new Insets(20,20,20,20));
        text.setText("Player 1");
        hbox.getChildren().addAll(buttonMove,clearButtton,text);
        hbox.setPadding(new Insets(20,40,20,40));
        hbox.setAlignment(Pos.CENTER);
        VBox vbox = new VBox();
        vbox.getChildren().addAll(gridPane,hbox);
        vbox.setAlignment(Pos.CENTER);


        Scene scene = new Scene(vbox,800, 900);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public void updateChosenPawn(AbstractPawn abstractPawn,Vector2d positionBoard) throws FileNotFoundException {
        if(abstractPawn.getPlayerNumber() != currentPlayer){
            text.setText("Its player " + currentPlayer + " turn!");
        }
        else {
            createMap();
            System.out.println(abstractPawn.getPosition());
            pawnMoveSequence.clear();
            extraMove = true;
            pawnMoveSequence.add(abstractPawn.getPosition());
            chosenPawn = abstractPawn;
            Image image = null;
            try {
                image = new Image(new FileInputStream(abstractPawn.getChosenPictureFilePath()));
            } catch (FileNotFoundException e) {
                System.out.println("File doesnt exists");
            }
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(90);
            imageView.setFitHeight(90);
            GridPane.setConstraints(imageView, positionBoard.getX(), positionBoard.getY());
            GridPane.setHalignment(imageView, HPos.CENTER);
            gridPane.add(imageView, positionBoard.getX(), positionBoard.getY());
        }
    }

    public void updateBoard(Vector2d newPosition,Vector2d positionBoard){
        if(chosenPawn == null){
            text.setText("Chose pawn first");
        }
        else if(!extraMove){
            text.setText("Cant make another move");
        }
        else{
            int length = pawnMoveSequence.size();

            if(board.checkMove(pawnMoveSequence.get(length-1),newPosition) == 1){
                pawnMoveSequence.add(newPosition);
                Image image = null;
                try {
                    image = new Image(new FileInputStream("src/main/resources/chosen.png"));
                } catch (FileNotFoundException e) {
                    System.out.println("File doesnt exists");
                }
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(90);
                imageView.setFitHeight(90);
                GridPane.setConstraints(imageView, positionBoard.getX(),positionBoard.getY());
                GridPane.setHalignment(imageView, HPos.CENTER);
                gridPane.add(imageView,positionBoard.getX(),positionBoard.getY());
            }
            else if(board.checkMove(pawnMoveSequence.get(length-1),newPosition) == 0){
                pawnMoveSequence.add(newPosition);
                Image image = null;
                try {
                    image = new Image(new FileInputStream("src/main/resources/chosen.png"));
                } catch (FileNotFoundException e) {
                    System.out.println("File doesnt exists");
                }
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(90);
                imageView.setFitHeight(90);
                GridPane.setConstraints(imageView, positionBoard.getX(),positionBoard.getY());
                GridPane.setHalignment(imageView, HPos.CENTER);
                gridPane.add(imageView,positionBoard.getX(),positionBoard.getY());
                extraMove = false;
            }
            else{
                text.setText("Wrong move");
            }
        }
    }

    public void createMap() {
        gridPane.setGridLinesVisible(false);
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getChildren().clear();
        gridPane.setGridLinesVisible(true);

        gridPane.setPadding(new Insets(20, 20, 20, 20));

        for(int i=0; i<9; i++){
            ColumnConstraints columnConstraints = new ColumnConstraints(800/9); // width in pixels
            //columnConstraints.setPercentWidth(100.0 / 8); // percentage of total width
            gridPane.getColumnConstraints().add(columnConstraints);
        }

        for(int i=0; i<9; i++){
            RowConstraints rowConstraints = new RowConstraints(800/9);
            //rowConstraints.setPercentHeight(100.0 / 8);
            gridPane.getRowConstraints().add(rowConstraints);
        }


        for(int i = 0; i<9 ; i++){
            for(int j=0; j<9;j ++){
                String text = null;
                if(i==0 && j==0){
                    text = "x/y";
                }
                else if(i==0){
                    text = String.valueOf(9-j);
                }
                else if(j ==0){
                    text = String.valueOf(i);
                }
                else{
                    if(board.isOccupied(new Vector2d(i,9-j))){
                        AbstractPawn abstractPawn = board.ObjectAt(new Vector2d(i,9-j));
                        Image image = null;
                        try {
                            image = new Image(new FileInputStream(abstractPawn.getPictureFilePath()));
                        } catch (FileNotFoundException e) {
                            System.out.println("File doesnt exists");
                        }
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(90);
                        imageView.setFitHeight(90);
                        Vector2d positionBoard = new Vector2d(i,j);
                        imageView.setOnMouseClicked(event -> {
                            try {
                                updateChosenPawn(abstractPawn,positionBoard);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        });
                        GridPane.setConstraints(imageView, i,j);
                        GridPane.setHalignment(imageView, HPos.CENTER);
                        gridPane.add(imageView,i,j);
                    }
                    else{
                        Image image = null;
                        try {
                            image = new Image(new FileInputStream("src/main/resources/background.png"));
                        } catch (FileNotFoundException e) {
                            System.out.println("File doesnt exists");
                        }
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(90);
                        imageView.setFitHeight(90);
                        Vector2d position = new Vector2d(i,9-j);
                        Vector2d positionBoard = new Vector2d(i,j);
                        imageView.setOnMouseClicked(event -> updateBoard(position,positionBoard));
                        GridPane.setConstraints(imageView, i,j);
                        GridPane.setHalignment(imageView, HPos.CENTER);
                        gridPane.add(imageView,i,j);
                    }
                }
                Label label = new Label(text);
                GridPane.setConstraints(label, i,j);
                GridPane.setHalignment(label, HPos.CENTER);
                gridPane.add(label,i,j);
            }
        }
    }

    public boolean checkIfPlayerWon(){
        if(currentPlayer == 1){
            for(int i=1; i<9; i++){
                for(int j=7; j<9; j++){
                    if(!board.isOccupied(new Vector2d(i,9-j))){
                        return false;
                    }
                }
            }
        }
        else{
            for(int i=1; i<9; i++){
                for(int j=1; j<3; j++){
                    if(!board.isOccupied(new Vector2d(i,9-j))){
                        return false;
                    }
                }
            }

        }

        return true;
    }



}

