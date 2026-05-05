import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.Parent;

public class AccountInfoController {

    @FXML private Label userIdLabel;
    @FXML private Label usernameLabel;
    @FXML private Label roleLabel;
    @FXML private Label accountIdLabel;
    @FXML private Label balanceLabel;
    @FXML private Label statusLabel;

    private User currentUser;
    private BankAccount currentAccount;

    public void setData(User user, BankAccount account) {
        currentUser = user;
        currentAccount = account;

        userIdLabel.setText("User ID: " + user.getUserId());
        usernameLabel.setText("Username: " + user.getUsername());
        roleLabel.setText("Role: " + user.getRole());

        accountIdLabel.setText("Account ID: " + account.getAccountId());
        balanceLabel.setText("Balance: $" + account.getBalance());
        statusLabel.setText("Status: " + account.getStatus());
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Pages/customer-dashboard.fxml"));
            Parent root = loader.load();

            BankAccountManager manager = new BankAccountManager();
            BankAccount updated = manager.getAccountByUserId(currentUser.getUserId());

            CustomerDashboardController controller = loader.getController();
            controller.setData(currentUser, updated);

            BankingApp.mainStage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
