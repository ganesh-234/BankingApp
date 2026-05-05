import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.LocalDate;

/*
 * CustomerDashboardController controls the customer dashboard.
 * - deposit money
 * - withdraw money
 * - view transactions
 * - view account info
 */


public class CustomerDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label balanceLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private TextField amountField;

    @FXML
    private Label messageLabel;

    private User user;
    private BankAccount account;

    private BankAccountManager accountManager = new BankAccountManager();
    private TransactionManager transactionManager = new TransactionManager();

    // Receives the logged-in user and their bank account from LoginController
    public void setData(User user, BankAccount account) {
        this.user = user;
        this.account = account;

        welcomeLabel.setText("Welcome, " + user.getUsername());
        updateUI();
    }

    // Refreshes balance and account status on screen
    private void updateUI() {
        balanceLabel.setText("Balance: $" + account.getBalance());
        statusLabel.setText("Status: " + account.getStatus());
    }

    // Checks if the account is frozen
    private boolean isFrozen() {
        return account.getStatus().equalsIgnoreCase("Frozen");
    }

    @FXML
    private void handleDeposit() {
        if (isFrozen()) {
            messageLabel.setText("Account Frozen.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountField.getText().trim());

            if (amount <= 0) {
                messageLabel.setText("Invalid amount.");
                return;
            }

            account.setBalance(account.getBalance() + amount);
            accountManager.saveAccount(account);

            transactionManager.addTransaction(
                    account.getAccountId(),
                    "Deposit",
                    amount,
                    LocalDate.now().toString()
            );

            amountField.clear();
            updateUI();
            messageLabel.setText("Deposit successful.");

        } catch (NumberFormatException e) {
            messageLabel.setText("Invalid input.");
        }
    }

    @FXML
    private void handleWithdraw() {
        if (isFrozen()) {
            messageLabel.setText("Account Frozen.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountField.getText().trim());

            if (amount <= 0) {
                messageLabel.setText("Invalid amount.");
                return;
            }

            if (amount > account.getBalance()) {
                messageLabel.setText("Insufficient funds.");
                return;
            }



            //If withdrawal is suspicious, do NOT subtract money yet.
            //The transaction is saved as FLAGGED and waits for admin review

            if (amount >= 1000) {
                account.setStatus("Frozen");
                accountManager.saveAccount(account);

                transactionManager.addTransaction(
                        account.getAccountId(),
                        "Withdraw",
                        amount,
                        LocalDate.now().toString()
                );

                amountField.clear();
                updateUI();
                messageLabel.setText("Fraud detected. Waiting for admin.");
                return;
            }

            // Normal withdrawals go through immediately
            account.setBalance(account.getBalance() - amount);
            accountManager.saveAccount(account);

            transactionManager.addTransaction(
                    account.getAccountId(),
                    "Withdraw",
                    amount,
                    LocalDate.now().toString()
            );

            amountField.clear();
            updateUI();
            messageLabel.setText("Withdrawal successful.");

        } catch (NumberFormatException e) {
            messageLabel.setText("Invalid input.");
        }
    }

    @FXML
    private void handleViewTransactions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Pages/transaction-history.fxml"));
            Parent root = loader.load();

            TransactionHistoryController controller = loader.getController();
            controller.setData(user, account);

            Scene scene = new Scene(root);
            BankingApp.mainStage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewAccountInfo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Pages/account-info.fxml"));
            Parent root = loader.load();

            AccountInfoController controller = loader.getController();
            controller.setData(user, account);

            Scene scene = new Scene(root);
            BankingApp.mainStage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Pages/main-menu.fxml"));
            Scene scene = new Scene(root);
            BankingApp.mainStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}