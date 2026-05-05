import java.io.*;
import java.util.ArrayList;
import java.util.List;


//TransactionManager handles transaction storage in transactions.txt

public class TransactionManager {
    private static final String TRANSACTION_FILE = "transactions.txt";

    public TransactionManager() {
        File file = new File(TRANSACTION_FILE);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error creating transactions file.");
        }
    }

    // Adds a transaction to transactions.txt
    public void addTransaction(int accountId, String type, double amount, String date) {
        int transactionId = getNextTransactionId();
        String status = getTransactionStatus(type, amount);

        Transaction transaction = new Transaction(
                transactionId,
                accountId,
                type,
                amount,
                date,
                status
        );

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTION_FILE, true))) {
            writer.write(transaction.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing transaction.");
        }
    }

    // Function that any withdrawal of $1000 or more is flagged
    private String getTransactionStatus(String type, double amount) {
        if (type.equalsIgnoreCase("Withdraw") && amount >= 1000) {
            return "FLAGGED";
        }

        return "NORMAL";
    }

    // Gets all transactions for one account
    public List<Transaction> getTransactionsForAccount(int accountId) {
        List<Transaction> transactions = new ArrayList<>();

        for (Transaction transaction : getAllTransactions()) {
            if (transaction.getAccountId() == accountId) {
                transactions.add(transaction);
            }
        }

        return transactions;
    }

    // Gets only transactions waiting for admin review
    public List<Transaction> getFlaggedTransactions() {
        List<Transaction> flaggedTransactions = new ArrayList<>();

        for (Transaction transaction : getAllTransactions()) {
            if (transaction.getStatus().equalsIgnoreCase("FLAGGED")) {
                flaggedTransactions.add(transaction);
            }
        }

        return flaggedTransactions;
    }

    // Finds a transaction by ID
    public Transaction getTransactionById(int transactionId) {
        for (Transaction transaction : getAllTransactions()) {
            if (transaction.getTransactionId() == transactionId) {
                return transaction;
            }
        }

        return null;
    }

    // Updates a transaction status, such as FLAGGED -> REVIEWED
    public boolean updateTransactionStatus(int transactionId, String newStatus) {
        List<Transaction> transactions = getAllTransactions();
        boolean found = false;

        for (Transaction transaction : transactions) {
            if (transaction.getTransactionId() == transactionId) {
                transaction.setStatus(newStatus);
                found = true;
                break;
            }
        }

        if (!found) {
            return false;
        }

        return saveAllTransactions(transactions);
    }

    // Finds the next transaction ID
    private int getNextTransactionId() {
        List<Transaction> transactions = getAllTransactions();
        int maxId = 0;

        for (Transaction transaction : transactions) {
            if (transaction.getTransactionId() > maxId) {
                maxId = transaction.getTransactionId();
            }
        }

        return maxId + 1;
    }


    // Loads all transactions
    private List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTION_FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 6) {
                    int transactionId = Integer.parseInt(parts[0]);
                    int accountId = Integer.parseInt(parts[1]);
                    String type = parts[2];
                    double amount = Double.parseDouble(parts[3]);
                    String date = parts[4];
                    String status = parts[5];

                    transactions.add(new Transaction(transactionId, accountId, type, amount, date, status));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading transactions.");
        }

        return transactions;
    }

    // Rewrites the transactions file after status changes
    private boolean saveAllTransactions(List<Transaction> transactions) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTION_FILE))) {
            for (Transaction transaction : transactions) {
                writer.write(transaction.toString());
                writer.newLine();
            }

            return true;
        } catch (IOException e) {
            System.out.println("Error saving transactions.");
            return false;
        }
    }
}