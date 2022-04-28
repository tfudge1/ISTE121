//package sample;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Wait extends VBox{
    private HBox control = new HBox();
    private Label lblWait = new Label("WAITING...");
    private Button btnStart = new Button("Start");

    private Label gm = new Label("Game master is:");
    private TextField gmName = new TextField("");
    private Separator separator = new Separator();
    private Label pleaseWait = new Label("Please wait for game to start");
    private Button start = new Button("start");
    private Separator separator2 = new Separator();
    private Label inLobby = new Label("In Lobby");
    public Wait(){
        this.getChildren().addAll(gm,gmName,separator,pleaseWait,start,separator2,inLobby);
    }
    public void isPlayer(){
        btnStart.setVisible(false);
        //pgbarHolder.setVisible(true);
    }
    public void setGMas(String name){
        gmName.setText(name);
    }
    public void addPlayer(String name,String color){
        Label localLabel = new Label(name);
        switch (color.toLowerCase()){
            case"orange":
                localLabel.setStyle("-fx-text-fill: #e68a12;");
                break;
            case"purple":
                localLabel.setStyle("-fx-text-fill: #a312e6;");
                break;
            case"black":
                localLabel.setStyle("-fx-text-fill: #000;");
                break;
            case"green":
                localLabel.setStyle("-fx-text-fill: #00ff00;");
                break;
            case"red":
                localLabel.setStyle("-fx-text-fill: #ff0000;");
                break;
            case"blue":
                localLabel.setStyle("-fx-text-fill: #0000ff;");
                break;
            case"pink":
                localLabel.setStyle("-fx-text-fill: #fa16b6;");
                break;
            case"yellow":
                localLabel.setStyle("-fx-text-fill: #fadc16;");
                break;
        }
        
        this.getChildren().addAll(localLabel);
    }
    public Button getStart(){
        return btnStart;
    }
}
