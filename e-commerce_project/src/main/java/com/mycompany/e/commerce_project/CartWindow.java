package com.mycompany.e.commerce_project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CartWindow extends JFrame implements ActionListener {
    private JTable cartTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    private JButton removeBtn, checkoutBtn, closeBtn;
    private DefaultListModel<Product> listModel;
    private homepage homePage;
    private User currentUser;

    public CartWindow(DefaultListModel<Product> listModel, homepage homePage, User currentUser) {
        this.listModel = listModel;
        this.homePage = homePage;
        this.currentUser = currentUser;

        setTitle("Your Cart");
        setSize(500, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        // Table Setup
        String[] columns = { "Product Name", "Price" };
        tableModel = new DefaultTableModel(columns, 0);
        cartTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(cartTable);
        scrollPane.setBounds(20, 20, 440, 200);
        add(scrollPane);

        // Total Label
        totalLabel = new JLabel("Total: ₱0.00");
        totalLabel.setBounds(20, 230, 200, 30);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(totalLabel);

        // Buttons
        removeBtn = new JButton("Remove Item");
        removeBtn.setBounds(20, 270, 130, 35);
        removeBtn.addActionListener(this);
        add(removeBtn);

        checkoutBtn = new JButton("Checkout");
        checkoutBtn.setBounds(170, 270, 130, 35);
        checkoutBtn.addActionListener(this);
        add(checkoutBtn);

        closeBtn = new JButton("Close");
        closeBtn.setBounds(320, 270, 130, 35);
        closeBtn.addActionListener(this);
        add(closeBtn);

        loadCartItems();
    }

    private void loadCartItems() {
        tableModel.setRowCount(0);
        double total = 0;
        for (int i = 0; i < listModel.size(); i++) {
            Product p = listModel.getElementAt(i);
            tableModel.addRow(new Object[] { p.getName(), "₱" + p.getPrice() });
            total += p.getPrice();
        }
        totalLabel.setText("Total: ₱" + String.format("%.2f", total));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == closeBtn) {
            dispose();
        } else if (e.getSource() == removeBtn) {
            int selectedRow = cartTable.getSelectedRow();
            if (selectedRow != -1) {
                Product p = listModel.getElementAt(selectedRow);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Remove " + p.getName() + " from cart?", "Confirm Remove",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    listModel.remove(selectedRow);
                    loadCartItems(); // Refresh table and total
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an item to remove.");
            }
        } else if (e.getSource() == checkoutBtn) {
            if (listModel.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Cart is empty!");
                return;
            }
            double total = 0;
            ArrayList<Product> cartItems = new ArrayList<>();
            for (int i = 0; i < listModel.size(); i++) {
                Product p = listModel.getElementAt(i);
                total += p.getPrice();
                cartItems.add(p);
            }

            // Open Payment Window
            new PaymentWindow(total, cartItems, homePage, currentUser).setVisible(true);
            dispose(); // Close cart window
        }
    }
}
