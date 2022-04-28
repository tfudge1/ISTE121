package sample;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Wait extends VBox{
    private HBox control = new HBox();
    private Label lblWait = new Label("WAITING...");
    private Button btnStart = new Button("Start");
    public Wait(){
        control.getChildren().addAll(lblWait,btnStart);
        this.getChildren().addAll(control);
    }
    public void addPlayer(String name, String color){
        playerGUI player = new playerGUI(name,color);
        this.getChildren().addAll(player);
    }
    public Button getStart(){
        return btnStart;
    }
    public class playerGUI extends HBox{
        private Label lblName = new Label("Player");
        private ProgressBar pgbar = new ProgressBar();
        public playerGUI(String name, String color){
            lblName.setText(name);
            this.getChildren().addAll(lblName,pgbar);
        }
    }
}
