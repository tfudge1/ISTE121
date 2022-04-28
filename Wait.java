//package sample;
import java.util.concurrent.Flow;

import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Wait extends VBox{
    private Label gm = new Label("Game master: ");
    private TextField gmName = new TextField();
    private Separator separator = new Separator();
    private Label waitForPlayers = new Label("Please wait for players ");
    private Button start = new Button("start");
    private FlowPane pgbarHolder = new FlowPane();
    private ProgressBar pgbar = new ProgressBar();
    private Separator separator2 = new Separator();
    private Label inLobby = new Label("In Lobby:");
    public Wait(){
        pgbarHolder.getChildren().addAll(pgbar);
        pgbar.setMinWidth(250);
        this.getChildren().addAll(gm,gmName,separator,waitForPlayers,start,pgbarHolder,separator2,inLobby);
        gmName.setEditable(false);
    }
    public void isGM(){
        start.setVisible(true);
        pgbarHolder.setVisible(false);
    }
    public void isPlayer(){
        start.setVisible(false);
        pgbarHolder.setVisible(true);
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
        return start;
    }
    public void setGameMaster(String name){
        gmName.setText(name);
    }
}
