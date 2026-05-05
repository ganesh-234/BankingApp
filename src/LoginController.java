import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    private UserManager userManager = new UserManager();

    // Scene switch WITHOUT fixed size
    private void switchScene(String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Scene scene = new Scene(root);

            BankingApp.mainStage.setScene(scene);
            scene.getRoot().requestFocus();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter username and password.");
            return;
        }

        User user = userManager.loginCustomer(username, password);

        if (user != null) {
            BankAccountManager bankAccountManager = new BankAccountManager();
            BankAccount account = bankAccountManager.getAccountByUserId(user.getUserId());

            // Auto-create account if missing
            if (account == null) {
                bankAccountManager.createAccountForUser(user.getUserId());
                account = bankAccountManager.getAccountByUserId(user.getUserId());
            }

            loadCustomerDashboard(user, account);

        } else {
            messageLabel.setText("Invalid customer username or password.");
        }
    }

    private void loadCustomerDashboard(User user, BankAccount account) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Pages/customer-dashboard.fxml"));
            Parent root = loader.load();

            CustomerDashboardController controller = loader.getController();
            controller.setData(user, account);

            Scene scene = new Scene(root);

            BankingApp.mainStage.setScene(scene);
            scene.getRoot().requestFocus();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        switchScene("Pages/main-menu.fxml");
    }
}