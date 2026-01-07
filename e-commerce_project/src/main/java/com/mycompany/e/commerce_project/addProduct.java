package com.mycompany.e.commerce_project;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;;

public class addProduct extends JFrame implements ActionListener {
    private JTextField nameField, priceField, quantityField, ratingField;
    private JButton addProductBtn;
    private JLabel nameLabel, priceLabel, quantityLabel, ratingLabel;
    private DefaultTableModel productTableModel;
    private User currentUser;

    public addProduct(DefaultTableModel productTableModel, User currentUser) {
        this.productTableModel = productTableModel;
        this.currentUser = currentUser;

        setTitle("Add Product");
        setSize(400, 300);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        nameLabel = new JLabel("Product Name:");
        nameLabel.setBounds(30, 30, 100, 25);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(150, 30, 200, 25);
        add(nameField);

        priceLabel = new JLabel("Price:");
        priceLabel.setBounds(30, 70, 100, 25);
        add(priceLabel);

        priceField = new JTextField();
        priceField.setBounds(150, 70, 200, 25);
        add(priceField);

        quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(30, 110, 100, 25);
        add(quantityLabel);

        quantityField = new JTextField();
        quantityField.setBounds(150, 110, 200, 25);
        add(quantityField);

        ratingLabel = new JLabel("Rating:");
        ratingLabel.setBounds(30, 150, 100, 25);
        add(ratingLabel);

        ratingField = new JTextField();
        ratingField.setBounds(150, 150, 200, 25);
        add(ratingField);

        addProductBtn = new JButton("Add Product");
        addProductBtn.setBounds(150, 200, 120, 30);
        addProductBtn.addActionListener(this);
        add(addProductBtn);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name = nameField.getText();
        String priceStr = priceField.getText();
        String quantityStr = quantityField.getText();
        String ratingStr = ratingField.getText();

        if (name.isEmpty() || priceStr.isEmpty() || quantityStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int quantity = Integer.parseInt(quantityStr);
            double rating = ratingStr.isEmpty() ? 0.0 : Double.parseDouble(ratingStr);

            String query = "INSERT INTO products (name, price, quantity, category, rating, addedBy) VALUES (?, ?, ?, ?, ?, ?)";

            try (java.sql.Connection conn = DatabaseConnection.getConnection();
                    java.sql.PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, name);
                stmt.setDouble(2, price);
                stmt.setInt(3, quantity);
                stmt.setString(4, "General"); // Default category
                stmt.setDouble(5, rating);
                stmt.setString(6, currentUser != null ? currentUser.getName() : "Admin");

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    productTableModel.addRow(new Object[] { name, "₱" + price, quantityStr,
                            currentUser != null ? currentUser.getName() : "Admin", rating });
                    JOptionPane.showMessageDialog(this, "Product added successfully!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid price, quantity, or rating.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }
}
