import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Authentication extends JFrame {
    private JLabel lblLogo;
    private JLabel lblUsername;
    private JLabel lblPassword;
    private JTextField txtPassword;
    private JTextField txtUsername;
    private JButton btnLogin;
    private JButton btnSignUp;

    public Authentication() {
        setTitle("Capstone Banking Solutions");
        initUI();
        addComponents();
        addActionListener();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initUI() {
        lblLogo = new JLabel(new ImageIcon(new ImageIcon("C:\\Users\\merci\\Downloads\\Capstone Banking.png")
                .getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH)));
        getContentPane().setBackground(Color.decode("#28282B"));

        lblUsername = new JLabel("Username:");
        lblPassword = new JLabel("Password:");

        Font labelFont = new Font("Lexend", Font.PLAIN, 16);
        lblUsername.setFont(labelFont);
        lblPassword.setFont(labelFont);

        lblUsername.setForeground(Color.WHITE);
        lblPassword.setForeground(Color.WHITE);

        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);

        btnLogin = new JButton("Login");
        btnSignUp = new JButton("Sign Up");
    }

    private void addComponents() {
        setLayout(null);
        lblLogo.setBounds(50, 50, 400, 400);
        lblUsername.setBounds(500, 200, 100, 30);
        lblPassword.setBounds(500, 250, 100, 30);
        txtUsername.setBounds(600, 200, 200, 30);
        txtPassword.setBounds(600, 250, 200, 30);
        btnLogin.setBounds(500, 350, 100, 30);
        btnSignUp.setBounds(700, 350, 100, 30);

        add(lblLogo);
        add(lblUsername);
        add(lblPassword);
        add(txtUsername);
        add(txtPassword);
        add(btnLogin);
        add(btnSignUp);
    }

    private void addActionListener() {
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = txtPassword.getText();

                if (checkLogin(username, password)) {
                    setVisible(false);
                    MyHome myHome = new MyHome();
                    myHome.initializeUserDetails(username); // Pass the username to initializeUserDetails
                    myHome.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnSignUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Account account = new Account();
                account.setVisible(true);
            }
        });
    }

    // Add a method to check login
    private boolean checkLogin(String username, String password) {
        try {
            String url = "jdbc:mysql://localhost:3306/capstone_banking";
            String dbUsername = "root";
            String dbPassword = "NarutoUzumaki25!";

            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);

            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            boolean isValid = resultSet.next();

            connection.close();

            return isValid;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }

    }

    public static void main(String[] args) {
        new Authentication();
    }
}
