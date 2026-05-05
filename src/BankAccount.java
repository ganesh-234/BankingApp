
// BankAccount represents a customer's actual banking account.


public class BankAccount {
    private int accountId;
    private int userId;
    private double balance;
    private String accountType;
    private String status;

    public BankAccount(int accountId, int userId, double balance, String accountType, String status) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
        this.accountType = accountType;
        this.status = status;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getUserId() {
        return userId;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getStatus() {
        return status;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return accountId + "," + userId + "," + balance + "," + accountType + "," + status;
    }
}