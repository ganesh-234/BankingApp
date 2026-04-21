public class AdminAccount extends User {

    public AdminAccount(int userId, String username, String password) {
        super(userId, username, password, "Admin");
    }

    public void freezeAccount() {
        System.out.println("Freeze account feature not implemented yet.");
    }

    public void unfreezeAccount() {
        System.out.println("Unfreeze account feature not implemented yet.");
    }

    public void reverseTransaction() {
        System.out.println("Reverse transaction feature not implemented yet.");
    }

    public void createAdmin() {
        System.out.println("Create admin feature not implemented yet.");
    }
}
