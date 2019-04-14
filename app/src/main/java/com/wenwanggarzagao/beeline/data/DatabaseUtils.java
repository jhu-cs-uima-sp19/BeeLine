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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wenwanggarzagao.beeline.Landing;
import com.wenwanggarzagao.beeline.io.ResponseHandler;

import java.util.ArrayList;
import java.util.List;

public class DatabaseUtils {

    private static FirebaseAuth mAuth;
    private static FirebaseDatabase database;
    private static boolean loggedin = false;
    public static User me;

    public static boolean isLoggedin() {
        return loggedin;
    }

    public static void load() {

        System.out.println("Loading database");
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
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
        System.out.println("Attempting to create new account for: " + email);
        mAuth.createUserWithEmailAndPassword(email, pass).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("create user task failed");

                e.printStackTrace(System.out);
            }
        }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                System.out.println("task completed");
                if (!task.isSuccessful()) {
                    System.out.println("create user failed");
                    return;
                }

                System.out.println("create user succeeded");
                FirebaseUser user = task.getResult().getUser();

                me = new User(user.getEmail(), user);
                SavedUserData sud = new SavedUserData();
                sud.name = user.getDisplayName();
                sud.email = user.getEmail();
                sud.userId = user.getUid();
                me.setSaveData(sud);
                pushUserData(sud);
            }
        });
    }

    /**
     * Attempts to authenticate with a user and password.
     * @param activity The activity this is called from. Should be the login activity.
     * @param email Email of the user.
     * @param password Password of the user.
     */
    public static void signIn(final Activity activity, final String email, final String password, final boolean wasCreated) {
        System.out.println("Attempting sign-in to " + email);
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    System.out.println("log in task failed.");
                    if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                        System.out.println("user null! creating account for " + email);

                        try {
                            Thread.sleep(1000l);
                        } catch (InterruptedException e) {

                        }
                        createAccount(activity, email, password);
                    } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        System.out.println("user put invalid credentials! " + email);
                    }
                    return;
                }
                FirebaseUser user = task.getResult().getUser();
                if (user != null){
                    System.out.println("user is not null! querying data...");
                    System.out.println("setting 'me'");
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
                    System.out.println("user null! creating account for " + email);
                    createAccount(activity, email, password);
                }
            }
        });

    }

    public static void pushUserData(SavedUserData data) {
        System.out.println("Trying to push data");
        database.getReference("users").child(data.userId).setValue(data);
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
        System.out.println("querying user data");
        DatabaseReference table = database.getReference("users").child(uid);
        table.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("ondatachange called!");
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    System.out.println("pinging data snapshot");
                    SavedUserData result = ds.getValue(SavedUserData.class);
                    consumer.handle(result);
                    System.out.println("pinged data snapshot");
                }
            }

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

        table.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Beeline> result = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Beeline beeline = ds.getValue(Beeline.class);
                    result.add(beeline);
                }
                consumer.handle(result);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

}
