package com.example.plt.Models;

public class Order {

    private String UserPhone;

    private String ItemName;
    private String ItemId;
    private String Quantity;
    private String Price;
    private String Discount;
    private String Size;
    private String Image;

    public Order() {
    }

    public Order(String userPhone, String itemId, String itemName, String quantity, String price, String discount, String size, String image) {
        UserPhone = userPhone;
        ItemName = itemName;
        ItemId = itemId;
        Quantity = quantity;
        Price = price;
        Discount = discount;
        Size = size;
        Image = image;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
