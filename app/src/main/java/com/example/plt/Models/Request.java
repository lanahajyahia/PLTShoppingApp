package com.example.plt.Models;

import java.util.List;

public class Request {


    private String phone;
    private String fullName;
    private String address;
    private String totalPrice;
    private String status;
    private String comment;
    private List<Order> orderList; //list of items in order
    private String paymentStatus;
    private String paymentMethod;

    public Request() {
    }

    public Request(String phone, String fullName, String address, String totalPrice, String status, String comment, List<Order> orderList,String paymentStatus,String paymentMethod) {
        this.phone = phone;
        this.fullName = fullName;
        this.address = address;
        this.totalPrice = totalPrice;
        this.status=status;
        this.comment = comment;
        this.orderList = orderList;
        this.paymentStatus=paymentStatus;
        this.paymentMethod=paymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}
