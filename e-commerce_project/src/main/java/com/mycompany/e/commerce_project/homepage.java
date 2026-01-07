/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.e.commerce_project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 *
 * @author paulv
 */
public class homepage extends JFrame implements ActionListener {
    private JLabel logoName, searchFilter, price, rating;
    private JScrollPane scrollPane;
    private JTextField txtField;
    private JTextArea resultArea;
    private JButton searchBtn, cartBtn, signupBtn, loginBtn, addBtn, removeBtn, addCartBtn;
    private JRadioButton ascendBtn, descendBtn;
    private ButtonGroup group;
    private JCheckBox star5, star4, star3, star2, star1;
    private JTable table;
    private DefaultTableModel tableModel;
    private DefaultListModel<Product> listModel; // Changed to store Product objects
    private JList<Product> itemList;
    private java.util.ArrayList<Product> allProducts; // Cache for binary search
    private User currentUser;

    public homepage(User user) {
        this.currentUser = user;

        setTitle("ShopiPop");
        setSize(1280, 720);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        logoName = new JLabel("ShopiPop");
        logoName.setBounds(30, 20, 150, 40);
        Font currentFont = logoName.getFont();
        logoName.setFont(currentFont.deriveFont(30f));
        add(logoName);

        add(logoName);

        allProducts = new java.util.ArrayList<>();
        itemList = new JList<Product>();
        listModel = new DefaultListModel<Product>();

        txtField = new JTextField();
        txtField.setBounds(200, 25, 650, 35);
        add(txtField);

        searchBtn = new JButton("Search");
        searchBtn.setBounds(860, 25, 75, 35);
        searchBtn.addActionListener(this);
        add(searchBtn);

        cartBtn = new JButton("Cart");
        cartBtn.setBounds(980, 25, 70, 35);
        cartBtn.addActionListener(this);
        add(cartBtn);

        loginBtn = new JButton("Log Out");
        loginBtn.setBounds(1060, 25, 100, 35);
        loginBtn.addActionListener(this);
        add(loginBtn);

        // SEARCH FILTER
        searchFilter = new JLabel("SEARCH FILTER");
        searchFilter.setBounds(30, 100, 100, 30);
        add(searchFilter);

        price = new JLabel("Price Order:");
        price.setBounds(30, 135, 100, 30);
        add(price);

        ascendBtn = new JRadioButton("Ascending");
        ascendBtn.setBounds(30, 160, 100, 25);
        ascendBtn.addActionListener(this);
        add(ascendBtn);

        descendBtn = new JRadioButton("Descending");
        descendBtn.setBounds(30, 180, 100, 25);
        descendBtn.addActionListener(this);
        add(descendBtn);

        group = new ButtonGroup();
        group.add(ascendBtn);
        group.add(descendBtn);

        rating = new JLabel("Ratings:");
        rating.setBounds(30, 210, 100, 25);
        add(rating);

        star5 = new JCheckBox();
        star5.setText("5 star");
        star5.setBounds(30, 235, 100, 25);
        star5.addActionListener(this);
        add(star5);

        star4 = new JCheckBox();
        star4.setText("4 star");
        star4.setBounds(30, 255, 100, 25);
        star4.addActionListener(this);
        add(star4);

        star3 = new JCheckBox();
        star3.setText("3 star");
        star3.setBounds(30, 275, 100, 25);
        star3.addActionListener(this);
        add(star3);

        star2 = new JCheckBox();
        star2.setText("2 star");
        star2.setBounds(30, 295, 100, 25);
        star2.addActionListener(this);
        add(star2);

        star1 = new JCheckBox();
        star1.setText("1 star");
        star1.setBounds(30, 315, 100, 25);
        star1.addActionListener(this);
        add(star1);

        addBtn = new JButton("Add Product");
        addBtn.setBounds(30, 375, 150, 35);
        addBtn.addActionListener(this);
        add(addBtn);

        removeBtn = new JButton("Remove Product");
        removeBtn.setBounds(30, 415, 150, 35);
        removeBtn.addActionListener(this);
        add(removeBtn);

        addCartBtn = new JButton("Add to Cart");
        addCartBtn.setBounds(30, 455, 150, 35);
        addCartBtn.addActionListener(this);
        add(addCartBtn);

        // TABLE FOR PRODUCTS
        String[] columns = { "Product Name", "Price", "Quantity", "Added By", "Rating" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(200, 100, 1040, 560);
        add(scrollPane);

        loadProductsFromDB();
    }

    public void loadProductsFromDB() {
        tableModel.setRowCount(0);
        allProducts.clear();

        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM products");
        java.util.List<Integer> selectedRatings = new java.util.ArrayList<>();

        if (star5.isSelected())
            selectedRatings.add(5);
        if (star4.isSelected())
            selectedRatings.add(4);
        if (star3.isSelected())
            selectedRatings.add(3);
        if (star2.isSelected())
            selectedRatings.add(2);
        if (star1.isSelected())
            selectedRatings.add(1);

        if (!selectedRatings.isEmpty()) {
            queryBuilder.append(" WHERE FLOOR(rating) IN (");
            for (int i = 0; i < selectedRatings.size(); i++) {
                queryBuilder.append(selectedRatings.get(i));
                if (i < selectedRatings.size() - 1) {
                    queryBuilder.append(", ");
                }
            }
            queryBuilder.append(")");
        }

        if (ascendBtn.isSelected()) {
            queryBuilder.append(" ORDER BY price ASC");
        } else if (descendBtn.isSelected()) {
            queryBuilder.append(" ORDER BY price DESC");
        }

        try (java.sql.Connection conn = DatabaseConnection.getConnection();
                java.sql.Statement stmt = conn.createStatement();
                java.sql.ResultSet rs = stmt.executeQuery(queryBuilder.toString())) {

            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed!");
                return;
            }

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                String category = rs.getString("category");
                double rating = rs.getDouble("rating");
                String addedBy = rs.getString("addedBy");

                Product p = new Product(id, name, price, quantity, category, rating,
                        addedBy != null ? addedBy : "Admin");
                allProducts.add(p);
                tableModel.addRow(
                        new Object[] { name, "₱" + price, quantity, addedBy != null ? addedBy : "Admin", rating });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading products: " + e.getMessage());
        }
        // Sort for binary search initially
        // Note: Collections.sort handles natural ordering (likely by Price or Name
        // depending on Product compareTo)
        // If we heavily rely on DB sorting for display, we might might want to skip
        // this or ensure Product.compareTo usage matches expectation?
        // Staying with original logic:
        java.util.Collections.sort(allProducts);
    }

