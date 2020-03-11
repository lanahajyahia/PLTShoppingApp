package com.example.plt.Models;

public class WishlistItem {

    private String ItemName;
    private String ItemId;
    private String Price;
    private String Image;
    private String UserPhone;

    public WishlistItem() {
    }

    public WishlistItem(String itemId, String itemName, String price, String image,String UserPhone) {
        ItemName = itemName;
        ItemId = itemId;
        Price = price;
        Image = image;
        this.UserPhone=UserPhone;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }


    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}

