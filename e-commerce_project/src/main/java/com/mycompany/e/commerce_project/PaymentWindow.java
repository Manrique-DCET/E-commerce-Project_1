package com.mycompany.e.commerce_project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class PaymentWindow extends JFrame implements ActionListener {
    private JLabel totalLabel, cardLabel, expLabel, cvvLabel;
    private JTextField cardField, expField, cvvField;
    private JButton payBtn, cancelBtn;
    private double totalAmount;
    private ArrayList<Product> cartItems;
    private homepage homePage; // Reference to update cart

    public PaymentWindow(double totalAmount, ArrayList<Product> cartItems, homepage homePage) {
        this.totalAmount = totalAmount;
        this.cartItems = cartItems;
        this.homePage = homePage;

        setTitle("Payment");
        setSize(400, 350);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        totalLabel = new JLabel("Total to Pay: ₱" + String.format("%.2f", totalAmount));
        totalLabel.setBounds(30, 20, 300, 30);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(totalLabel);

        cardLabel = new JLabel("Card Number:");
        cardLabel.setBounds(30, 70, 100, 25);
        add(cardLabel);

        cardField = new JTextField();
        cardField.setBounds(140, 70, 200, 25);
        add(cardField);

        expLabel = new JLabel("Expiry Date:");
        expLabel.setBounds(30, 110, 100, 25);
        add(expLabel);

        expField = new JTextField();
        expField.setBounds(140, 110, 100, 25);
        add(expField);

        cvvLabel = new JLabel("CVV:");
        cvvLabel.setBounds(30, 150, 100, 25);
        add(cvvLabel);

        cvvField = new JTextField();
        cvvField.setBounds(140, 150, 100, 25);
        add(cvvField);

        payBtn = new JButton("Pay Now");
        payBtn.setBounds(80, 220, 100, 35);
        payBtn.addActionListener(this);
        add(payBtn);

        cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(200, 220, 100, 35);
        cancelBtn.addActionListener(this);
        add(cancelBtn);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == payBtn) {
            processPayment();
        } else if (e.getSource() == cancelBtn) {
            dispose();
        }
    }

    private void processPayment() {
        if (cardField.getText().isEmpty() || expField.getText().isEmpty() || cvvField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        // Mock payment processing...
        boolean paymentSuccess = true;

        if (paymentSuccess) {
            if (saveOrderToDB()) {
                JOptionPane.showMessageDialog(this, "Payment Successful! Order placed.");
                homePage.clearCart(); // Clear the cart in homepage
                homePage.loadProductsFromDB(); // Refresh product list to show updated quantities
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Payment processed but failed to save order.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean saveOrderToDB() {
        // Assume user ID 1 (Admin) for now since we don't have session management fully
        // implemented passing user IDs around
        // In a real app, you'd pass the current logged-in user's ID.
        int userId = 1;

        String insertOrder = "INSERT INTO orders (userId, totalPrice) VALUES (?, ?)";
        String insertOrderItem = "INSERT INTO order_items (orderId, productId, quantity, price) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Transaction

            // 1. Insert Order
            PreparedStatement orderStmt = conn.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setInt(1, userId);
            orderStmt.setDouble(2, totalAmount);
            orderStmt.executeUpdate();

            ResultSet rs = orderStmt.getGeneratedKeys();
            int orderId = -1;
            if (rs.next()) {
                orderId = rs.getInt(1);
            }

            // 2. Insert Order Items & Update Product Quantity
            PreparedStatement itemStmt = conn.prepareStatement(insertOrderItem);
            String updateProductSql = "UPDATE products SET quantity = quantity - ? WHERE id = ?";
            PreparedStatement updateQtyStmt = conn.prepareStatement(updateProductSql);

            for (Product p : cartItems) {
                // Insert Item
                itemStmt.setInt(1, orderId);
                itemStmt.setInt(2, p.getId());
                itemStmt.setInt(3, 1);
                itemStmt.setDouble(4, p.getPrice());
                itemStmt.addBatch();

                // Update Quantity
                updateQtyStmt.setInt(1, 1); // Decrease by 1
                updateQtyStmt.setInt(2, p.getId());
                updateQtyStmt.addBatch();
            }
            itemStmt.executeBatch();
            updateQtyStmt.executeBatch();

            conn.commit();

            // Refresh homepage product list if possible?
            // Since we don't have a direct reference to reload, the user might need to
            // restart or we assume the homepage refreshes on specific actions.
            // But we do have a reference to homePage!
            // homePage.loadProductsFromDB(); // If this method was public...

            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                if (conn != null)
                    conn.rollback();
            } catch (Exception e) {
            }
            return false;
        } finally {
            try {
                if (conn != null)
                    conn.setAutoCommit(true);
                conn.close();
            } catch (Exception e) {
            }
        }
    }
}
