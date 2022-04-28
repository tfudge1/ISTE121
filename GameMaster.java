package sample;
import java.util.ArrayList;
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
            return _isStarted && sentence != null && racerDict.values().size() > 1;
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
                }
            }
        }

        public void refreshRacers(Car updatedRacer) {
            for (Car r : racerDict.values()) {
                if (r != updatedRacer) {
                    r.getClientConnection().refreshClients(updatedRacer.getID(), updatedRacer.getWordCount());
                }
            }

        }


        @Override
        public void run() {
            while (!isStarted()) {
                //Await Players
            }
            while (!checkWinner()) {
                //While the race in progress the racers will be updating their own values so do nothing
            }
            System.out.println("The Winners are " + winner.toArray().toString());
            System.out.println(RaceReport());
            _isStarted = false;
        }


        public boolean checkWinner() {
            for (Car r : racerDict.values()) {
                if (r.getWordCount() == totalLength) {
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
            addSentence("Practice your keyboard typing speed here with words or sentences in many different languages with this free online 1 minute typing test.");
            for (Car r : racerDict.values()) {
                r.getClientConnection().startGame(sentence);
            }
            return true;
        }

        public String RaceReport() {
            StringBuilder report = new StringBuilder();
            for (Car r : racerDict.values()) {
                report.append(r.toString()).append("\n");
            }
            return report.toString();
        }

        public void removeCar(String uid) {
            racerDict.remove(uid);

        }

    }
}

