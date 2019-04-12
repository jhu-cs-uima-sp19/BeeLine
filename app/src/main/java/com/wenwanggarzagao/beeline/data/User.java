package com.wenwanggarzagao.beeline.data;

import java.util.UUID;

/**
 * Represents a user account.
 */
public class User {

    private String name;
    private String username;
    private UUID id;

    public static User newUser(String name, String username) {
        return new User(UUID.randomUUID(), name, username);
    }

    public User(UUID id, String name, String username) {
        this.name = name;
        this.username = username;
        this.id = id;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof UUID) {
            return other.equals(this.id);
        }

        if (other instanceof User) {
            return this.id.equals(((User) other).id);
        }

        return false;
    }

}
