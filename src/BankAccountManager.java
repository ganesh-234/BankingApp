import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
 * BankAccountManager handles account storage in accounts.txt
 */

public class BankAccountManager {
    private static final String ACCOUNT_FILE = "accounts.txt";

    public BankAccountManager() {
        File file = new File(ACCOUNT_FILE);

        try {
            // Create accounts.txt automatically if missing
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error creating accounts file.");
        }
    }

    //Creates one checking account for a new user
    public boolean createAccountForUser(int userId) {
        if (getAccountByUserId(userId) != null) {
            return false;
        }

        int accountId = getNextAccountId();
        BankAccount account = new BankAccount(accountId, userId, 0.0, "Checking", "Active");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNT_FILE, true))) {
            writer.write(account.toString());
            writer.newLine();
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    // Finds an account by user ID
    public BankAccount getAccountByUserId(int userId) {
        List<BankAccount> accounts = loadAccounts();

        for (BankAccount account : accounts) {
            if (account.getUserId() == userId) {
                return account;
            }
        }

        return null;
    }


    // Saves an updated account by rewriting accounts.txt
    public boolean saveAccount(BankAccount updatedAccount) {
        List<BankAccount> accounts = loadAccounts();

        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getAccountId() == updatedAccount.getAccountId()) {
                accounts.set(i, updatedAccount);
                return saveAllAccounts(accounts);
            }
        }

        return false;
    }

    // Finds the next account ID.
    private int getNextAccountId() {
        List<BankAccount> accounts = loadAccounts();
        int maxId = 0;

        for (BankAccount account : accounts) {
            if (account.getAccountId() > maxId) {
                maxId = account.getAccountId();
            }
        }

        return maxId + 1;
    }

    // Loads all accounts from accounts.txt
    public List<BankAccount> loadAccounts() {
        List<BankAccount> accounts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNT_FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 5) {
                    int accountId = Integer.parseInt(parts[0]);
                    int userId = Integer.parseInt(parts[1]);
                    double balance = Double.parseDouble(parts[2]);
                    String accountType = parts[3];
                    String status = parts[4];

                    accounts.add(new BankAccount(accountId, userId, balance, accountType, status));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading accounts file.");
        }

        return accounts;
    }

    // Rewrites the entire accounts.txt file
    private boolean saveAllAccounts(List<BankAccount> accounts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNT_FILE))) {
            for (BankAccount account : accounts) {
                writer.write(account.toString());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Finds an account by account ID.
    public BankAccount getAccountById(int accountId) {
        List<BankAccount> accounts = loadAccounts();

        for (BankAccount account : accounts) {
            if (account.getAccountId() == accountId) {
                return account;
            }
        }

        return null;
    }
}
