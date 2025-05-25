import java.io.*;
import java.util.*;

/**
 * Represents a User with a username and password.
 */
class User {
    private String username;
    private String password;

    /**
     * Constructs a new User.
     *
     * @param username the username
     * @param password the password (plain text or hashed)
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Converts user to a file-storable format.
     */
    public String toFileString() {
        return username + "," + password;
    }

    /**
     * Parses a User from a file line.
     */
    public static User fromFileString(String line) {
        String[] parts = line.split(",");
        return new User(parts[0], parts[1]);
    }
}


/**
 * Handles file read/write operations for User data.
 */
class FileUtil {
    private static final String FILE_NAME = "users.txt";

    /**
     * Reads users from the file.
     */
    public static List<User> readUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                users.add(User.fromFileString(line));
            }
        } catch (IOException e) {
            // File might not exist yet
        }
        return users;
    }

    /**
     * Writes users to the file.
     */
    public static void writeUsers(List<User> users) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (User user : users) {
                bw.write(user.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }
}


/**
 * Provides business logic for user operations.
 */
class UserService {
    private List<User> users;

    public UserService() {
        users = FileUtil.readUsers();
    }

    /**
     * Registers a new user.
     */
    public boolean register(String username, String password) {
        if (getUserByUsername(username) != null) return false;
        users.add(new User(username, password));
        FileUtil.writeUsers(users);
        return true;
    }

    /**
     * Validates user credentials.
     */
    public boolean login(String username, String password) {
        User user = getUserByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    /**
     * Deletes a user by username.
     */
    public boolean deleteUser(String username) {
        User user = getUserByUsername(username);
        if (user != null) {
            users.remove(user);
            FileUtil.writeUsers(users);
            return true;
        }
        return false;
    }

    /**
     * Displays all usernames.
     */
    public void listUsers() {
        for (User user : users) {
            System.out.println("Username: " + user.getUsername());
        }
    }

    private User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) return user;
        }
        return null;
    }
}


/**
 * Controls user interaction and input handling.
 */
class AuthController {
    private UserService userService = new UserService();
    private Scanner scanner = new Scanner(System.in);

    /**
     * Starts the authentication system.
     */
    public void start() {
        while (true) {
            System.out.println("\n=== USER AUTH SYSTEM ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Delete User");
            System.out.println("4. List Users");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1": register(); break;
                case "2": login(); break;
                case "3": deleteUser(); break;
                case "4": userService.listUsers(); break;
                case "5": System.out.println("Goodbye!"); return;
                default: System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void register() {
        System.out.print("Enter new username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter new password: ");
        String password = scanner.nextLine().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Username and password cannot be empty.");
            return;
        }

        if (userService.register(username, password)) {
            System.out.println("User registered successfully.");
        } else {
            System.out.println("Username already exists.");
        }
    }

    private void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        if (userService.login(username, password)) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    private void deleteUser() {
        System.out.print("Enter username to delete: ");
        String username = scanner.nextLine().trim();

        if (userService.deleteUser(username)) {
            System.out.println("User deleted.");
        } else {
            System.out.println("User not found.");
        }
    }
}


/**
 * Entry point of the application.
 */
public class AuthApp {
    public static void main(String[] args) {
        new AuthController().start();
    }
}
