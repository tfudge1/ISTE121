import java.util.ArrayList;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.awt.event.KeyListener;
import javafx.scene.input.KeyEvent;

public class Play extends VBox{
    private TextArea txtArea = new TextArea();
    private TextField inputArea = new TextField();
    public void Wait(ArrayList<String> words, ArrayList<String> players){
        for(int i =0; i < players.size(); i ++){
            //this.getChildren().addAll(players.get(i));
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
    public void validateText(String typed){
            //if string typed = (what its supposed to ){ }
        }
    }

