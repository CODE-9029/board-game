package marupeke;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;


public class TileGUI
{
    public Button button;
    public MarupekeTile tile;
    
    private final int x;
    private final int y;
    
    public TileGUI(boolean isEditable, State state, int x, int y)
    {
        tile = new MarupekeTile(isEditable, state);
        button = buttonSetup();
        
        this.x = x;
        this.y = y;
    }
    
    private Button buttonSetup()
    {
        ImageView newImage = new ImageView();
        Button newButton = new Button();

        switch(tile.state)
        {
            case X :       newImage.setImage(MarupekeGUI.imageX);break;
            case O :       newImage.setImage(MarupekeGUI.imageO);break;
            case SOLID :   newImage.setImage(MarupekeGUI.imageSOLID);break;
            case BLANK :   newImage.setImage(MarupekeGUI.imageBLANK);break;
        }
        
        newImage.setFitWidth(MarupekeGUI.tileSize);
        newImage.setFitHeight(MarupekeGUI.tileSize);
        newButton.setGraphic(newImage);
        
        newButton.setOnAction(e ->
        {

            if(tile.isEditable == false) return;
            
            TileGUI tileGUI = cycleButtonState();
            newButton.setGraphic(tileGUI.button.getGraphic());
            tile.state = tileGUI.tile.state;
        });
        
        return newButton;
    }
    
    private TileGUI cycleButtonState()
    {      
        State newState;
        switch(tile.state)
        {
            case X :
                newState = State.O;
                break;
            case O :
                newState = State.BLANK;
                break;
            case BLANK :
                newState = State.X;
                break;
            default :
                newState = tile.state;
                break;
        };

        TileGUI newTile = new TileGUI(true, newState, x, y);

        switch(newState)
        {
            case X :
                MarupekeGUI.marupeke.setX(x, y, true);
                if(!MarupekeGUI.marupeke.isLegal()){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("illegalGrid");
                    alert.setHeaderText(null);
                    alert.setContentText("This is illegal now!");
                    alert.showAndWait();
                }
                break;
            case O :
                MarupekeGUI.marupeke.setO(x, y, true);
                if(!MarupekeGUI.marupeke.isLegal()){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("illegalGrid");
                    alert.setHeaderText(null);
                    alert.setContentText("This is illegal now!");
                    alert.showAndWait();
                }
                break;
            case BLANK :
                MarupekeGUI.marupeke.unmark(x, y);
                if(!MarupekeGUI.marupeke.isLegal()){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("illegalGrid");
                    alert.setHeaderText(null);
                    alert.setContentText("This is illegal now!");
                    alert.showAndWait();
                }
                break;
        }
        
        return newTile;
    }
}
