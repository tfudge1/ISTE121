import java.net.*;
import java.io.*;

public class server {

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

            //client sends UTF "ready"
            String clientInit =  dis.readUTF();
            //do something about adding the two client together 
            
         }catch(Exception ex){
            ex.printStackTrace();
         }
      }
      public boolean getReady(){
          return ready;
      }
      public void setReady(boolean _ready){
        ready = _ready;
      }
      public void runGame(){
          try{
              
          }catch(Exception ex){ }
      }
   }
}