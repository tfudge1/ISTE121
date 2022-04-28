

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class WinnerScreen extends VBox{

    Button buttonRestart = new Button("Restart");
    public WinnerScreen(){
        this.setAlignment(Pos.CENTER);

    }
    public void addWinner(ArrayList<Car> placements){
        int index=1;
        for(Car car: placements){
            Label label = new Label(index+". " +car.getName());
            label.setStyle("-fx-background-color:  " + car.getColor() + ";-fx-font-size: 28px");
            label.setAlignment(Pos.CENTER);
            this.getChildren().add(label);
            index++;
        }
    }
    public void setGM(boolean gm){
        if(gm){


            buttonRestart .setStyle("-fx-font-size: 28px");
            this.getChildren().add( buttonRestart );

        }
    }
    public Button getRestartButton(){
        return  buttonRestart ;
    }
    public void clear(){
        this.getChildren().clear();
    }


}