    public void clearCart() {
        listModel.clear();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addBtn) {
            new addProduct(tableModel, currentUser).setVisible(true);
        }
        if (e.getSource() == removeBtn) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                // Get product name (assuming names are unique or we should rely on ID if
                // possible)
                // Better to look up the Product object from our list
                String productName = (String) tableModel.getValueAt(selectedRow, 0);

                Product selectedProduct = null;
                for (Product p : allProducts) {
                    if (p.getName().equals(productName)) {
                        selectedProduct = p;
                        break;
                    }
                }

                if (selectedProduct != null) {
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Are you sure you want to delete " + productName + "?", "Confirm Delete",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try (java.sql.Connection conn = DatabaseConnection.getConnection();
                                java.sql.PreparedStatement stmt = conn
                                        .prepareStatement("DELETE FROM products WHERE id = ?")) {

                            stmt.setInt(1, selectedProduct.getId());
                            int rows = stmt.executeUpdate();
                            if (rows > 0) {
                                tableModel.removeRow(selectedRow);
                                allProducts.remove(selectedProduct);
                                listModel.removeElement(selectedProduct); // Also remove from cart if there
                                JOptionPane.showMessageDialog(this, "Product removed from database.");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(this, "Error deleting product: " + ex.getMessage());
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a product to remove.", "No Selection",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
        if (e.getSource() == addCartBtn) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                // Determine which product from allProducts matches the selected row
                // This assumes the table order matches allProducts or we search by unique name.
                // Since we sort allProducts but maybe not the table, we should find by name.
                String productName = (String) tableModel.getValueAt(selectedRow, 0);

                Product selectedProduct = null;
                for (Product p : allProducts) {
                    if (p.getName().equals(productName)) {
                        selectedProduct = p;
                        break;
                    }
                }

                if (selectedProduct != null) {
                    listModel.addElement(selectedProduct);
                    JOptionPane.showMessageDialog(this, productName + " has been added to your cart.", "Added to Cart",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a product to add to cart.", "No Selection",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
        if (e.getSource() == cartBtn) {
            if (listModel.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Your cart is empty.", "Cart", JOptionPane.INFORMATION_MESSAGE);
            } else {
                StringBuilder cartContents = new StringBuilder("Items in your cart:\n");
                double total = 0;
                java.util.ArrayList<Product> cartItems = new java.util.ArrayList<>();

                for (int i = 0; i < listModel.size(); i++) {
                    Product p = listModel.getElementAt(i);
                    cartContents.append("‣ ").append(p.getName()).append(" - ₱").append(p.getPrice()).append("\n");
                    total += p.getPrice();
                    cartItems.add(p);
                }
                cartContents.append("\nTotal: ₱").append(String.format("%.2f", total));

                // Show custom dialog with checkout
                Object[] options = { "Checkout", "Close" };
                int n = JOptionPane.showOptionDialog(this,
                        cartContents.toString(),
                        "Cart",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[1]);

                if (n == JOptionPane.YES_OPTION) {
                    new PaymentWindow(total, cartItems, this).setVisible(true);
                }
            }
        }
        if (e.getSource() == searchBtn) {
            String keyword = txtField.getText().trim();
            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter a product name to search.");
                return;
            }
            int rowCount = tableModel.getRowCount();
            String[] products = new String[rowCount];
            for (int i = 0; i < rowCount; i++) {
                products[i] = tableModel.getValueAt(i, 0).toString();
            }
            Arrays.sort(products);
            int index = binarySearch(products, keyword);

            if (index != -1) {
                for (int i = 0; i < rowCount; i++) {
                    if (table.getValueAt(i, 0).toString().equalsIgnoreCase(products[index])) {

                        table.setRowSelectionInterval(i, i);
                        table.scrollRectToVisible(table.getCellRect(i, 0, true));

                        JOptionPane.showMessageDialog(this, "Found: " + products[index]);
                        return;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Product not found.");
            }
        }

        if (e.getSource() == loginBtn) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Log Out",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new loginwindow().setVisible(true);
                this.dispose();
            }
        }

        // Handle Sort and Filter Events
        if (e.getSource() == ascendBtn || e.getSource() == descendBtn ||
                e.getSource() == star5 || e.getSource() == star4 ||
                e.getSource() == star3 || e.getSource() == star2 || e.getSource() == star1) {
            loadProductsFromDB();
        }
    }

    public int binarySearch(String[] arr, String key) {
        int left = 0;
        int right = arr.length - 1;

        while (left <= right) {
            int mid = (left + right) / 2;
            int result = key.compareTo(arr[mid]);

            if (result == 0)
                return mid;
            if (result > 0)
                left = mid + 1;
            else
                right = mid - 1;
        }
        return -1;
    }
}
