/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.e.commerce_project;

import javax.swing.*;

/**
 *
 * @author paulv
 */
public class signupwindow extends JFrame implements java.awt.event.ActionListener {
    private JLabel nameLabel, emailLabel, passwordLabel;
    private JTextField nameField, emailField;
    private JPasswordField passField;
    private JButton signupBtn, backBtn;

    public signupwindow() {
        setTitle("Sign Up");
        setSize(500, 350);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 40, 100, 30);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(150, 40, 270, 30);
        add(nameField);

        emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 90, 100, 30);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(150, 90, 270, 30);
        add(emailField);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 140, 100, 30);
        add(passwordLabel);

        passField = new JPasswordField();
        passField.setBounds(150, 140, 270, 30);
        add(passField);

        signupBtn = new JButton("Sign Up");
        signupBtn.setBounds(150, 200, 100, 40);
        signupBtn.addActionListener(this);
        add(signupBtn);

        backBtn = new JButton("Back");
        backBtn.setBounds(270, 200, 100, 40);
        backBtn.addActionListener(this);
        add(backBtn);

    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() == backBtn) {
            new loginwindow().setVisible(true);
            this.dispose();
        } else if (e.getSource() == signupBtn) {
            registerUser();
        }
    }

    private void registerUser() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = String.valueOf(passField.getPassword());

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        String query = "INSERT INTO users (name, email, password, isAdmin) VALUES (?, ?, ?, ?)";

        try (java.sql.Connection conn = DatabaseConnection.getConnection();
                java.sql.PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setBoolean(4, false); // Default not admin

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Registration Successful!");
                new loginwindow().setVisible(true);
                this.dispose();
            }
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
