package sample;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Connect extends VBox{
    private TextField IP = new TextField("Localhost");
    private TextField name = new TextField("name");
    private Button connect = new Button("Connect");
    private Button yellow = new Button("Yellow");
    private Button blue = new Button("Blue");
    private Button green = new Button("Green");
    private Button purple = new Button("Purple");
    private Button pink = new Button("Pink");
    private Button red = new Button("Red");
    private Button orange= new Button("Orange");
    private HBox HboxN1 = new HBox(10);
    private VBox VboxN1 = new VBox(10);
    private VBox VboxN2 = new VBox(10);
    public Connect(){
        yellow.setMaxWidth(100);
        yellow.setStyle("-fx-background-color: #fdffb6");
        blue.setMaxWidth(100);
        blue.setStyle("-fx-background-color:#90dbf4");
        green.setMaxWidth(100);
        green.setStyle("-fx-background-color:#b9fbc0");
        purple.setMaxWidth(100);
        purple.setStyle("-fx-background-color:#cfbaf0");
        pink.setMaxWidth(100);
        pink.setStyle("-fx-background-color:#f1c0e8");
        red.setMaxWidth(100);
        red.setStyle("-fx-background-color:#ffadad");
        orange.setMaxWidth(100);
        orange.setStyle("-fx-background-color:#ffd6a5");
        name.setMaxWidth(200);
        IP.setMaxWidth(200);
        connect.setMaxWidth(150);
        VboxN1.getChildren().addAll(yellow,blue,green,purple);
        VboxN2.getChildren().addAll(pink,red,orange);
        HboxN1.getChildren().addAll(VboxN1,VboxN2);
        this.setAlignment(Pos.TOP_CENTER);
        HboxN1.setAlignment(Pos.TOP_CENTER);
        this.getChildren().addAll(IP,name,connect,HboxN1);
        connect.setVisible(false);
    }
    public Button getConnect(){
        return connect;
    }
    public void offColors(){
        HboxN1.setVisible(false);
        connect.setVisible(true);
    }
    public void onColors(){
        HboxN1.setVisible(true);
        connect.setVisible(false);
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

    public Button getOrange(){
        return orange;
    }
    public TextField getIP(){
        return IP;
    }
    public TextField getName(){
        return name;
    }
}
