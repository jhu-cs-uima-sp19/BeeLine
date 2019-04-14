package com.wenwanggarzagao.beeline.data;

import com.google.firebase.auth.FirebaseUser;

/**
 * Represents a user account.
 */
public class User {

    public String name;
    public String email;
    public FirebaseUser fbuser;
    public SavedUserData saveData;

    public User(String email, FirebaseUser user) {
        this.email = email;
        this.fbuser = user;
    }

    public void setSaveData(SavedUserData data) {
        this.saveData = data;
        this.name = data.name;
    }

    public String getBio() {
        return bio;
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
