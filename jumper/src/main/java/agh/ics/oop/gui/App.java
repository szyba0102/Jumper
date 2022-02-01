package agh.ics.oop.gui;

import agh.ics.oop.*;
import agh.ics.oop.pawns.AbstractPawn;
import agh.ics.oop.pawns.BlackPawn;
import agh.ics.oop.pawns.WhitePawn;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class App extends Application {
    private final GridPane gridPane = new GridPane();
    private final Board board = new Board();
    private AbstractPawn chosenPawn = null; // variable that points on pawn chosen by player
    private final List<Vector2d> pawnMoveSequence = new ArrayList<>(); // list that contains moves chosen by player
    private boolean extraMove = true;   // variable that informs if player can choose another move
    private final Text textCurrentPlayer = new Text(); // text that informs whose turn it is
    private final Text textMove = new Text(); // text that informs if chosen move is correct
    private int currentPlayer = 1; // possibility of values of currentPlayer is 1 for white pawns and 2 for black pawns
    private final Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

    @Override
    public void start(Stage primaryStage) {
        
        // beginning setup
        gridPane.setAlignment(Pos.CENTER);
        createBoard();

        // creating button that perform chosen move
        Button buttonMove = new Button("Make move");
        buttonMove.setPadding(new Insets(20,20,20,20));
        buttonMove.setOnAction((event) -> {

            // functions that check if player chose any pawn before clicking button
            if(chosenPawn == null){
                textMove.setText("Chose pawn and sequence of move first!");
            }
            // functions that check if player chose any move before clicking button
            else if(pawnMoveSequence.size() == 1){
                textMove.setText("Chose move!");
            }
            // if moves and pawn where chosen:
            else{
                // moving pawn (changing his position)
                board.changeOfPositionOfPawn(chosenPawn,pawnMoveSequence.get(pawnMoveSequence.size()-1));

                // checking if player after that move won the game
                // if true new scene will be created with information who won
                if(checkIfPlayerWon()){
                    // creating text with information
                    Text winText = new Text("PLAYER " + currentPlayer + " WON!!!");
                    winText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
                    winText.setTextAlignment(TextAlignment.CENTER);
                    winText.setFill(Color.WHITE);

                    // creating picture (gif) that will occur on the new scene
                    Image image = null;
                    try {
                        image = new Image(new FileInputStream("src/main/resources/congratulation.gif"));
                    } catch (FileNotFoundException e) {
                        System.out.println("File doesnt exists");
                    }
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(screenBounds.getHeight()/2);
                    imageView.setFitHeight(screenBounds.getHeight()/2);

                    // creating VBox that will contain text and gif
                    VBox endBox = new VBox();
                    endBox.getChildren().addAll(winText,imageView);
                    endBox.setSpacing(30);
                    endBox.setAlignment(Pos.CENTER);
                    endBox.fillWidthProperty();

                    // creating VBox background
                    endBox.setBackground(new Background(
                            new BackgroundImage(
                                    new Image("https://starwarsblog.starwars.com/wp-content/uploads/2020/04/star-wars-backgrounds-25.jpg"),
                                    BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                                    new BackgroundPosition(Side.LEFT, 0, true, Side.BOTTOM, 0, true),
                                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, true)
                            )));

                    // creating scene
                    Scene endScene = new Scene(endBox,screenBounds.getHeight(), screenBounds.getHeight());
                    primaryStage.setScene(endScene);
                    primaryStage.show();
                }

                // if player didn't win all information about chosen pawn and moves are deleted and currentPlayer is changed
                else {
                    chosenPawn = null;
                    pawnMoveSequence.clear();
                    extraMove = true;
                    currentPlayer = (currentPlayer % 2) + 1;
                    createBoard();
                    textCurrentPlayer.setText("Player " + currentPlayer);
                    textMove.setText("Chose wisely :)!");
                }
            }
        });

        // creating box containing text and label about current player
        Label labelCurrentPlayer = new Label("YOUR TURN:");
        textCurrentPlayer.setText("Player 1 turn");
        VBox boxCurrentPlayer = new VBox();
        boxCurrentPlayer.setSpacing(10);
        boxCurrentPlayer.setAlignment(Pos.CENTER);
        boxCurrentPlayer.getChildren().addAll(labelCurrentPlayer,textCurrentPlayer);

        // creating box containing text and label about current move
        Label labelMove = new Label("MOVE:");
        textMove.setText("Chose wisely :)!");
        VBox boxMove = new VBox();
        boxMove.setSpacing(10);
        boxMove.setAlignment(Pos.CENTER);
        boxMove.getChildren().addAll(labelMove,textMove);

        // creating VBox that contains buttonMove, labels and texts
        VBox vbox = new VBox();
        vbox.setSpacing(40);
        vbox.getChildren().addAll(buttonMove,boxCurrentPlayer,boxMove);
        vbox.setPadding(new Insets(20,40,20,40));
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefWidth(screenBounds.getWidth()/4);

        // HBox that contains main VBox and gridPane representing board
        HBox hbox = new HBox();
        hbox.getChildren().addAll(vbox,gridPane);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setBackground(new Background(
                new BackgroundImage(
                        new Image("https://media.istockphoto.com/photos/concept-of-leadership-golden-king-chess-on-the-board-picture-id1321432643?b=1&k=20&m=1321432643&s=170667a&w=0&h=SjKHSlCoCEL6yBSX7qkfiPsBX_RLFxZqPVwsu3ar0jk="),
                        BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                        new BackgroundPosition(Side.LEFT, 0, true, Side.BOTTOM, 0, true),
                        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, true)
                )));

        // creating scene
        Scene gameScene = new Scene(hbox,screenBounds.getHeight(), screenBounds.getHeight());
        primaryStage.setScene(gameScene);
        primaryStage.show();
        primaryStage.setFullScreen(true);
    }

    public void updateChosenPawn(AbstractPawn abstractPawn,Vector2d positionBoard) throws FileNotFoundException {
        textMove.setText("Chose wisely :)!");
        // if player chose other players pawn:
        if(abstractPawn.getPlayerNumber() != currentPlayer) {
            textMove.setText("Its player's " + currentPlayer + " turn!");
        }
        else {
            // updating map and clearing former move sequence (after choosing pawn all information about former move is deleted)
            createBoard();
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
            // updating gridPane
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(screenBounds.getHeight()/11);
            imageView.setFitHeight(screenBounds.getHeight()/11);
            GridPane.setConstraints(imageView, positionBoard.getX(), positionBoard.getY());
            GridPane.setHalignment(imageView, HPos.CENTER);
            gridPane.add(imageView, positionBoard.getX(), positionBoard.getY());
        }
    }

    public void creatingChosenFieldImage(Vector2d positionBoard){
        // creating image and updating gridPane
        Image image = null;
        try {
            image = new Image(new FileInputStream("src/main/resources/chosen.png"));
        } catch (FileNotFoundException e) {
            System.out.println("File doesnt exists");
        }
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(screenBounds.getHeight()/11);
        imageView.setFitHeight(screenBounds.getHeight()/11);
        GridPane.setConstraints(imageView, positionBoard.getX(),positionBoard.getY());
        GridPane.setHalignment(imageView, HPos.CENTER);
        gridPane.add(imageView,positionBoard.getX(),positionBoard.getY());
    }

    public void updateBoard(Vector2d newPosition,Vector2d positionBoard){
        if(chosenPawn == null){
            textMove.setText("Chose pawn first");
        }
        else if(!extraMove){
            textMove.setText("You can't make another move");
        }
        else{
            int length = pawnMoveSequence.size();
            // checking if chosen move is correct
            if(board.checkMove(pawnMoveSequence.get(length-1),newPosition) == 1){
                pawnMoveSequence.add(newPosition);
                creatingChosenFieldImage(positionBoard);
            }
            else if(board.checkMove(pawnMoveSequence.get(length-1),newPosition) == 0){
                pawnMoveSequence.add(newPosition);
                creatingChosenFieldImage(positionBoard);
                extraMove = false;
            }
            else{
                textMove.setText("Wrong move");
            }
        }
    }

    public void createBoard() {
        gridPane.setGridLinesVisible(false);
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getChildren().clear();
        gridPane.setGridLinesVisible(true);
        gridPane.setPadding(new Insets(20, 20, 20, 20));

        for(int i=0; i<9; i++){
            ColumnConstraints columnConstraints = new ColumnConstraints(screenBounds.getHeight()/11); // width in pixels
            gridPane.getColumnConstraints().add(columnConstraints);
        }

        for(int i=0; i<9; i++){
            RowConstraints rowConstraints = new RowConstraints(screenBounds.getHeight()/11);
            gridPane.getRowConstraints().add(rowConstraints);
        }

        int numToChar = 65;
        for(int i = 0; i<9 ; i++){
            for(int j=0; j<9;j ++){
                String text = null;
                // if its right top corner nothing is added
                if(i==0 && j==0){
                    text = "";
                }
                // if it is top border numbers from 1 to 8 are added
                else if(i==0){
                    text = String.valueOf(9-j);
                }
                // if it is left border letters from A to H are added
                else if(j ==0){
                    char charIndex = (char) numToChar;
                    text = "" + charIndex + "";
                    numToChar += 1;
                }
                // if it is part of center of chessboard pawns or empty fields are added
                else{
                    // adding pawns
                    if(board.isOccupied(new Vector2d(i,9-j))){
                        AbstractPawn abstractPawn = board.objectAt(new Vector2d(i,9-j));
                        Image image = null;
                        try {
                            image = new Image(new FileInputStream(abstractPawn.getPictureFilePath()));
                        } catch (FileNotFoundException e) {
                            System.out.println("File doesnt exists");
                        }
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(screenBounds.getHeight()/11);
                        imageView.setFitHeight(screenBounds.getHeight()/11);
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
                        continue;
                    }
                    // adding empty field
                    else{
                        Image image = null;
                        try {
                            image = new Image(new FileInputStream("src/main/resources/background.png"));
                        } catch (FileNotFoundException e) {
                            System.out.println("File doesnt exists");
                        }
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(screenBounds.getHeight()/11);
                        imageView.setFitHeight(screenBounds.getHeight()/11);
                        Vector2d position = new Vector2d(i,9-j);
                        Vector2d positionBoard = new Vector2d(i,j);
                        imageView.setOnMouseClicked(event -> updateBoard(position,positionBoard));
                        GridPane.setConstraints(imageView, i,j);
                        GridPane.setHalignment(imageView, HPos.CENTER);
                        gridPane.add(imageView,i,j);
                        continue;
                    }
                }
                // adding labels
                Label label = new Label(text);
                GridPane.setConstraints(label, i,j);
                GridPane.setHalignment(label, HPos.CENTER);
                gridPane.add(label,i,j);
            }
        }
    }

    // checking if current player won the game
    public boolean checkIfPlayerWon(){
        // if current player plays with white pawns function check if two top lines are occupied by white pawns
        if(currentPlayer == 1){
            for(int i=1; i<9; i++){
                for(int j=7; j<9; j++){
                    if(!(board.objectAt(new Vector2d(i,j)) instanceof WhitePawn)){
                        return false;
                    }
                }
            }
        }
        // else if current player plays with black pawns function check if two bottom lines are occupied by black pawns
        else{
            for(int i=1; i<9; i++){
                for(int j=1; j<3; j++){
                    if(!(board.objectAt(new Vector2d(i,j)) instanceof BlackPawn)){
                        return false;
                    }
                }
            }

        }
        return true;
    }

}

