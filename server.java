import java.net.*;
import java.io.*;
import java.util.*;

public class Server{
   public GameMaster gm;


   public static void main(String[] args) {

      new Server().execute();
   }
   
   public void execute() {
      gm=new GameMaster();
      GameMaster.Game currentGame=gm.startANewGame();


      System.out.println("Accepting Connection on port 12345..");
      try{
         ServerSocket ss = new ServerSocket(12345);
      
         while(true){
            Socket s = ss.accept();
            System.out.println("Request received from " + s.getInetAddress().getHostName());
            new ProcessThread(s, currentGame).start();
         }
      }catch(Exception ex){
         ex.printStackTrace();
      }
      
   }

   class ProcessThread extends Thread {
      DataInputStream dis;
      DataOutputStream dos;
      Socket socket;

      //The current game this process is attached to
      GameMaster.Game currentGame;
      //The car representation this process is attached to
      Car myCar;

      public ProcessThread(Socket s, GameMaster.Game g){
         socket = s;
         currentGame=g;
      }
   
      public void run() {
      
         try{
            String ip = socket.getInetAddress().getHostName();
            
            System.out.println("Accepting connection from ip " + ip);

            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            //client sends UTF "READY&WAITING"
            while(true){
               String clientAction =  dis.readUTF();
               //do something about adding the two client together 
               if(clientAction.equals("READY&WAITING")){
                  Car car = readCar();
                  myCar=car;
                  currentGame.addRacer(car);
               }else if(clientAction.equals("STARTED")){
                  updateClient();
                 // checkComplete() -- GameMaster will handle this
               }else if(clientAction.equals("CLOSE")){
                  currentGame.removeCar(myCar.getID());
                  socket.close();
               }else if(clientAction.equals("STARTREQUEST")){
                  startRequest();
               }
            }
         }catch(Exception ex){
            ex.printStackTrace();
         }
      }


      public void startRequest(){
         boolean result =currentGame.startGame(myCar.getID());
         try{
            dos.writeBoolean(result);
         }catch (Exception ex){ }

      }

      public void removeCar(String uid){
         try{
            dos.writeUTF("REMOVECAR");
            dos.writeUTF(uid);
         }catch (Exception ex){ }

      }

      public void updateClient(){//updates word count of this client and sends others
         int _wordCount = -1;
         try{
            _wordCount = dis.readInt();
         }catch(Exception ex){ }
         currentGame.updateRacer(myCar.getID(), _wordCount);
      }

      public void refreshClients(String uid, int wordCount){
         try{
            dos.writeUTF("REFRESH");
            dos.writeUTF(uid);
            dos.writeInt(wordCount);
         }catch (Exception ex){ }
      }
      public void writeCar(Car Car){//sends car object
         try{
            dos.writeUTF(Car.getName());
            dos.writeUTF(Car.getColor());
            dos.writeUTF(Car.getID());
            dos.writeInt(Car.getWordCount());
            dos.flush();
         }catch(Exception ex){}
      }
      public Car readCar(){//reds car object
         String name = null;
         String color = null;
         int wordCount = 0;

         try{
            name = dis.readUTF();
            color = dis.readUTF();
            wordCount = dis.readInt();

         }catch(Exception ex){ }
         return new Car(name,color,wordCount);
      }

      public void startGame(String sentence){
      try{
         dos.writeUTF("START");
         dos.writeUTF(sentence);
      }catch (Exception ex){ }

      }
   }
}