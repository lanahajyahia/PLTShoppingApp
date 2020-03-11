package com.example.plt.Models;

public class User {
    private String firstName;
    private String lastName;
    private String password;
    private String phone;
    private String secureCode;


    public User()
    {

    }

    public User(String firstName, String lastName,String password,String secureCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.secureCode=secureCode;
    }

    public String getSecureCode() {
        return secureCode;
    }

    public void setSecureCode(String secureCode) {
        this.secureCode = secureCode;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public String getPassword() {
        return password;
    }
}
