import java.net.*;
import java.io.*;
import java.util.*;

public class server {
   public ArrayList<car> clientCars = new ArrayList<car>();

   public static void main(String[] args) {
      new server().execute();
   }
   
   public void execute() {
   
      System.out.println("Accepting Connection on port 12345..");
      try{
         ServerSocket ss = new ServerSocket(12345);
      
         while(true){
            Socket s = ss.accept();
            System.out.println("Request received from " + s.getInetAddress().getHostName());
            new ProcessThread(s).start();
         }
      }catch(Exception ex){
         ex.printStackTrace();
      }
      
   }

   class ProcessThread extends Thread {
      DataInputStream dis;
      DataOutputStream dos;
      boolean ready = false;
      Socket socket;
      

      public ProcessThread(Socket s){
         socket = s;
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
                  clientCars.add(readCar());
                  //server waits for more clients 
                  //then sends them to the client:
                  //filler code
                  clientCars.add(new car("Michele","red",0,5));
                  clientCars.add(new car("Tim","blue",0,5));
                  writeCar(clientCars.get(1));
                  writeCar(clientCars.get(2));

                  dos.writeUTF("START");
               }else if(clientAction.equals("STARTED")){
                  updateClients(); 
                  checkComplete();
               }else if(clientAction.equals("STOPPED")){
                  sendAllClients();//should also send amount of time or Words per min to calc it with 
               }else if(clientAction.equals("CLOSE")){
                  clientCars.remove(Integer.parseInt(dis.readUTF()));
                  socket.close();
               }
            }
         }catch(Exception ex){
            ex.printStackTrace();
         }
      }
      
      public boolean checkComplete(){//sends either PLAYON or STOP to server 
         for(car Car : clientCars){
            if(Car.compleated()){
               //then end game 
               try{
                  dos.writeUTF("STOP");
               }catch(Exception ex){ }
               return true;
            }
         }
         try{
            dos.writeUTF("PLAYON");
         }catch(Exception ex){ }
         return false;
      }
      public void updateClients(){//updates word count of this client and sends others 
         String index = null;
         int _wordCount = 0;
         try{
            index = dis.readUTF();
            _wordCount = dis.readInt();
            sendAllClients();
         }catch(Exception ex){ }
         clientCars.get(Integer.parseInt(index)).setWordCount(_wordCount);
      }
      public void sendAllClients(){//itterates through clientCars and sends their index and numWords
         try{
            for(int i = 0 ; i < clientCars.size(); i++){
               dos.writeUTF(Integer.toString(i));
               dos.writeInt(clientCars.get(i).getWordCount());
            }
         }catch(Exception ex){ }
      }
      public void writeCar(car Car){//sends car object
         try{
            dos.writeUTF(Car.getName());
            dos.writeUTF(Car.getColor());
            dos.writeInt(Car.getWordCount());
            dos.writeInt(Car.getMaxWords());
            dos.flush();
         }catch(Exception ex){}
      }
      public car readCar(){//reds car object
         String name = null;
         String color = null;
         int wordCount = 0;
         int maxWords = 0;
         try{
            name = dis.readUTF();
            color = dis.readUTF();
            wordCount = dis.readInt();
            maxWords = dis.readInt();
         }catch(Exception ex){ }
         return new car(name,color,wordCount,maxWords);
      }
      public boolean getReady(){//???
          return ready;
      }
      public void setReady(boolean _ready){//???
        ready = _ready;
      }
      public void runGame(){//????
          try{
              
          }catch(Exception ex){ }
      }
   }
}