import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static final String USER_FILE = "users.txt";

    public UserManager() {
        File file = new File(USER_FILE);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error creating users file.");
        }
    }

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

    public User loginCustomer(String username, String password) {
        List<User> users = loadUsers();

        for (User user : users) {
            if (user.login(username, password) && user.getRole().equals("Customer")) {
                return user;
            }
        }

        return null;
    }

    public boolean userExists(String username) {
        List<User> users = loadUsers();

        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }

        return false;
    }

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
}
