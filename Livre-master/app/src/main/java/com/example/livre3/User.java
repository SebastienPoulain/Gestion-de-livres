package com.example.livre3;

/**
 *
 * classe qui permet de créer un objet de type User
 *
 * @author  emilie cormier houle, sébastien poulain,jérémy lampron
 * @version 2020 mars
 * @since 1.0
 */

import androidx.annotation.NonNull;



public class User {

    private int id;
    private String lastName;
    private String firstName;
    private String email;
    private String phoneNumber;


    public User( int id, String lastName, String firstName, String email, String phoneNumber)
    {
        setId(id);
        setNom(lastName);
        setPrenom(firstName);
        setEmail(email);
        setTel(phoneNumber);

    }

    public String getNom() {
        return lastName;
    }

    public String getPrenom() {
        return firstName;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }


    public String getTel() {
        return phoneNumber;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String lastName) {
        if (lastName.trim().length() > 2)
            this.lastName = lastName.trim().substring(0,1).toUpperCase() + lastName.trim().substring(1).toLowerCase();
        else
            this.lastName = lastName;
    }

    public void setPrenom(String firstName) {
        if (firstName.trim().length() > 2)
            this.firstName = firstName.trim().substring(0,1).toUpperCase() + firstName.trim().substring(1).toLowerCase();
        else
            this.firstName = firstName;
    }

    public void setEmail(String email) {
        this.email = email.trim().toLowerCase();
    }

    public void setTel(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", userId='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

}
