package com.wenwanggarzagao.beeline.data;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.wenwanggarzagao.beeline.Landing;
import com.wenwanggarzagao.beeline.io.ResponseHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DatabaseUtils {

    private static FirebaseAuth mAuth;
    private static FirebaseDatabase database;
    private static boolean loggedin = false;

    public static void load() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    public static @Nullable FirebaseUser getCurrentUser() {
        if (!loggedin) {
            throw new IllegalStateException("Tried to get current user without logging in first!");
        }

        return mAuth.getCurrentUser();
    }

    // TODO change Activity to name of login screen

    /**
     * Creates a new account.
     * @param activity The activity this is called from. Should be the login activity.
     * @param email Email of the user.
     * @param pass Password of the user.
     */
    public static void createAccount(final Landing activity, final String email, final String pass) {
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            System.out.println("Added new user " + email);
                            // TODO update UI
                            activity.setUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(activity, "That email is already in use.",
                                    Toast.LENGTH_SHORT).show();
                            // TODO update UI
                        }
                    }
                });
    }

    /**
     * Attempts to authenticate with a user and password.
     * @param activity The activity this is called from. Should be the login activity.
     * @param email Email of the user.
     * @param password Password of the user.
     */
    public static void signIn(final Landing activity, final String email, final String password) {
        for (int i = 0; i < 20; i++)
            System.out.println("Attempting sign-in to " + email + i);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            loggedin = true;
                            for (int i = 0; i < 20; i++)
                                System.out.println("Successfully logged into " + email + i);
                            // TODO update UI
                        } else {
                            for (int i = 0; i < 20; i++)
                                System.out.println("Failed to log into " + email + ". Trying account creation." + i);
                            createAccount(activity, email, password);
                            // If sign in fails, display a message to the user.
                            /*(Toast.makeText(activity, "Invalid email/password combination.",
                                    Toast.LENGTH_SHORT).show();
                            // TODO update UI*/
                        }
                    }
                });

    }

    public static void pushBeeline(Beeline bline) {
        DatabaseReference table = database.getReference("beelines");
        DatabaseReference value = table.child("zip_" + bline.to.zip).push();
        value.setValue(bline);
    }

    public static void getBeelinesNear(short zip, final ResponseHandler<List<Beeline>> consumer) {
        List<Beeline> list = new ArrayList<>();
        DatabaseReference table = database.getReference("beelines").child("zip_" + zip);
        Query myTopPostsQuery = table.orderByKey();

        table.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                List<Beeline> result = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Beeline beeline = ds.getValue(Beeline.class);
                    result.add(beeline);
                }
                consumer.handle(result);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

}
