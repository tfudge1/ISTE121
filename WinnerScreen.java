package sample;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class WinnerScreen extends VBox{


    public WinnerScreen(){
        this.setAlignment(Pos.CENTER);

    }
    public void addWinner(ArrayList<Car> placements){
        int index=1;
        for(Car car: placements){
            Label label = new Label(index+". " +car.getName());
            label.setStyle("-fx-text-fill: " + car.getColor() + ";");
            label.setAlignment(Pos.CENTER);
            this.getChildren().add(label);
        }
    }


}
