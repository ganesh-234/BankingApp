import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.Parent;

public class AdminLoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private UserManager userManager = new UserManager();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        User admin = userManager.loginAdmin(username, password);

        if (admin != null) {
            loadAdminDashboard(admin);
        } else {
            messageLabel.setText("Invalid admin credentials.");
        }
    }

    @FXML
    private void handleBack() {
        switchScene("Pages/main-menu.fxml");
    }

    private void loadAdminDashboard(User admin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Pages/admin-dashboard.fxml"));
            Parent root = loader.load();

            AdminDashboardController controller = loader.getController();
            controller.setAdmin(admin);

            BankingApp.mainStage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void switchScene(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            BankingApp.mainStage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
