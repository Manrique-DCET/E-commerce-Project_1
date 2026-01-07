package com.mycompany.e.commerce_project;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private boolean isAdmin;

    public User(int id, String name, String email, String password, boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
