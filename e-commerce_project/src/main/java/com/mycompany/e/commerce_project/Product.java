package com.mycompany.e.commerce_project;

public class Product implements Comparable<Product> {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private String category;
    private double rating;
    private String addedBy;
    private boolean isDeleted;

    public Product(int id, String name, double price, int quantity, String category, double rating, String addedBy,
            boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.rating = rating;
        this.addedBy = addedBy;
        this.isDeleted = isDeleted;
    }

    public Product(int id, String name, double price, int quantity, String category, double rating, String addedBy) {
        this(id, name, price, quantity, category, rating, addedBy, false);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }

    public double getRating() {
        return rating;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Product other) {
        return this.name.compareToIgnoreCase(other.name);
    }
}
