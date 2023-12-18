package com.zareinaflameniano.sample;

public class User {
    public String First;
    public String Last;
    public String Email;
    public String Password;

    public String Phone;
    public User() {

    }

    public User(String first, String last, String email, String password, String phone) {
        this.First = first;
        this.Last = last;
        this.Email = email;
        this.Password = password;
        this.Phone = phone;
    }
}

