package com.mycompany.e.commerce_project;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author paulv
 */
public class loginwindow extends JFrame implements ActionListener {
    private JLabel email, password, messageLabel;
    private JTextField emailField;
    private JPasswordField passField;
    private JButton loginBtn, resetBtn;

    public loginwindow() {

        setTitle("Log In");
        setSize(500, 300);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        email = new JLabel("Username:");
        email.setBounds(50, 50, 100, 30);
        add(email);

        password = new JLabel("Password:");
        password.setBounds(50, 100, 100, 30);
        add(password);

        emailField = new JTextField();
        emailField.setBounds(150, 50, 270, 30);
        add(emailField);

        passField = new JPasswordField();
        passField.setBounds(150, 100, 270, 30);
        add(passField);

        resetBtn = new JButton("Reset");
        resetBtn.setBounds(250, 150, 100, 40);
        resetBtn.addActionListener(this);
        add(resetBtn);

        loginBtn = new JButton("Log In");
        loginBtn.setBounds(150, 150, 100, 40);
        loginBtn.addActionListener(this);
        add(loginBtn);

        JButton signupLinkBtn = new JButton("Create Account");
        signupLinkBtn.setBounds(150, 200, 200, 30);
        signupLinkBtn.addActionListener(e -> {
            new signupwindow().setVisible(true);
            this.dispose();
        });
        add(signupLinkBtn);

        messageLabel = new JLabel();
        messageLabel.setBounds(50, 240, 300, 30);
        add(messageLabel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetBtn) {
            emailField.setText("");
            passField.setText("");
        }

        if (e.getSource() == loginBtn) {
            String userID = emailField.getText(); // Using this as email or username
            String password = String.valueOf(passField.getPassword());

            User user = authenticate(userID, password);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "Login Successful");
                homepage HomePage = new homepage(user); // Pass user to homepage
                HomePage.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password.", "Login Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private User authenticate(String username, String password) {
        User user = null;
        String query = "SELECT * FROM users WHERE (email = ? OR name = ?) AND password = ?";

        try (java.sql.Connection conn = DatabaseConnection.getConnection();
                java.sql.PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, username);
            stmt.setString(3, password);

            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getBoolean("isAdmin"));
                }
            }
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
        return user;
    }
}
