import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
// import java.util.Arrays;

public class User_Authentication extends JFrame implements ActionListener {
    // --- UI Components ---
    private JLabel usernameLabel, passwordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField; // Used for secure password entry
    private JButton registerButton, loginButton, viewUsersButton, deleteUserButton, clearButton, exitButton;
    private JPanel panel;

    // --- Data Storage ---
    // ArrayList to store user data: [username, password]
    private ArrayList<String[]> users = new ArrayList<>();

    /**
     * Constructor for the User Authentication system.
     */
    public User_Authentication() {
        // --- Window Setup ---
        setTitle("User Authentication System");
        setSize(450, 250); // Adjusted size for fewer components
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // --- Component Initialization ---
        usernameLabel = new JLabel("Username:");
        passwordLabel = new JLabel("Password:");
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);

        // --- Button Creation ---
        registerButton = createButton("Register");
        loginButton = createButton("Login");
        viewUsersButton = createButton("View Users");
        deleteUserButton = createButton("Delete User");
        clearButton = createButton("Clear");
        exitButton = createButton("Exit");

        // --- Panel and Layout ---
        // Using a 4x2 grid for a cleaner layout
        panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add components to the panel
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);

        panel.add(registerButton);
        panel.add(loginButton);
        panel.add(viewUsersButton);
        panel.add(deleteUserButton);
        panel.add(clearButton);
        panel.add(exitButton);

        // Add the panel to the frame
        add(panel);
        setVisible(true);
    }

    /**
     * Helper method to create and add an action listener to a button.
     */
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.addActionListener(this);
        return button;
    }

    /**
     * Handles all button click events.
     */
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == registerButton) {
                registerUser();
            } else if (e.getSource() == loginButton) {
                loginUser();
            } else if (e.getSource() == viewUsersButton) {
                viewUsers();
            } else if (e.getSource() == deleteUserButton) {
                deleteUser();
            } else if (e.getSource() == clearButton) {
                clearFields();
            } else if (e.getSource() == exitButton) {
                System.exit(0);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Registers a new user.
     */
    private void registerUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Username and password cannot be empty!");
            return;
        }

        // Check if user already exists
        for (String[] user : users) {
            if (user[0].equalsIgnoreCase(username)) {
                showMessage("Username already exists. Please choose another.");
                return;
            }
        }

        users.add(new String[]{username, password});
        showMessage("User registered successfully!");
        clearFields();
    }

    /**
     * Logs a user in.
     */
    private void loginUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        for (String[] user : users) {
            if (user[0].equalsIgnoreCase(username) && user[1].equals(password)) {
                showMessage("Login successful! Welcome, " + username + ".");
                clearFields();
                return;
            }
        }

        showMessage("Invalid username or password.");
    }

    /**
     * Displays a list of all registered usernames.
     */
    private void viewUsers() {
        if (users.isEmpty()) {
            showMessage("No users are registered yet.");
            return;
        }
        
        String[] columns = {"Registered Usernames"};
        Object[][] data = new Object[users.size()][1];
        for (int i = 0; i < users.size(); i++) {
            data[i][0] = users.get(i)[0]; // Only show username, not password
        }
        
        JTable table = new JTable(data, columns);
        showTable(table, "Registered Users");
    }

    /**
     * Deletes a user.
     */
    private void deleteUser() {
        String usernameToDelete = JOptionPane.showInputDialog(this, "Enter username to delete:");
        if (usernameToDelete == null || usernameToDelete.trim().isEmpty()) {
            return; // User cancelled or entered nothing
        }

        boolean removed = users.removeIf(user -> user[0].equalsIgnoreCase(usernameToDelete.trim()));

        if (removed) {
            showMessage("User '" + usernameToDelete + "' deleted successfully.");
        } else {
            showMessage("User '" + usernameToDelete + "' not found.");
        }
    }

    /**
     * Clears the input fields.
     */
    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }
    
    /**
     * Helper method to show a simple message dialog.
     */
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    /**
     * Helper method to show data in a JTable within a dialog.
     */
    private void showTable(JTable table, String title) {
        JOptionPane.showMessageDialog(this, new JScrollPane(table), title, JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Main method to run the application.
     */
    public static void main(String[] args) {
        new User_Authentication();
    }
}
