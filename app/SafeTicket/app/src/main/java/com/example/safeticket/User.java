package com.example.safeticket;

public class User {
    // ID, PWD, 이름, 전화번호
    private String id;
    private String password;
    private String name;
    private String phoneNumber;

    User(String id, String password, String name, String phoneNumber)
    {
        this.id = id;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    String getId() { return this.id; }
    String getPassword() { return this.password; }
    String getName() { return this.name; }
    String getPhoneNumber() { return this.phoneNumber; }
}
