package com.wenwanggarzagao.beeline.data;

import com.google.firebase.auth.FirebaseUser;

import java.util.UUID;

/**
 * Represents a user account.
 */
public class User {

    public String name;
    public String email;
    public FirebaseUser fbuser;

    public User(String email, FirebaseUser user) {
        this.email = email;
        this.fbuser = user;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return this.email.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof User) {
            return this.email.equals(((User) other).email);
        }

        return false;
    }

}
