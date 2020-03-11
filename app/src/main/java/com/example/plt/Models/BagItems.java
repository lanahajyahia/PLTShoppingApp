package com.example.plt.Models;

public class BagItems {

    private String imageUrl , description,price,size;
    private int amount =1;

    public BagItems() {
    }

    public BagItems(String imageUrl, String description, String price, String size) {
        this.imageUrl = imageUrl;
        this.description = description;
        this.price = price;
        this.size = size;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getSize() {
        return size;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
