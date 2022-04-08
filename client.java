//client sends UTF with isReady();
//server sends confirm when two clients are ready 

import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.*;
import java.io.*;
import java.net.*;
import javafx.application.Platform;

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

   public static void main(String[] args) {
      launch(args);
   }
   
   // Called automatically after launch sets up javaFX
   public void start(Stage _stage) throws Exception {
      stage = _stage;
      stage.setTitle("VBox Example");

      flPane2.getChildren().addAll(startbtn,ipConnect);

      typeText.setWrapText(true);
      typeText.setMaxWidth(450);
      typeText.setTranslateX(25);

      progressBar.prefWidthProperty().bind(vbox.widthProperty().subtract(20));
      progressBar.setTranslateX(25);

      txtArea.setTranslateX(25);

      vbox.getChildren().addAll(progressBar,typeText,txtArea);
      flPane.getChildren().addAll(vbox);

      root.getChildren().addAll(flPane2,separator,flPane);

      flPane.setVisible(false);
      
      startbtn.setOnAction(this);

      scene = new Scene(root, 500, 300);
      stage.setScene(scene);
      stage.show();

   }
   
   public void handle(ActionEvent evt) {
      // Get the button that was clicked
      Button btn = (Button)evt.getSource();
      
      // Switch on its name
      switch(btn.getText()) {
        case "Connect":
            /* This is for when you want to check that the connnecting has worked before displaying the racetracks
            System.out.println("btn clicked");
            SC = new serverCommunicate(ipConnect.getText());
            if(SC.checkConnect()){
                startbtn.setText("Ready");
                flPane.setVisible(true);
                ipConnect.setVisible(false);
                SC.start();
            }else{
                System.out.println("failed to connect");
            }
            */
            SC = new serverCommunicate(ipConnect.getText());
            SC.start();
            startbtn.setText("Ready");
            flPane.setVisible(true);
            ipConnect.setVisible(false);
            break;
        case "Ready":
            System.out.println("ready");
            SC.isReady();
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
