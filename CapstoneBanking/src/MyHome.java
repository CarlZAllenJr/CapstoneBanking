import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MyHome extends JFrame{
    private JTextArea transactionHistory;

    // Create text fields to display user details
    private JTextField userIdField;
    private JTextField accountNumberField;
    private JTextField accountTypeField;
    private JTextField checkingBalanceField;
    private JTextField savingBalanceField;
    private JTextField amountInput;

    public MyHome() {
        setTitle("Capstone Banking Solutions: My Home Page");
        getContentPane().setBackground(Color.decode("#28282B"));

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);

        initUI();
        addTransactionComponents();

        // Create text fields for user details
        userIdField = new JTextField();
        accountNumberField = new JTextField();
        accountTypeField = new JTextField();
        checkingBalanceField = new JTextField();
        savingBalanceField = new JTextField();

        // Set them to non-editable
        userIdField.setEditable(false);
        userIdField.setBackground(Color.LIGHT_GRAY);
        accountNumberField.setEditable(false);
        accountNumberField.setBackground(Color.LIGHT_GRAY);
        accountTypeField.setEditable(false);
        accountTypeField.setBackground(Color.LIGHT_GRAY);
        checkingBalanceField.setEditable(false);
        checkingBalanceField.setBackground(Color.LIGHT_GRAY);
        savingBalanceField.setEditable(false);
        savingBalanceField.setBackground(Color.LIGHT_GRAY);

        // Set bounds for text fields
        userIdField.setBounds(455, 50, 200, 30);
        accountNumberField.setBounds(455, 90, 200, 30);
        accountTypeField.setBounds(455, 130, 200, 30);
        checkingBalanceField.setBounds(455, 170, 200, 30);
        savingBalanceField.setBounds(455, 210, 200, 30);

        // Add them to the frame
        add(userIdField);
        add(accountNumberField);
        add(accountTypeField);
        add(checkingBalanceField);
        add(savingBalanceField);
    }
    public void initializeUserDetails(String username) {
        int userId = getUserIdFromDatabase(username);
        User userDetails = getUserDetails(userId);
        if (userDetails != null) {
            userIdField.setText(String.valueOf(userDetails.getUserId()));
            accountNumberField.setText(String.valueOf(userDetails.getAccountNumber()));
            accountTypeField.setText(userDetails.getAccountType());
            checkingBalanceField.setText(String.valueOf(userDetails.getBalance()));

            // Fetch and display transaction history
            displayTransactionHistory(userDetails.getAccountNumber());
        }
    }

    // Add a method to get user ID from the database
    private int getUserIdFromDatabase(String username) {
        try {
            String url = "jdbc:mysql://localhost:3306/capstone_banking";
            String dbUsername = "root";
            String dbPassword = "NarutoUzumaki25!";

            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);

            String sql = "SELECT user_id FROM users WHERE username=?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("user_id");
            }

            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return -1; // Return -1 if user ID is not found
    }

    private MyHome.User getUserDetails(int userId) {
        try {
            String url = "jdbc:mysql://localhost:3306/capstone_banking";
            String username = "root";
            String password = "NarutoUzumaki25!";

            Connection connection = DriverManager.getConnection(url, username, password);

            String sql = "SELECT users.user_id, accounts.account_number, users.account_type_1, users.balance " +
                    "FROM users " +
                    "INNER JOIN accounts ON users.user_id = accounts.user_id " +
                    "WHERE users.user_id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int fetchedUserId = resultSet.getInt("user_id");
                int accountNumber = resultSet.getInt("account_number");
                String accountType = resultSet.getString("account_type_1");
                double balance = resultSet.getDouble("balance");

                return new User(fetchedUserId, accountNumber, accountType, balance);
            }

            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private void initUI() {
        setLayout(null);

        Font labelFont = new Font("Lexend", Font.PLAIN, 16);

        String[] labels = {"User ID: ", "Account Number: ", "Account Type: ", "Checking Balance: ", "Savings Balance: "};
        int yOffset = 50;

        // For loop to add labels to page
        for (String labelText : labels) {
            JLabel label = new JLabel(labelText);
            label.setFont(labelFont);
            label.setForeground(Color.WHITE);
            label.setBounds(250, yOffset, 200, 30);
            label.setHorizontalAlignment(SwingConstants.RIGHT);
            add(label);
            yOffset += 40;
        }

        // Add logo to page
        JLabel lblLogo = new JLabel(new ImageIcon(new ImageIcon("C:\\Users\\merci\\Downloads\\Capstone Banking.png")
                .getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
        lblLogo.setBounds(25, 0, 200, 200);
        add(lblLogo);

        addComponents();
    }

    private void addComponents(){
        // Add buttons using a loop
        String[] buttonLabels = {"Deposit", "Withdraw", "Logout"};
        JButton[] buttons = new JButton[buttonLabels.length];

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(buttonLabels[i]);
            buttons[i].setBounds(300 + (i * 120), 500, 100, 30);
            add(buttons[i]);
        }

        addActions(buttons);
    }

    private void addActions(JButton[] buttons) {
        buttons[0].addActionListener(e -> processTransaction("Deposit"));
        buttons[1].addActionListener(e -> processTransaction("Withdraw"));
        buttons[2].addActionListener(e -> handleLogout());
    }

    private void handleLogout(){
        // Close the MyHome window
        dispose();

        // Open the Authentication page
        new Authentication();
    }

    private void addTransactionComponents() {
        transactionHistory = new JTextArea();
        transactionHistory.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(transactionHistory);
        scrollPane.setBounds(50, 250, 300, 200);
        add(scrollPane);

        // Add a text field for user input
        amountInput = new JTextField();
        amountInput.setBounds(525, 330, 100, 30);
        add(amountInput);

        // Add a label to indicate the purpose of the input field
        JLabel lblAmount = new JLabel("Enter Amount:");
        lblAmount.setFont(new Font("Lexend", Font.PLAIN, 16));
        lblAmount.setForeground(Color.WHITE);
        lblAmount.setBounds(400, 330, 120, 30);
        lblAmount.setHorizontalAlignment(SwingConstants.RIGHT);
        add(lblAmount);

        String[] buttonLabels = {"Deposit", "Withdraw", "Logout"};
        JButton[] buttons = new JButton[buttonLabels.length];

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(buttonLabels[i]);
            buttons[i].setBounds(300 + (i * 120), 500, 100, 30);
            add(buttons[i]);
        }

        addActions(buttons);
    }

    private void processTransaction(String type) {
        double amount = Double.parseDouble(amountInput.getText());

        String transaction = "";

        if (type.equals("Deposit")) {
            transaction += "Deposit: + $" + amount + "\n";
            // Update database with deposit amount
            updateBalanceInDatabase(amount);
            // Insert transaction into the database
            insertTransaction(Integer.parseInt(accountNumberField.getText()), "Deposit", amount);
        } else if (type.equals("Withdraw")) {
            transaction += "Withdraw: - $" + amount + "\n";
            // Update database with withdrawal amount (remember to handle overdraft if applicable)
            updateBalanceInDatabase(-amount); // Use negative amount for withdrawal
            // Insert transaction into the database
            insertTransaction(Integer.parseInt(accountNumberField.getText()), "Withdraw", amount);
        }

        // Example code for updating transaction history
        transactionHistory.append(transaction);
    }

    private void updateBalanceInDatabase(double amount) {
        try {
            String url = "jdbc:mysql://localhost:3306/capstone_banking";
            String username = "root";
            String password = "NarutoUzumaki25!";

            Connection connection = DriverManager.getConnection(url, username, password);

            String sql = "UPDATE users SET balance = balance + ? WHERE user_id = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDouble(1, amount);
            statement.setInt(2, Integer.parseInt(userIdField.getText()));

            statement.executeUpdate();

            // Update the checkingBalanceField in the GUI
            double newBalance = Double.parseDouble(checkingBalanceField.getText()) + amount;
            checkingBalanceField.setText(String.valueOf(newBalance));

            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void insertTransaction(int account_number, String transaction_type, double amount) {
        try {
            String url = "jdbc:mysql://localhost:3306/capstone_banking";
            String username = "root";
            String password = "NarutoUzumaki25!";

            Connection connection = DriverManager.getConnection(url, username, password);

            String sql = "INSERT INTO transactions (account_number, transaction_type, amount, date_time) VALUES (?, ?, ?, NOW())";

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, account_number);
            statement.setString(2, transaction_type);
            statement.setDouble(3, amount);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating transaction failed, no rows affected.");
            }

            // Retrieve the auto-generated transaction_id
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int transactionId = generatedKeys.getInt(1);
                System.out.println("Generated transaction ID: " + transactionId);
            } else {
                throw new SQLException("Creating transaction failed, no ID obtained.");
            }

            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void displayTransactionHistory(int accountNumber) {
        try {
            String url = "jdbc:mysql://localhost:3306/capstone_banking";
            String username = "root";
            String password = "NarutoUzumaki25!";

            Connection connection = DriverManager.getConnection(url, username, password);

            String sql = "SELECT * FROM transactions WHERE account_number = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, accountNumber);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String transactionType = resultSet.getString("transaction_type");
                double amount = resultSet.getDouble("amount");
                String dateTime = resultSet.getString("date_time");

                String transaction = transactionType + ": $" + amount + " at " + dateTime + "\n";
                transactionHistory.append(transaction);
            }

            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args){
        MyHome myHome = new MyHome();
        myHome.setVisible(true);
    }

    // User.java
    public class User {
        private int userId;
        private int accountNumber;
        private String accountType;
        private double balance;

        public User(int userId, int accountNumber, String accountType, double balance) {
            this.userId = userId;
            this.accountNumber = accountNumber;
            this.accountType = accountType;
            this.balance = balance;
        }

        public int getUserId() {
            return userId;
        }

        public int getAccountNumber() {
            return accountNumber;
        }

        public String getAccountType() {
            return accountType;
        }
        /*public String getsavingBalanceField() {
            return savingBalanceField();
        }*/

        public double getBalance() {
            return balance;
        }
    }
}
