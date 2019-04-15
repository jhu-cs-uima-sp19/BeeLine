package com.wenwanggarzagao.beeline.data;

import com.google.firebase.auth.FirebaseUser;

/**
 * Represents a user account.
 */
public class User {

    public FirebaseUser fbuser;
    public SavedUserData saveData;

    public User(FirebaseUser user) {
        this.fbuser = user;
    }

    public User(FirebaseUser user, SavedUserData data) {
        this(user);
        this.setSaveData(data);
    }

    public void setSaveData(SavedUserData data) {
        this.saveData = data;
    }

    public String getUsername() {
        return saveData.username;
    }

    public String getEmail() {
        return fbuser.getEmail();
    }

    @Override
    public int hashCode() {
        return this.getEmail().hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof User) {
            return this.getEmail().equals(((User) other).getEmail());
        }

        return false;
    }

}
