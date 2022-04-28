import java.util.ArrayList;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.awt.event.KeyListener;
import javafx.scene.input.KeyEvent;

public class Play extends VBox{
    private final TextArea txtArea = new TextArea();
    private final TextField inputArea = new TextField();
    public ArrayList<CarTrack> carTracks = new ArrayList<>();
    public Play() {
        this.setSpacing(10);
        this.setStyle("-fx-background-color: #FFFFFF;");
        this.getChildren().addAll(inputArea,txtArea);
    }

    public void addCars(Car car) {
        CarTrack carTrack = new CarTrack(car.getName(), car.getColor());
        carTracks.add(carTrack);
        this.getChildren().add(0,carTrack);
    }
    public void addCars(ArrayList<Car> cars) {
        for (Car car : cars) {
            CarTrack carTrack = new CarTrack(car.getName(), car.getColor());
            carTracks.add(carTrack);

            this.getChildren().add(0,carTrack);
        }

    }
    public class CarTrack extends HBox{
        private Label lblName = new Label("Player");
        private ProgressBar pgbar = new ProgressBar();
        public CarTrack (String name, String color){
            lblName.setText(name);
            lblName.setStyle("-fx-text-fill: " + color + ";-fx-font-weight: bold;");
            pgbar.setMinWidth(250);
            pgbar.setStyle("-fx-accent: " + color + ";");
            pgbar.setProgress(0);
            this.getChildren().addAll(lblName,pgbar);
        }
    }
    public void validateText(String typed){
            //if string typed = (what its supposed to ){ }
        }
    }

