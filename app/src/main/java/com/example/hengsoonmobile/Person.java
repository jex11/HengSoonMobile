package com.example.hengsoonmobile;

/**
 * Created by Jex on 20/11/2016.
 */

public class Person {
    //name and address string
    private String name;
    private String address;

    public Person() {
      /*Blank default constructor essential for Firebase*/
    }

    public Person(String name, String address) {
        this.name = name;
        this.address = address;
    }

    //Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
