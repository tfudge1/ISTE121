import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Connect extends VBox{
    private TextField IP = new TextField("IP");
    private Button connect = new Button("Connect");
    private Button yellow = new Button("yellow");
    private Button blue = new Button("blue");
    private Button green = new Button("green");
    private Button purple = new Button("purple");
    private Button pink = new Button("pink");
    private Button red = new Button("red");
    private Button black = new Button("black");
    private Button ornage = new Button("ornage");
    private HBox HboxN1 = new HBox(10);
    private VBox VboxN1 = new VBox(10);
    private VBox VboxN2 = new VBox(10);
    public Connect(){
        yellow.setMaxWidth(100);
        blue.setMaxWidth(100);
        green.setMaxWidth(100);
        purple.setMaxWidth(100);
        pink.setMaxWidth(100);
        red.setMaxWidth(100);
        black.setMaxWidth(100);
        ornage.setMaxWidth(100);
        IP.setMaxWidth(200);
        connect.setMaxWidth(150);
        VboxN1.getChildren().addAll(yellow,blue,green,purple);
        VboxN2.getChildren().addAll(pink,red,black,ornage);
        HboxN1.getChildren().addAll(VboxN1,VboxN2);
        this.setAlignment(Pos.TOP_CENTER);
        HboxN1.setAlignment(Pos.TOP_CENTER);
        this.getChildren().addAll(IP,connect,HboxN1);
    }
    public Button getConnect(){
        return connect;
    }
    public Button getYellow(){
        return yellow;
    }
    public Button getBlue(){
        return blue;
    }
    public Button getGreen(){
        return green;
    }
    public Button getPurple(){
        return purple;
    }
    public Button getPink(){
        return pink;
    }
    public Button getRed(){
        return red;
    }
    public Button getBlack(){
        return black;
    }
    public Button getOrange(){
        return ornage;
    }
}
