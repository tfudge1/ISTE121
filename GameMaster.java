import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class GameMaster {
    public ArrayList<Game> gameList;


    public GameMaster() {
        gameList = new ArrayList<>();
    }

    public Game startANewGame() {
        Game g = new Game();
        gameList.add(g);
        return g;
    }


    private class Game extends Thread {

        private ConcurrentHashMap<String, Racer> racerDict;
        private boolean _isStarted = false;


        private String sentence;
        private int totalLength;

        public boolean isStarted() {
             return _isStarted&&sentence!=null&&racerDict.values().size()>1;
        }

        public Game() {

            racerDict = new ConcurrentHashMap<>();

        }

        public void addRacer(String color) {
            if (!isStarted()) {
                String uid = UUID.randomUUID().toString();
                Racer r = new Racer(uid, color, this);
                racerDict.put(uid, r);

            }

        }

        public void updateRacer(String uid, int position) {
            if (isStarted()) {
                Racer r = racerDict.get(uid);
                int currentPosition = r.getPosition();
                if (currentPosition < position) {
                    r.setPosition(position);
                    refreshRacers(r);
                }
            }


        }

        public void refreshRacers(Racer updatedRacer) {
            for (Racer r : racerDict.values()) {
                if (r != updatedRacer) {
                    r.updateOtherRacer(updatedRacer);
                }
            }

        }


        @Override
        public void run() {
            _isStarted = true;
            while(!checkWinner()){
                //While the race in progress the racers will be updating their own values so do nothing

            }
            System.out.println("The Winners are "+winner.toArray().toString());
            System.out.println(RaceReport());
            _isStarted=false;


        }


        public ArrayList<Racer> winner= new ArrayList<>();

        public boolean checkWinner(){
            for(Racer r:racerDict.values()){
                if(r.getPosition()==totalLength){
                    winner.add(r);
                }
            }
            return winner.size()>0;
        }

        public void addSentence(String s) {
            sentence = s;
            totalLength = s.split(" ").length;
        }


        public String RaceReport(){
            StringBuilder report= new StringBuilder();
            for(Racer r:racerDict.values()){
                report.append(r.toString()).append("\n");
            }
            return report.toString();
        }
    }

    private class Racer {
        private String UID;

        private int position = 0;
        private String color = "white";
        private ClientHandler ch;


        public Racer(String UID, String color, Game g) {
            this.UID = UID;
            this.color = color;
            this.position = 0;
            ch = new ClientHandler(this, g);
        }

        public String getUID() {
            return UID;
        }


        public synchronized int getPosition() {
            return position;
        }

        public String getColor() {
            return color;
        }

        public synchronized void setPosition(int position) {
            this.position = position;
        }

        public void updateOtherRacer(Racer r) {
            ch.updateRacer(r);
        }

        @Override
        public String toString(){
            return "The "+ color+" Racer ("+UID+") is at "+position;
        }

    }

    private class ClientHandler extends Thread {


        public ClientHandler(Racer r, Game mainGame) {

        }

        public void updateRacer(Racer r) {

        }


    }
}