
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Wait extends VBox{
    private HBox control = new HBox();
    private Label lblWait = new Label("WAITING...");
    private Button btnStart = new Button("Start");

    private Label gm = new Label("You are the game master, start when ready");

    private Separator separator = new Separator();
    private Label pleaseWait = new Label("Please wait for game to start");
    private Button start = new Button("Start");
    private Separator separator2 = new Separator();
    private Label inLobby = new Label("In Lobby");
    public Wait(){
        this.getChildren().addAll(gm,separator,pleaseWait,start,separator2,inLobby);
    }
    public void isPlayer(){
        start.setVisible(false);
        btnStart.setVisible(false);
        gm.setVisible(false);
        //pgbarHolder.setVisible(true);
    }
    public void setGMas(String name){
        pleaseWait.setVisible(false);
        //gmName.setText(name);
    }
    public void addPlayer(String name,String color){
        Label localLabel = new Label(name);
        localLabel.setStyle("-fx-background-color:"+color);
        
        this.getChildren().addAll(localLabel);
    }
    public Button getStart(){
        return start;
    }
}
