
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class GameMaster {
    public ArrayList<Game> gameList;


    public GameMaster() {
        gameList = new ArrayList<>();
    }

    public Game startANewGame() {
        Game g = new Game();
        gameList.add(g);
        g.start();
        return g;
    }


    public class Game extends Thread {


        public ArrayList<Car> winner = new ArrayList<>();
        public String hostUID = "";
        private ConcurrentHashMap<String, Car> racerDict;
        private boolean _isStarted = false;

        private String sentence;
        private int totalLength;

        public boolean isStarted() {
            System.out.println("Is started: " + _isStarted);
            return _isStarted ;//&& sentence != null; //&& racerDict.values().size() > 1;
        }

        public Game() {

            racerDict = new ConcurrentHashMap<>();
        }

        public void addRacer(Car car) {
            if (!isStarted()) {
                if (racerDict.size() == 0) {
                    hostUID = car.getID();
                }
                racerDict.put(car.getID(), car);
                notifyOthersOfNewPlayer(car);
            }
        }
        public void notifyOthersOfNewPlayer(Car c){
            for (Car r : racerDict.values()) {
                if (r != c) {
                    r.getClientConnection().addPlayer(c);
                }
            }
        }
        public ArrayList<Car> getCarList(){
            return new ArrayList<>(racerDict.values());
        }
        public void updateRacer(String uid, int position) {
            if (isStarted()) {
                Car r = racerDict.get(uid);
                int currentPosition = r.getWordCount();
                if (currentPosition < position) {
                    r.setWordCount(position);
                    refreshRacers(r);

                }else{
                    System.out.println("Racer " + r.getID() + " is trying to cheat and is trying to move backwards");
                }
            }
        }
        public boolean hasEveryoneFinished(){
            System.out.println("Checking if everyone has finished " + winner.size() + " " + racerDict.size());

            return  (winner.size() == racerDict.size());
        }
        public void endGame(){
            for (Car r : racerDict.values()) {
                r.getClientConnection().endGame(winner);
            }
        }
        public int getRacerSize(){
            return racerDict.size();
        }

        public void refreshRacers(Car updatedRacer) {
            for (Car r : racerDict.values()) {
                if (r != updatedRacer) {
                    System.out.println("Sending update to " + r.getID() + " of " + updatedRacer.getID()+"->"+updatedRacer.getWordCount());
                    r.getClientConnection().refreshClients(updatedRacer.getID(), updatedRacer.getWordCount());
                }
            }

        }

        public void generateSentence(){
            String fileName = "gatsby.txt";
            Random r = new Random();
            int sentence = r.nextInt(300);
            int length =0 ;
            try{
                File file = new File(fileName);
                Scanner sc = new Scanner(file);
                while(sc.hasNextLine()){
                    String snippet = sc.nextLine();
                    if(length == sentence){
                        System.out.println("line" + snippet);
                        snippet = snippet.trim();
                        addSentence(snippet);
                        break;
                    }
                        length++;
                }
                sc.close();

            } catch (FileNotFoundException _e) {
                _e.printStackTrace();
            }
        }
        @Override
        public void run() {
            System.out.println("Filling Lobby");
            generateSentence();
            /*
            while(true) {
                if (isStarted()) {
                    //Await players
                    if (checkWinner()) {
                        System.out.println("The Winners are " + winner.toArray().toString());
                        System.out.println(RaceReport());
                        _isStarted = false;
                        break;
                    }
                }
            }*/
        }
        public void updateRacersWPM(String uid, int wpm, Car c) {
                for(Car r : racerDict.values()){
                    if(!r.getID().equals(uid)){
                        r.getClientConnection().sendWordsPerMinute(uid, wpm);
                    }
                }

                winner.add(c);
                System.out.println("Winner: " + c.getID());
                if(hasEveryoneFinished()){
                    endGame();
                }
        }

        public boolean checkWinner() {
            for (Car r : racerDict.values()) {
                System.out.println("Checking " + r.getID() + " for winner" + r.getWordCount());
                if (r.getWordCount() == totalLength) {
                    System.out.println("Found a match" + r.getID() + " for winner" + r.getWordCount() + " " + totalLength);
                    winner.add(r);
                }
            }
            return winner.size() > 0;
        }

        public void addSentence(String s) {
            sentence = s;
            totalLength = s.split(" ").length;
        }

        public boolean startGame(String uid) {
            if (!uid.equals(hostUID)) {
                return false;
            }

            _isStarted = true;

            //addSentence("Practice your keyboard typing speed here with words or sentences in many different languages with this free online 1 minute typing test.");
           // addSentence("This is a test sentence");

            System.out.println("Starting Game: " + sentence);
            System.out.println("Total Length: " + totalLength);
            for (Car r : racerDict.values()) {
                r.getClientConnection().startGame(sentence);
            }
            return true;
        }

        public String getHostUID(){
            return hostUID;
        }

        public String RaceReport() {
            StringBuilder report = new StringBuilder();
            for (Car r : racerDict.values()) {
                report.append(r.toString()).append("\n");
            }
            return report.toString();
        }

        public void removeCar(String uid) {
            System.out.println("Removing " + uid);
            racerDict.remove(uid);
            winner.removeIf(r -> r.getID().equals(uid));
        }
        public void restartGame(){
            generateSentence();

            winner.clear();
            _isStarted=true;
            for (Car r : racerDict.values()) {
                r.setWordCount(0);
                r.getClientConnection().startGame(sentence);
            }
        }

    }
}

