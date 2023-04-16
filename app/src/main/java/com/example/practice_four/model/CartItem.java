package com.example.practice_four.model;

public class CartItem {

    int quantity;
    String name;
    String seller;
    String description;
    Double price;

    private String Key;

    public CartItem(int quantity, String name, String seller, String description, Double price) {
        this.quantity = quantity;
        this.name = name;
        this.seller = seller;
        this.description = description;
        this.price = price;
    }

    public CartItem() {
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSeller() {
        return seller;
    }
    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }

    public void setKey(String key) {
        Key = key;
    }
    public String getKey() {
        return Key;
    }

}
