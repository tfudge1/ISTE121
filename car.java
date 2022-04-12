import java.util.*;
public class car{
    public String name;
    public String color;
    public int wordCount;
    public int maxWords;
    public ArrayList<String> words = new ArrayList<String>();

    public car(String _name, String _color, int _wordCount, int _maxWords){
        color = _color;
        name = _name;
        wordCount = _wordCount;
        maxWords = _maxWords;
    }
    public boolean compleated(){
        if(wordCount == maxWords){
            return true;
        }else{
            return false;
        }
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
    public int getWordCount(){
        return wordCount;
    }
    public void setWordCount(int _wordCount){
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
}