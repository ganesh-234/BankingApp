import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class CreateAccountController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    private BankAccountManager bankAccountManager = new BankAccountManager();

    private UserManager userManager = new UserManager();

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
    private void handleCreateAccount() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please fill in all fields.");
            return;
        }

        boolean success = userManager.registerCustomer(username, password);

        if (success) {
            User newUser = userManager.loginCustomer(username, password);

            if (newUser != null) {
                bankAccountManager.createAccountForUser(newUser.getUserId());
            }

            messageLabel.setText("Customer account created successfully.");
            clearFields();
        } else {
            messageLabel.setText("Username already exists or account creation failed.");
        }
    }

    @FXML
    private void handleBack() {
        switchScene("Pages/main-menu.fxml");
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
    }
}