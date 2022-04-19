import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Play extends VBox{
    private TextArea txtArea = new TextArea();
    private TextField inputArea = new TextField();
    public Wait(ArrayList words, ArrayList players){
        for(int i =0; i < players.size(); i ++){
            this.getChildren().addAll(players.get(i));
        }
        this.getChildren().addAll(txtArea);
        for(String word: words){
            txtArea.appendText(word);
        }
        this.getChildren().addAll(inputArea);
    }
    public void addPlayer(String name, String color){
        playerGUI player = new playerGUI(name,color);
        this.getChildren().addAll(player);
    }
    public TextField getInputArea(){
        return inputArea;
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