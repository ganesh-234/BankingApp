public class Transaction {
    private int transactionId;
    private int accountId;
    private String type;
    private double amount;
    private String date;
    private String status;

    /*
     * Transaction represents one deposit or withdrawal.
     * Status can be:
     * NORMAL   = completed normal transaction
     * FLAGGED  = suspicious transaction waiting for admin review
     * REVIEWED = approved by admin
     */


    public Transaction(int transactionId, int accountId, String type, double amount, String date, String status) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return transactionId + "," + accountId + "," + type + "," + amount + "," + date + "," + status;
    }

    public String display() {
        return type + " $" + amount + " on " + date + " [" + status + "]";
    }
}