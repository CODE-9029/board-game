package marupeke;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MarupekeGUI extends Application
{
    protected static final int tileSize = 60;
    protected static final int spacing = 0;
    protected static final int Width = 1000;
    protected static final int Height = 800;
    protected static final boolean changeSize = false;
    protected static final String Title = "Welcome to play Marupeke. You can change its state by clicking on the editable square(Those white ones)";
    protected static final String background = "#E6E6FA";
    public static final Image imageX = new Image("file:images/x.png");
    public static final Image imageO = new Image("file:images/o.png");
    public static final Image imageSOLID = new Image("file:images/solid.png");
    public static final Image imageBLANK = new Image("file:images/blank.png");
    public static ComboBox<Difficulty> difficulty = new ComboBox<>();
    public static Marupeke marupeke;
    public static BorderPane root = new BorderPane();
    public static TextField sizeTips = new TextField("Size: ");
    public static TextField gameSize = new TextField("3");
    public static GridPane gameGrid = new GridPane();
    public static Button createNew = new Button("New Game");
    public static Button check = new Button("    Check    ");
    public static HBox topBorder = new HBox();
    public static VBox side = new VBox();

    public static void main(String[] args) { launch(args); }
    
    @Override
    public void start(Stage window)
    {
        difficulty.getItems().addAll(Difficulty.Easy, Difficulty.Hard);
        difficulty.getSelectionModel().selectFirst();

        createNew.setOnAction(e ->
        {   
            try
            {
                gameGrid = createGrid();
            }
            catch(NumberFormatException exception)
            {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("illegalGridSize");
                alert.setHeaderText(null);
                alert.setContentText("illegalGridSize! \nPlease enter a number between 3 and 10.");
                alert.showAndWait();
                System.exit(0);
            }
        });
        
        check.setOnAction(e ->
        {
            checkGrid();
        });

        sizeTips.setEditable(false);
        topBorder.getChildren().add(sizeTips);
        topBorder.getChildren().add(gameSize);
        topBorder.setSpacing(3);
        side.getChildren().add(createNew);
        side.getChildren().add(check);
        root.setRight(side);
        root.setLeft(difficulty);
        gameGrid = createGrid();
        root.setTop(topBorder);

        Scene scene = new Scene(root, Width, Height);
        window.setTitle(Title);
        window.setScene(scene);
        window.setResizable(changeSize);
        window.show();
    }
    
    private GridPane createGrid()
    {
        int gameSizeNum = Integer.parseInt(gameSize.getText());
        
        if(gameSizeNum < 3 || gameSizeNum > 10)
        {   
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("illegalGridSize");
            alert.setHeaderText(null);
            alert.setContentText("illegalGridSize! \nThe size you entered is invalid. \nPlease enter a number between 3 and 10.");
            alert.showAndWait();
            System.exit(0);
            
            return null;
        }

        if(gameSizeNum > 3 && gameSizeNum <= 10)
        {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("illegalGridSize");
            alert.setHeaderText(null);
            alert.setContentText("Only the size of 3 can ensure that the puzzle can be completed," +
                    "if the size is greater than 3,\n the program to ensure that it is legal and completeable is likely to be executed for too long,\n" +
                    "so if the size is greater than 3, it will not be guaranteed to be completed");
            alert.showAndWait();
        }
        
        marupeke = Marupeke.randomPuzzle(gameSizeNum, difficulty.getValue());
        GridPane gamePane = gameToGrid();
        
        paneSetup(gamePane);
        root.setCenter(gamePane);
        
        return gamePane;
    }
    
    private GridPane gameToGrid()
    {
        GridPane grid = new GridPane();
        
        for(int i = 0; i < marupeke.getGrid().length; i++)
        {
            for(int j = 0; j < marupeke.getGrid()[0].length; j++)
            {
                TileGUI tileGUI = new TileGUI(marupeke.getGrid()[i][j].isEditable, marupeke.getGrid()[i][j].state, i, j);
                grid.add(tileGUI.button, j, i);         
            }
        }
        
        return grid;
    }
    
    private void paneSetup(GridPane pane)
    {
        pane.setHgap(spacing);
        pane.setVgap(spacing);
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-background-color:" + background + ";");
    }
    
    private boolean checkGrid()
    {
        boolean isComplete = marupeke.isPuzzleComplete();

        // Show alert on screen when the user wants to check the grid
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle((isComplete) ? "Well done!" : "Try again!");
        alert.setHeaderText(null);
        alert.setContentText((isComplete) ? "Good job, you successfully solved the puzzle!" : "Sorry, this is not solved yet.");
        alert.showAndWait();

        if(isComplete) createGrid();

        return isComplete;
    }
}
