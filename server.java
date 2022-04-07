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
   
      Socket socket;
      public ProcessThread(Socket s){
         socket = s;
      }
   
      public void run() {
      
         try{
            String ip = socket.getInetAddress().getHostName();
            
            System.out.println("Accepting connection from ip " + ip);
            
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            
            int first = dis.readInt();
            int second = dis.readInt();
               
            dos.writeUTF("Sum is: " + (first + second));
            dos.flush();
            
            socket.close();
            
            
         }catch(Exception ex){
            ex.printStackTrace();
         }
      }
   }
}