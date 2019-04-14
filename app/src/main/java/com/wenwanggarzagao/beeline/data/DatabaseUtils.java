package com.wenwanggarzagao.beeline.data;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.wenwanggarzagao.beeline.Landing;
import com.wenwanggarzagao.beeline.io.ResponseHandler;

import java.util.ArrayList;
import java.util.List;

public class DatabaseUtils {

    private static FirebaseAuth mAuth;
    private static FirebaseDatabase database;
    private static boolean loggedin = false;
    public static User me;

    public static void load() {
        System.out.println("Loading database");
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
    public static void createAccount(final Activity activity, final String email, final String pass) {
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("thing", "signInWithEmail:success");
                            System.out.println("Added new user " + email);
                            // TODO update UI
                            // set up account
                            SavedUserData sud = new SavedUserData();
                            sud.name = user.getDisplayName();
                            sud.email = user.getEmail();
                            sud.userId = user.getUid();
                            me = new User(user.getEmail(), user);
                            me.setSaveData(sud);
                            pushUserData(sud);
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
    public static void signIn(final Activity activity, final String email, final String password) {
        System.out.println("Attempting sign-in to " + email);
        FirebaseAuth.AuthStateListener mAuthListener;
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                System.out.println("mauthstate changed");
                if(user != null){
                    System.out.println("user is not null! querying data...");
                    me = new User(user.getEmail(), user);
                    queryUserData(user.getUid(),
                            new ResponseHandler<SavedUserData>() {
                                @Override
                                public void handle(SavedUserData u) {
                                    me.setSaveData(u);
                                }
                            }
                    );
                    loggedin = true;
                    System.out.println("Successfully logged into " + email);
                    // TODO update UI
                }
            }
        };


        mAuth.addAuthStateListener(mAuthListener);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnFailureListener(activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace(System.out);
                    }
                })
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            me = new User(user.getEmail(), user);
                            queryUserData(user.getUid(),
                                    new ResponseHandler<SavedUserData>() {
                                        @Override
                                        public void handle(SavedUserData u) {
                                            me.setSaveData(u);
                                        }
                                    }
                            );
                            loggedin = true;
                            System.out.println("Successfully logged into " + email);
                            // TODO update UI
                        } else {
                            System.out.println("Failed to log into " + email + ". Trying account creation.");
                            createAccount(activity, email, password);
                            // If sign in fails, display a message to the user.
                            /*(Toast.makeText(activity, "Invalid email/password combination.",
                                    Toast.LENGTH_SHORT).show();
                            // TODO update UI*/
                        }
                    }
                });

    }

    public static void pushUserData(SavedUserData data) {
        System.out.println("Trying to push data");
        database.getReference("users").child(data.email).setValue(data);
        queryUserData(me.fbuser.getUid(), new ResponseHandler<SavedUserData>() {
            @Override
            public void handle(SavedUserData savedUserData) {
                System.out.println("Pushed data successfully!");
                System.out.println(savedUserData.name + " " + savedUserData.email + " " + savedUserData.userId);
            }
        });
    }

    /**
     * Queries user data, handling it in a consumer once it's finished.
     * @param uid The user's string ID as assigned by Firebase.
     * @param consumer What to do after the SavedUserData is fetched.
     */
    public static void queryUserData(String uid, final ResponseHandler<SavedUserData> consumer) {
        DatabaseReference table = database.getReference("users").child(uid);
        table.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    SavedUserData result = ds.getValue(SavedUserData.class);
                    consumer.handle(result);
                }
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

    /**
     * Adds a new beeline to database.
     * @param bline The beeline to add.
     */
    public static void pushBeeline(Beeline bline) {
        DatabaseReference table = database.getReference("beelines");
        DatabaseReference value = table.child("zip_" + bline.to.zip).push();
        value.setValue(bline);
    }

    /**
     * Queries a list of Beelines for the specified zip code.
     * @param zip Zip code.
     * @param consumer The thing that happens after fetching the data.
     */
    public static void queryBeelinesNear(short zip, final ResponseHandler<List<Beeline>> consumer) {
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
