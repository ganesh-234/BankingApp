import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
 * UserManager handles all user-related file operations:
 * - creating users
 * - checking duplicates
 * - logging in customers/admins
 * - loading users from users.txt
 */

public class UserManager {
    private static final String USER_FILE = "users.txt";

    public UserManager() {
        File file = new File(USER_FILE);

        try {
            // Create users.txt automatically if it does not exist.
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error creating users file.");
        }

        // Ensures there is always one admin account for testing.
        ensureDefaultAdmin();
    }

    private void ensureDefaultAdmin() {
        List<User> users = loadUsers();

        for (User user : users) {
            if (user.getRole().equalsIgnoreCase("Admin")
                    || user.getRole().equalsIgnoreCase("MasterAdmin")) {
                return;
            }
        }

        User admin = new User(1, "admin", "admin", "Admin");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            writer.write(admin.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error creating default admin.");
        }
    }


    // Registers a new customer.
    public boolean registerCustomer(String username, String password) {
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            return false;
        }

        if (userExists(username)) {
            return false;
        }

        int newUserId = getNextUserId();
        User user = new User(newUserId, username, password, "Customer");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            writer.write(user.toString());
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("Error writing user data.");
            return false;
        }
    }


    // log in customers
    public User loginCustomer(String username, String password) {
        List<User> users = loadUsers();

        for (User user : users) {
            if (user.login(username, password) && user.getRole().equals("Customer")) {
                return user;
            }
        }

        return null;
    }


    // prevents duplicate users (if uppercase and lowercase names count as duplicates)
    public boolean userExists(String username) {
        List<User> users = loadUsers();

        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }

        return false;
    }


    //Finds the next available user ID
    public int getNextUserId() {
        List<User> users = loadUsers();
        int maxId = 0;

        for (User user : users) {
            if (user.getUserId() > maxId) {
                maxId = user.getUserId();
            }
        }

        return maxId + 1;
    }


    // Loads all users from users.txt
    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 4) {
                    int userId = Integer.parseInt(parts[0]);
                    String username = parts[1];
                    String password = parts[2];
                    String role = parts[3];

                    users.add(new User(userId, username, password, role));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading users file.");
        }

        return users;
    }


    //admin login
    public User loginAdmin(String username, String password) {
        List<User> users = loadUsers();

        for (User user : users) {
            if (user.login(username, password) &&
                    (user.getRole().equals("Admin") || user.getRole().equals("MasterAdmin"))) {
                return user;
            }
        }

        return null;
    }

    // Finds a user using their unique ID.
    public User getUserById(int userId) {
        List<User> users = loadUsers();

        for (User user : users) {
            if (user.getUserId() == userId) {
                return user;
            }
        }

        return null;
    }

}
