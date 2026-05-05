import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.List;

/*
 * AdminDashboardController handles admin features:
 * - view users
 * - freeze/unfreeze customer accounts
 * - view flagged transactions
 * - approve flagged transactions
 */

public class AdminDashboardController {

    @FXML
    private Label adminLabel;

    @FXML
    private TextField userIdField;

    @FXML
    private TextField transactionIdField;

    @FXML
    private TextArea outputArea;

    private User admin;
    private UserManager userManager = new UserManager();
    private BankAccountManager accountManager = new BankAccountManager();
    private TransactionManager transactionManager = new TransactionManager();

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    @FXML
    private void handleFreeze() {
        changeStatus("Frozen");
    }

    @FXML
    private void handleUnfreeze() {
        changeStatus("Active");
    }

    // Freezes or unfreezes customer accounts
    private void changeStatus(String status) {
        try {
            int userId = Integer.parseInt(userIdField.getText().trim());

            User targetUser = userManager.getUserById(userId);

            if (targetUser == null) {
                outputArea.setText("User not found.");
                return;
            }

            if (!targetUser.getRole().equalsIgnoreCase("Customer")) {
                outputArea.setText("Admin accounts cannot be frozen or unfrozen.");
                return;
            }

            BankAccount account = accountManager.getAccountByUserId(userId);

            if (account == null) {
                outputArea.setText("Account not found.");
                return;
            }

            account.setStatus(status);
            accountManager.saveAccount(account);

            outputArea.setText("Account status changed to " + status + ".");

        } catch (NumberFormatException e) {
            outputArea.setText("Invalid user ID.");
        }
    }


    // Displays customer accounts only
    // Admin accounts are hidden from this list
    @FXML
    private void handleViewUsers() {
        List<User> users = userManager.loadUsers();

        outputArea.clear();

        for (User user : users) {
            if (user.getRole().equalsIgnoreCase("Customer")) {
                BankAccount account = accountManager.getAccountByUserId(user.getUserId());

                if (account != null) {
                    outputArea.appendText(
                            "User ID: " + user.getUserId()
                                    + " | Username: " + user.getUsername()
                                    + " | Account ID: " + account.getAccountId()
                                    + " | Balance: $" + account.getBalance()
                                    + " | Status: " + account.getStatus()
                                    + "\n"
                    );
                } else {
                    outputArea.appendText(
                            "User ID: " + user.getUserId()
                                    + " | Username: " + user.getUsername()
                                    + " | No bank account found"
                                    + "\n"
                    );
                }
            }
        }
    }

    // Shows transactions marked FLAGGED
    @FXML
    private void handleViewFlaggedTransactions() {
        List<Transaction> flaggedTransactions = transactionManager.getFlaggedTransactions();

        outputArea.clear();

        if (flaggedTransactions.isEmpty()) {
            outputArea.setText("No flagged transactions found.");
            return;
        }

        for (Transaction transaction : flaggedTransactions) {
            BankAccount account = accountManager.getAccountById(transaction.getAccountId());

            if (account != null) {
                User user = userManager.getUserById(account.getUserId());

                if (user != null && user.getRole().equalsIgnoreCase("Customer")) {
                    outputArea.appendText(
                            "Transaction ID: " + transaction.getTransactionId()
                                    + " | User: " + user.getUsername()
                                    + " | Account ID: " + transaction.getAccountId()
                                    + " | Type: " + transaction.getType()
                                    + " | Amount: $" + transaction.getAmount()
                                    + " | Date: " + transaction.getDate()
                                    + " | Status: " + transaction.getStatus()
                                    + "\n"
                    );
                }
            }
        }
    }

    // Admin review:
    // - Finds the flagged transaction
    // - Deducts the money from the account
    // - Marks transaction as REVIEWED
    // - Reactivates account
    @FXML
    private void handleMarkReviewed() {
        try {
            int transactionId = Integer.parseInt(transactionIdField.getText().trim());

            Transaction transaction = transactionManager.getTransactionById(transactionId);

            if (transaction == null) {
                outputArea.setText("Transaction not found.");
                return;
            }

            if (!transaction.getStatus().equalsIgnoreCase("FLAGGED")) {
                outputArea.setText("Only flagged transactions can be reviewed.");
                return;
            }

            BankAccount account = accountManager.getAccountById(transaction.getAccountId());

            if (account == null) {
                outputArea.setText("Account not found.");
                return;
            }

            if (transaction.getAmount() > account.getBalance()) {
                outputArea.setText("Transaction cannot go through. Insufficient funds.");
                return;
            }

            account.setBalance(account.getBalance() - transaction.getAmount());
            account.setStatus("Active");
            accountManager.saveAccount(account);

            transactionManager.updateTransactionStatus(transactionId, "REVIEWED");

            outputArea.setText("Transaction reviewed and completed.");
            transactionIdField.clear();

        } catch (NumberFormatException e) {
            outputArea.setText("Invalid transaction ID.");
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