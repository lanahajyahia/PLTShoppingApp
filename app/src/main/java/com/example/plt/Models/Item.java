package com.example.plt.Models;

import java.util.Comparator;

public class Item {
    private String name;
    private String image1;
    private String image2;
    private String description;
    private String price;
    private String discount;
    private String categoryId;

    public Item() {
    }

    public Item(String name, String image1, String image2, String description, String price, String discount, String categoryId) {
        this.name = name;
        this.image1 = image1;
        this.image2 = image2;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public static final Comparator<Item> LOW_TO_HIGH =new Comparator<Item>() {
        @Override
        public int compare(Item item1, Item item2) {
            if (Integer.parseInt(item1.getPrice()) >= Integer.parseInt(item2.getPrice()))
                return 1;
            else return -1; //
        }
    };
    public static final Comparator<Item> HIGH_LOW_TO =new Comparator<Item>() {
        @Override
        public int compare(Item item1, Item item2) {
            if (Integer.parseInt(item2.getPrice()) >= Integer.parseInt(item1.getPrice()))
                return 1;
            else return -1;
        }
    };


}

