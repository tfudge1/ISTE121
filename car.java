import java.util.*;
public class Car {
    public String name;
    public String color;
    public int wordCount;
    public int maxWords=-1;
    public ArrayList<String> words = new ArrayList<String>();
    private String UID;
    private Server.ProcessThread clientConnection;

    public Car(String _name, String _color, int _wordCount){
        color = _color;
        name = _name;
        wordCount = _wordCount;
        UID = UUID.randomUUID().toString();
    }
    public boolean completed(){
        if(wordCount == maxWords){
            return true;
        }else{
            return false;
        }
    }
    public String getID(){
        return UID;
    }
    public void addWord(String newWord){
        words.add(newWord);
    }
    public void upWordCount(){
        wordCount += 1;
    }
    public String getColor(){
        return color;
    }
    public String getName(){
        return name;
    }
    public synchronized int getWordCount(){
        return wordCount;
    }
    public synchronized void setWordCount(int _wordCount){
        wordCount = _wordCount;
    }
    public int getMaxWords(){
        return maxWords;
    }
    public String getWord(int index){
        return words.get(index);
    }
    public int getNumWords(){
        return words.size();
    }
    public void setClientConnection(Server.ProcessThread _clientConnection){
        clientConnection = _clientConnection;
    }
    public Server.ProcessThread getClientConnection(){
        return clientConnection;
    }
    @Override
    public String toString(){
        return "The "+ color+" Car ("+UID+") is at "+wordCount +" of "+maxWords+" words";
    }
}