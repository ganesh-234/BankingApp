import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BankingApp extends Application {

    public static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("Pages/main-menu.fxml"));
        Scene scene = new Scene(root);

        mainStage.setTitle("Distributed Banking System");
        mainStage.setScene(scene);

        scene.getRoot().requestFocus();
        mainStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}