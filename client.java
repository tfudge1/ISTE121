//client sends UTF with isReady();
//server sends confirm when two clients are ready 

import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.*;
import java.io.*;
import java.net.*;
import javafx.application.Platform;
import javafx.scene.paint.Color;

public class client extends Application implements EventHandler<ActionEvent> {
    private serverCommunicate SC;
    private Stage stage;
    private Scene scene;
    private VBox root = new VBox(8);

    private FlowPane flPane = new FlowPane();
    private VBox vbox = new VBox();
    private ProgressBar progressBar = new ProgressBar();
    private String sampletext = "This is some text that you will need to type out and have the program valadate then you will be scored against other players in this fun little game that we have made for out class and did not use any lorem ipsum for because we are cool like that and please give us an A";
    private Label typeText = new Label(sampletext);
    private TextField txtArea = new TextField();

    private FlowPane flPane2 = new FlowPane();
    private Button startbtn = new Button("Connect");
    private TextField ipConnect = new TextField();
    private Separator separator = new Separator();
    //connect screen
    private VBox ConnectOutter = new VBox(10);
    private TextField txtfldIp = new TextField();
    private Button connectbtn = new Button("Connect");
    private HBox HBoxN1 = new HBox(10);
    private VBox VBoxN1 = new VBox(10);
    private VBox VBoxN2 = new VBox(10);

    private Button yellow = new Button("Yellow");
    private Button red = new Button("Red");
    private Button blue = new Button("Blue");
    private Button purple = new Button("Purple");

    private Button orange = new Button("Orange");
    private Button pink = new Button("Pink");
    private Button black = new Button("Black");
    private Button green = new Button("Green");
    public String colorSelected;

   public static void main(String[] args) {
      launch(args);
   }
   
   // Called automatically after launch sets up javaFX
    public void start(Stage _stage) throws Exception {
        stage = _stage;
        stage.setTitle("VBox Example");

        //still need to add the color select area to the gui and set it disable after selected 
        //connect screen
        ConnectOutter.getChildren().addAll(txtfldIp,connectbtn,HBoxN1);
        HBoxN1.getChildren().addAll(VBoxN1,VBoxN2);
        VBoxN1.getChildren().addAll(yellow,red,blue,purple);
        VBoxN2.getChildren().addAll(orange,pink,black,green);
        ConnectOutter.setMaxWidth(300);
        txtfldIp.setText("IP");
        ConnectOutter.setAlignment(Pos.TOP_CENTER);
        HBoxN1.setAlignment(Pos.TOP_CENTER);

        connectbtn.setVisible(false);
        
        
        //Wait GUI

        
        

      flPane2.getChildren().addAll(startbtn,ipConnect);

      typeText.setWrapText(true);
      typeText.setMaxWidth(450);
      typeText.setTranslateX(25);

      progressBar.prefWidthProperty().bind(vbox.widthProperty().subtract(20));
      progressBar.setTranslateX(25);

      txtArea.setTranslateX(25);

      vbox.getChildren().addAll(progressBar,typeText,txtArea);
      flPane.getChildren().addAll(vbox);

      //root.getChildren().addAll(flPane2,separator,flPane);
      root.getChildren().addAll(ConnectOutter);
      root.setAlignment(Pos.TOP_CENTER);

      stage.setMinWidth(500);
      stage.setMinHeight(400);

      flPane.setVisible(false);
      
      connectbtn.setOnAction(this);
      yellow.setOnAction(this);
      red.setOnAction(this);
      blue.setOnAction(this);
      purple.setOnAction(this);
      orange.setOnAction(this);
      pink.setOnAction(this);
      black.setOnAction(this);
      green.setOnAction(this);


      scene = new Scene(root, 900, 400);
      stage.setScene(scene);
      stage.show();

   }
   
   public void handle(ActionEvent evt) {
      // Get the button that was clicked
      Button btn = (Button)evt.getSource();
      
      // Switch on its name
      switch(btn.getText()) {
        case "Connect":
            ConnectOutter.setVisible(false);
                //when testing with actual server use:
            //SC = new serverCommunicate(txtfldIp.getText());
                //when testing without server use:
            SC = new serverCommunicate("localhost");
            SC.start();
            startbtn.setText("Ready");
            flPane.setVisible(true);
            ipConnect.setVisible(false);
            break;
        case "Ready":
            System.out.println("ready");
            SC.isReady();
            break;
        case "Yellow":
            HBoxN1.setVisible(false);
            connectbtn.setVisible(true);
            colorSelected = "yellow";
            break;
        case "Red":
            HBoxN1.setVisible(false);
            connectbtn.setVisible(true);
            colorSelected = "red";
            break;
        case "Blue":
            HBoxN1.setVisible(false);
            connectbtn.setVisible(true);
            colorSelected = "blue";
            break;
        case "Purple":
            HBoxN1.setVisible(false);
            connectbtn.setVisible(true);
            colorSelected = "purple";
            break;
        case "Ornage":
            HBoxN1.setVisible(false);
            connectbtn.setVisible(true);
            colorSelected = "orange";
            break;
        case "Pink":
            HBoxN1.setVisible(false);
            connectbtn.setVisible(true);
            colorSelected = "pink";
            break;
        case "Black":
            HBoxN1.setVisible(false);
            connectbtn.setVisible(true);
            colorSelected = "black";
            break;
        case "green":
            HBoxN1.setVisible(false);
            connectbtn.setVisible(true);
            colorSelected = "green";
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
    public car myCar;
    //private String address;
    public serverCommunicate(String _address){
        //address = _address;
        try{
            socket = new Socket(_address,12345);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        }catch(Exception ex){ }
        System.out.println("Thread started");
    }
    public void isReady(){
        try{
            dos.writeUTF("ready");
            dos.flush();
        }catch(Exception ex){ }
    }
    public boolean checkConnect(){
        return socket.isConnected();
    }
    
    public void run(){
       try{
            //server sends UTF saying that two clients are connected 
            String start = dis.readUTF();
            System.out.println(start);

            playGame();

       } catch (Exception ex){
          ex.printStackTrace();
       }
    }
    public void playGame(){
        //check inputs while being typed
    }
    public boolean validate(String typed, String text){
        //need to find out how to get where the person is in the paragraph to check if the word is right
        if(typed == text){
            return true;
        }else{
            return false;
        }
        
    }
 }
}
