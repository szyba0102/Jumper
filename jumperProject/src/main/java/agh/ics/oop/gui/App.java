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
    private AbstractPawn chosenPawn = null;
    private final List<Vector2d> pawnMoveSequence = new ArrayList<>();
    private boolean extraMove = true;
    private final Text text = new Text();
    private int currentPlayer = 1;
    private final Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

    @Override
    public void start(Stage primaryStage) throws Exception {
        gridPane.setAlignment(Pos.CENTER);
        text.setText("Player 1 turn");
        createMap();
        Button buttonMove = new Button("Make move");
        buttonMove.setOnAction((event) -> {
            if(chosenPawn == null){
                text.setText("Chose pawn and sequence of move first!");
            }
            else if(pawnMoveSequence.size() == 1){
                text.setText("Chose move!");
            }
            else{
            board.changeOfPositionOfPawn(chosenPawn,pawnMoveSequence.get(pawnMoveSequence.size()-1));
            if(checkIfPlayerWon()){
                Text won = new Text("PLAYER " + currentPlayer + " WON!!!");
                won.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
                won.setTextAlignment(TextAlignment.CENTER);
                won.setFill(Color.WHITE);
                Image image = null;
                try {
                    image = new Image(new FileInputStream("src/main/resources/congratulation.gif"));
                } catch (FileNotFoundException e) {
                    System.out.println("File doesnt exists");
                }
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(screenBounds.getHeight()/2);
                imageView.setFitHeight(screenBounds.getHeight()/2);
                VBox vbox = new VBox();
                vbox.getChildren().addAll(won,imageView);
                vbox.setSpacing(30);
                vbox.setAlignment(Pos.CENTER);
                vbox.fillWidthProperty();
                vbox.setBackground(new Background(
                        new BackgroundImage(
                                new Image("https://starwarsblog.starwars.com/wp-content/uploads/2020/04/star-wars-backgrounds-25.jpg"),
                                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                                new BackgroundPosition(Side.LEFT, 0, true, Side.BOTTOM, 0, true),
                                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, true)
                        )));
                Scene scene = new Scene(vbox,screenBounds.getHeight(), screenBounds.getHeight());
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


        VBox vbox = new VBox();
        vbox.setSpacing(40);
        buttonMove.setPadding(new Insets(20,20,20,20));
        text.setText("Player 1");
        vbox.getChildren().addAll(buttonMove,text);
        vbox.setPadding(new Insets(20,40,20,40));
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefWidth(screenBounds.getWidth()/4);
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
        Scene gameScene = new Scene(hbox,screenBounds.getHeight(), screenBounds.getHeight());
        primaryStage.setScene(gameScene);
        primaryStage.show();
        primaryStage.setFullScreen(true);
    }

    public void updateChosenPawn(AbstractPawn abstractPawn,Vector2d positionBoard) throws FileNotFoundException {
        if(abstractPawn.getPlayerNumber() != currentPlayer){
            text.setText("Its player " + currentPlayer + " turn!");
        }
        else {
            createMap();
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
            imageView.setFitWidth(screenBounds.getHeight()/11);
            imageView.setFitHeight(screenBounds.getHeight()/11);
            GridPane.setConstraints(imageView, positionBoard.getX(), positionBoard.getY());
            GridPane.setHalignment(imageView, HPos.CENTER);
            gridPane.add(imageView, positionBoard.getX(), positionBoard.getY());
        }
    }

    public void creatingChosenFieldImage(Vector2d positionBoard){
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
            text.setText("Chose pawn first");
        }
        else if(!extraMove){
            text.setText("Cant make another move");
        }
        else{
            int length = pawnMoveSequence.size();
            System.out.println(board.checkMove(pawnMoveSequence.get(length-1),newPosition));
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
                if(i==0 && j==0){
                    text = "";
                }
                else if(i==0){
                    text = String.valueOf(9-j);
                }
                else if(j ==0){
                    char charIndex = (char) numToChar;
                    text = "" + charIndex + "";
                    numToChar += 1;
                }
                else{
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
                    }
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
                    if(!(board.objectAt(new Vector2d(i,j)) instanceof WhitePawn)){
                        return false;
                    }
                }
            }
        }
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

