
import java.net.*;
import java.io.*;
import java.util.ArrayList;

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
            System.out.println("INBOUND ATTEMPT: " + s.getInetAddress().getHostName());
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
            
            System.out.println("INBOUND CONNECTION - " + ip);

            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            //client sends UTF "READY&WAITING"
            while(true){
               String clientAction =  dis.readUTF();
               System.out.println("INBOUND: " + clientAction);

               //do something about adding the two client together
               if(clientAction.equals("READY&WAITING")){
                  Car car = readCar();
                  myCar=car;
                  myCar.setClientConnection(this);
                  currentGame.addRacer(car);
                  sendCarList();
               }else if(clientAction.equals("STARTED")){
                  //updateClient();
                 // checkComplete() -- GameMaster will handle this
               }else if(clientAction.equals("CLOSE")){
                  currentGame.removeCar(myCar.getID());
                  socket.close();
                  break;
               }else if(clientAction.equals("STARTREQUEST")){
                  startRequest();
               }else if(clientAction.equals("UPDATE")){
                  updateClient();
               }else if(clientAction.equals("FINISHED")){
                  getWordsPerMinute();
               }else if(clientAction.equals("RESTART")){
                  currentGame.restartGame();
               }
            }
         }catch(Exception ex){
            ex.printStackTrace();
         }
      }
      public void startRequest(){
         boolean result = currentGame.startGame(myCar.getID());
         try{
//insted of writeing a boolean how about sending "START" to this client like all the other ones...
//at least thats how I think it works 
           // dos.writeBoolean(result);
         }catch (Exception ex){
            ex.printStackTrace();
         }

      }
      public void getWordsPerMinute(){
         try{
            int wpm = dis.readInt();
            currentGame.updateRacersWPM(myCar.getID(), wpm, myCar);
         }catch (Exception ex){
            ex.printStackTrace();
         }

      }
      public void sendEnd(ArrayList<Car> cars){
         try{
            dos.writeUTF("END");
            dos.writeInt(cars.size());
            for(Car c : cars){
               dos.writeUTF(c.getID());
            }
            dos.flush();
         }catch (Exception ex){
            ex.printStackTrace();
         }
      }
      public void sendWordsPerMinute(String UID, int wpm){
         try{
            dos.writeUTF("WPM");
            dos.writeUTF(UID);
            dos.writeInt(wpm);
            dos.flush();
         }catch (Exception ex){
            ex.printStackTrace();
         }
      }
      public void sendCarList(){
         ArrayList<Car> cars = currentGame.getCarList();
         try{
            dos.writeInt(cars.size());
            for(Car c : cars){
               if(c!=myCar)
                  writeCar(c);
            }

         } catch (IOException _e) {
            _e.printStackTrace();
         }
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
         }catch(Exception ex){
            ex.printStackTrace();
         }
         System.out.println("Updating client: "+myCar.getID()+" with word count: " + _wordCount);
         currentGame.updateRacer(myCar.getID(), _wordCount);
      }

      public void refreshClients(String uid, int wordCount){
         try{
            dos.writeUTF("REFRESH");
            dos.writeUTF(uid);
            dos.writeInt(wordCount);
            dos.flush();
         }catch (Exception ex){
            ex.printStackTrace();
         }
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
         String UID = null;

         try{
            name = dis.readUTF();
            System.out.println("Name: " + name);
            color = dis.readUTF();
            System.out.println("Color: " + color);
            wordCount = dis.readInt();
            System.out.println("WordCount: " + wordCount);
            UID = dis.readUTF();
            System.out.println("UID: " + UID);
            Car c = new Car(name, color, wordCount, UID);
            System.out.println("Car read: " + c.toString());
            return c;
         }catch(Exception ex){
            ex.printStackTrace();
         }
         return null;
      }

      public void startGame(String sentence){
      try{
         System.out.println("OUTBOUND: START");
         dos.writeUTF("START");
         dos.writeUTF(sentence);
      }catch (Exception ex){ }

      }
      public void endGame(ArrayList<Car> cars){
         try{
            dos.writeUTF("END");
            dos.writeInt(cars.size());
            for(Car c : cars){
               dos.writeUTF(c.getID());
            }
            dos.flush();
         }catch (Exception ex){ }
      }
      public void addPlayer(Car newPlayer){
         try{
            dos.writeUTF("ADDPLAYER");
            writeCar(newPlayer);
         }catch(Exception ex){
            ex.printStackTrace();
         }
      }

   }
}
