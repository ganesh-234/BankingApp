import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.Parent;

import java.util.List;

public class TransactionHistoryController {

    @FXML private Label accountLabel;
    @FXML private TextArea transactionArea;

    private User currentUser;
    private BankAccount currentAccount;

    private TransactionManager transactionManager = new TransactionManager();

    public void setData(User user, BankAccount account) {
        currentUser = user;
        currentAccount = account;

        accountLabel.setText("Account ID: " + account.getAccountId());

        List<Transaction> transactions =
                transactionManager.getTransactionsForAccount(account.getAccountId());

        transactionArea.clear();

        for (Transaction t : transactions) {
            transactionArea.appendText(t.display() + "\n");
        }
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
