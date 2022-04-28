import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.*;
import java.io.*; 
import java.net.*;
import java.util.ArrayList;

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
      connectGUI.getBlack().setOnAction(this);
      connectGUI.getOrange().setOnAction(this);
      waitGUI.getStart().setOnAction(this);

      
      scene = new Scene(root, 500, 300);
        stage.setScene(connectScene);
        stage.show();

    }
    public void handle(KeyEvent kevt){
        /*if(kevt.getCode() == kevt.ENTER){
            playGUI.processInput();
            playGUI.getInputArea().setText("");
        }*/
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
        case "yellow":
            connectGUI.offColors();
            myColor = "Yellow";
            break;
        case "blue":
            connectGUI.offColors();
            myColor = "blue";
            break;
        case "green":
            connectGUI.offColors();
            myColor = "green";
            break;
        case "purple":
            connectGUI.offColors();
            myColor = "purple";
            break;
        case "pink":
            connectGUI.offColors();
            myColor = "pink";
            break;
        case "red":
            connectGUI.offColors();
            myColor = "red";
            break;
        case "black":
            connectGUI.offColors();
            myColor = "black";
            break;
        case "ornage":
            connectGUI.offColors();
            myColor = "ornage";
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
                    if(serverAction.equals("START")){
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                playGUI.addCars(otherPlayers);
                                playGUI.addCars(thisCar);
                                stage.setScene(playScene);
                            }
                        });
                        dos.writeUTF("STARTED");
                        dos.flush();
                    }else if (serverAction.equals("REFRESH")){
                        updateRacer(dis.readUTF(),dis.readInt());
                    }else if(serverAction.equals("ADDPLAYER")){
                        addRacer();

                    }
                }
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        public void updateRacer(String UID, int WordCount){
            //find what racer it is with UID, and update their wordcount
            for(Car c: otherPlayers){
                if(c.IDis(UID)){
                    c.setWordCount(WordCount);
                    //update play GUI
                }
            }
            
        }
        public void sendRequest(){
            try{
                dos.writeUTF("STARTREQUEST");
                dos.flush();
            }catch(Exception ex){ }
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
        public boolean checkConnect(){
            return socket.isConnected();
        }


    }
}
