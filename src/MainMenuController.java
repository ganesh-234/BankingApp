import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class MainMenuController {

    private void switchScene(String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Scene scene = new Scene(root);
            BankingApp.mainStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUserLogin() {
        switchScene("Pages/login.fxml");
    }

    @FXML
    private void handleCreateAccount() {
        switchScene("Pages/create-account.fxml");
    }

    @FXML
    private void handleAdminLogin() {
        System.out.println("Admin login not implemented yet.");
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}
