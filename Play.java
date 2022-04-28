
//package sample;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Play extends VBox{
    private final TextArea txtArea = new TextArea();
    private final TextFlow sentenceDisplay = new TextFlow();
    private final TextField inputArea = new TextField();

    private client.serverCommunicate server;
    private Text currentWord;
    private int currentWordIndex;
    private int sentenceLength;
    private HashMap<String, CarTrack> carTrackMap = new HashMap<>();
    public Play() {
        this.setSpacing(10);
        this.setStyle("-fx-background-color: #FFFFFF;");
        txtArea.setEditable(false);
        txtArea.setWrapText(true);
        inputArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.SPACE) {
                    String currentText = inputArea.getText();
                    currentText = sanitizeText(currentText);
                    if(currentText.length() > 0) {
                        validateText(currentText);
                    }else{
                        inputArea.clear();
                    }


                }
            }
        });
        this.getChildren().addAll(sentenceDisplay, inputArea);
    }
    public void addServer(client.serverCommunicate server) {
        this.server = server;
        Label lbl = new Label("Server ID: " + server.thisCar.getID());
        this.getChildren().add(lbl);
    }
    public void setSentences(String sentence) {
        //txtArea.setText(sentence);
        String[] words = sentence.split(" ");
        sentenceLength = words.length;
        for (String word : words) {
            sentenceDisplay.getChildren().add(new Text(word + " "));
        }
        setCurrentWord(0);
    }
    public void setCurrentWord(int index){
        if(index < sentenceLength) {
            currentWordIndex = index;
            currentWord = (Text) sentenceDisplay.getChildren().get(index);
            currentWord.setStyle("-fx-fill: #FF0000;");
        }else if(index == sentenceLength){
            currentWordIndex = index;
            //TODO: End of sentence
        }
    }
    public void addCars(Car car) {
        CarTrack carTrack = new CarTrack(car.getName(), car.getColor());
        carTrackMap.put(car.getID(), carTrack);
        this.getChildren().add(0,carTrack);
    }
    public void addCars(ArrayList<Car> cars) {
        for (Car car : cars) {
            CarTrack carTrack = new CarTrack(car.getName(), car.getColor());
            carTrackMap.put(car.getID(), carTrack);
            this.getChildren().add(0,carTrack);
        }

    }
    public class CarTrack extends VBox{
        private Label lblName = new Label("Player");
        private ProgressBar pgbar = new ProgressBar();
        public CarTrack (String name, String color){
            lblName.setText(name);
            lblName.setStyle("-fx-background-color:  " + color + ";-fx-font-weight: bold; -fx-padding: 5px;");
            pgbar.setMinWidth(250);
            pgbar.setStyle("-fx-accent: " + color + ";");
            pgbar.setProgress(0);
            this.getChildren().addAll(lblName,pgbar);
        }
    }
    public void updateProgressBar(String carID, int wc) {
        double progress = (double)wc / sentenceLength;
        System.out.println("PLAY GUI: " + carID + " " + wc + " " + sentenceLength + " " + progress);
        CarTrack carTrack = carTrackMap.get(carID);
        System.out.println(carTrack+" is the car track with name "+carTrack.lblName.getText() + " with pgbar " + carTrack.pgbar.getProgress());
        carTrack.pgbar.setProgress(progress);
        System.out.println("new Progess "+ carTrack.pgbar.getProgress());
    }

    public String sanitizeText(String typed){
        typed = typed.trim();
        typed = typed.replaceAll(" ", "");
        return typed;
    }
    public void validateText(String typed){
        String comparison = currentWord.getText();
        comparison =sanitizeText(comparison);
          System.out.println("Validating Text-" + typed + "-" + comparison+"-");
           if(typed.equals(comparison)){
               System.out.println("Correct");
               inputArea.clear();
               currentWord.setStyle("-fx-fill: #00FF00;");

               setCurrentWord(currentWordIndex + 1);
               updateProgressBar(server.thisCar.getID(),currentWordIndex );
               server.sendUpdate(currentWordIndex );

           }
        }
    public void addWordsPerMinute(String UID, int wpm){
        CarTrack carTrack = carTrackMap.get(UID);
        carTrack.lblName.setText(carTrack.lblName.getText() + ": Words Per Minute: " + wpm);
    }
}


