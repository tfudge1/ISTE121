import java.util.*;
public class car{
    public String color;
    public String CSVtext;
    public ArrayList<String> words = new ArrayList<String>();
    public car(String _color, String _CSVtext){
        color = _color;
        CSVtext = _CSVtext;

        String[] tempWords = CSVtext.split(",");
        for(String str : tempWords){
            words.add(str);
        }

    }
    public String getColor(){
        return color;
    }
    public String getCSVtext(){
        return CSVtext;
    }
    public String getWord(int index){
        return words.get(index);
    }
}