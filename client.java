//package  sample;
import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.*;
import java.io.*; 
import java.net.*;
import java.util.ArrayList;
import java.awt.event.KeyListener;
import java.util.Date;

import javafx.scene.input.KeyEvent;

import javafx.application.Platform;

public class client extends Application implements EventHandler<ActionEvent> {
    private serverCommunicate SC;
    private Stage stage;
    private Scene scene;
    private VBox root = new VBox(8);
    private String sampletext = "This is some text that you will need to type out and have the program valadate then you will be scored against other players in this fun little game that we have made for out class and did not use any lorem ipsum for because we are cool like that and please give us an A";

    private Connect connectGUI = new Connect();
    private Scene connectScene = new Scene(connectGUI,230,300);

    private Wait waitGUI = new Wait();
    private Scene waitScene = new Scene(waitGUI,300,300);

    private Play playGUI = new Play();
    private Scene playScene = new Scene(playGUI,300,300);

    private WinnerScreen winnerGUI =new WinnerScreen();
    private Scene winnerScene = new Scene(winnerGUI,300,300);

    private String myColor;

    public static void main(String[] args) {
        launch(args);
    }

    // Called automatically after launch sets up javaFX
    public void start(Stage _stage) throws Exception {
        stage = _stage;
        stage.setTitle("Racers");
        connectGUI.getConnect().setOnAction(this);
        connectGUI.getYellow().setOnAction(this);
        connectGUI.getBlue().setOnAction(this);
        connectGUI.getGreen().setOnAction(this);
        connectGUI.getPurple().setOnAction(this);
        connectGUI.getPink().setOnAction(this);
        connectGUI.getRed().setOnAction(this);
        connectGUI.getOrange().setOnAction(this);
        waitGUI.getStart().setOnAction(this);
        //playGUI.getInputArea().addKeyListener(listener);
      
        scene = new Scene(root, 500, 300);
        stage.setScene(connectScene);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                SC.sendDisconnect();
            }
        });
        stage.show();

    }

   public void handle(ActionEvent evt) {
      // Get the button that was clicked
      Button btn = (Button)evt.getSource();
      
      // Switch on its name
      switch(btn.getText()) {
        case "Connect":
            SC = new serverCommunicate(connectGUI.getIP().getText());
            SC.initCar(connectGUI.getName().getText(),myColor);
            SC.start();
            stage.setScene(waitScene);
            waitGUI.addPlayer(SC.getThisCarName(), SC.getThisCarColor());
            break;
        case "Ready":
            System.out.println("ready");
            //SC.isReady();
            break;
        case "Yellow":
            connectGUI.offColors();
            myColor = "#fdffb6";
            break;
        case "Blue":
            connectGUI.offColors();
            myColor = "#90dbf4";
            break;
        case "Green":
            connectGUI.offColors();
            myColor = "#b9fbc0";
            break;
        case "Purple":
            connectGUI.offColors();
            myColor = "#cfbaf0";
            break;
        case "Pink":
            connectGUI.offColors();
            myColor = "#f1c0e8";
            break;
        case "Red":
            connectGUI.offColors();
            myColor = "#ffadad";
            break;
        case "Orange":
            connectGUI.offColors();
            myColor = "#ffd6a5";
            break;
        case "Start":
            SC.sendRequest();
            break;
    }
}
   public class serverCommunicate extends Thread{
        public String currentDIR;
        public DataInputStream dis;
        public DataOutputStream dos;
        public boolean Disconnect = false;
        private Socket socket;
        public int correctChar = 0;
        public Car thisCar;
        public ArrayList<Car> otherPlayers = new ArrayList<Car>();
        private Date startTime;
        private int sentenceLength;
        public serverCommunicate(String _address){//constructor makes new socket
            try{
                socket = new Socket(_address,12345);
                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());
            }catch(Exception ex){ }
            System.out.println("Thread started");
        }
        public void initCar(String name, String color){//makes car obj from connect fields
            thisCar = new Car(name, color, 0);
        }
        public String getThisCarName(){//used to pass thisCar's name
            return thisCar.getName();
        }
        public String getThisCarColor(){//used to pass thisCar's color
            return thisCar.getColor();
        }
        public void run(){
            try{
                dos.writeUTF("READY&WAITING");
                writeCar();
                int otherRacers = dis.readInt();
                //read other clients
                for(int i = 0; i < otherRacers-1; i++){
                    addRacer();
                }
                while(true){
                    //dis.readUTF();
                    String serverAction = dis.readUTF();
                    System.out.println("Incoming Action: " + serverAction);
                    if(serverAction.equals("START")){
                        String sentence = dis.readUTF();
                        startTime = new Date();
                        sentenceLength = sentence.split(" ").length;
                        System.out.println("Sentence: " + sentence);
                        System.out.println("Length: " + sentenceLength);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                playGUI.setSentences(sentence);
                                playGUI.addCars(otherPlayers);
                                playGUI.addCars(thisCar);
                                playGUI.addServer(SC);
                                playGUI.setCurrentWord(thisCar.getWordCount());
                                stage.setScene(playScene);
                            }
                        });
                        dos.writeUTF("STARTED");
                        dos.flush();
                        System.out.println("Started Race!");
                    }else if (serverAction.equals("REFRESH")){
                        String carID = dis.readUTF();
                        int wordCount = dis.readInt();
                        updateRacer(carID, wordCount);
                    }else if(serverAction.equals("ADDPLAYER")){
                        addRacer();
                    }else if(serverAction.equals("WPM")){
                        String carID = dis.readUTF();
                        int wpm = dis.readInt();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                playGUI.addWordsPerMinute(carID, wpm);
                            }
                        });
                    }else if(serverAction.equals("END")){
                        endGame();
                    }else if(serverAction.equals("REMOVEPLAYER")){
                        removeRacer();
                    }
                }
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        public void endGame(){
            try{
                int numCars = dis.readInt();
                ArrayList<Car> placement = new ArrayList<Car>();
                for(int i = 0; i < numCars; i++){
                    String ID = dis.readUTF();
                    if(ID.equals(thisCar.getID())){
                        placement.add(thisCar);
                    }else{
                        for(Car c : otherPlayers){
                            if(c.getID().equals(ID)){
                                placement.add(c);
                            }
                        }
                    }
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        winnerGUI.addWinner(placement);
                        stage.setScene(winnerScene);
                    }
                });
            } catch (IOException _e) {
                _e.printStackTrace();
            }
        }
        public void updateRacer(String UID, int WordCount){
            //find what racer it is with UID, and update their wordcount
            for(Car c: otherPlayers){
                if(c.IDis(UID)){
                    c.setWordCount(WordCount);
                    //update play GUI
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            playGUI.updateProgressBar(UID, WordCount);
                        }
                    });
                }
            }
            
        }
        public void sendUpdate(int WordCount){
            try{
                dos.writeUTF("UPDATE");
                //dos.writeUTF(thisCar.getID());
                dos.writeInt(WordCount);
                dos.flush();
                if(WordCount == sentenceLength){
                    //dos.writeUTF("FINISHED");
                    finish();
                }
            } catch (IOException _e) {
                _e.printStackTrace();
            }
        }
        public void sendRequest(){
            try{
                dos.writeUTF("STARTREQUEST");
                dos.flush();
                //boolean success= dis.readBoolean();

            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        public void writeCar(){
            try{
                System.out.println("writing car");
                dos.writeUTF(thisCar.getName());
                dos.writeUTF(thisCar.getColor());
                dos.writeInt(thisCar.getWordCount());
                dos.writeUTF(thisCar.getID());
                dos.flush();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        public void addRacer(){
            try{
                //get new car
                String name = dis.readUTF();
                System.out.println("name: " + name);
                String color = dis.readUTF();
                System.out.println("color: " + color);
                String id = dis.readUTF();
                System.out.println("id: " + id);
                String isGM = dis.readUTF();
                if(isGM.equals(id)){
                    waitGUI.setGMas(name);
                }
                

                int wordCount = dis.readInt();
                System.out.println("wordcount: " + wordCount);
                Car c = new Car(name,color,wordCount,id);

                otherPlayers.add(c);
                //add car to GUI
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        waitGUI.addPlayer(c.getName(), c.getColor());
                    }
                });
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        public void finish(){
            Date endTime = new Date();
            long time = endTime.getTime() - startTime.getTime();
            System.out.println("Time: " + time+ " of " + sentenceLength);
            int wpm = (int) Math.round(sentenceLength/(time/60000.0));
            System.out.println("Words per minute: " + wpm);
            playGUI.addWordsPerMinute(thisCar.getID(),wpm);
            try{
                dos.writeUTF("FINISHED");
                dos.writeInt(wpm);
                dos.flush();
            }catch (IOException _e){
                _e.printStackTrace();
            }
        }
        public boolean checkConnect(){
            return socket.isConnected();
        }
        public void removeRacer(){
            try{
                String id = dis.readUTF();
                otherPlayers.removeIf(c -> c.IDis(id));
            }catch (IOException _e){
                _e.printStackTrace();
            }
        }
        public void sendDisconnect(){
            try{
                dos.writeUTF("CLOSE");
                dos.flush();
                stage.setScene(connectScene);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }


    }
}
