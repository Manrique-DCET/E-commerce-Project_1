package com.mycompany.e.commerce_project;

import java.util.Date;

public class Order {
    private int orderId;
    private int userId;
    private String userName;
    private double totalPrice;
    private Date orderDate;

    public Order(int orderId, int userId, String userName, double totalPrice, Date orderDate) {
        this.orderId = orderId;
        this.userId = userId;
        this.userName = userName;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    // Getters
    public int getOrderId() {
        return orderId;
    }

    public String getUserName() {
        return userName;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
