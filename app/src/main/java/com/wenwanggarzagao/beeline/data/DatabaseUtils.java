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
import com.wenwanggarzagao.beeline.io.Discriminator;
import com.wenwanggarzagao.beeline.io.ResponseHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseUtils {

    private static FirebaseAuth mAuth;
    public static FirebaseDatabase database;
    private static boolean loggedin = false;
    public static User me;

    public static boolean isLoggedin() {
        return loggedin;
    }

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

    public static void saveUser() {
        pushUserData(me.saveData);
    }

    // TODO change Activity to name of login screen

    /**
     * Creates a new account.
     * @param activity The activity this is called from. Should be the login activity.
     * @param email Email of the user.
     * @param pass Password of the user.
     */
    public static void createAccount(final Activity activity, final String username, final String email, final String pass) {
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

                me = new User(user);
                SavedUserData sud = new SavedUserData();
                sud.username = username;
                sud.email = user.getEmail();
                sud.userId = user.getUid();
                sud.bio = "Sample bio text.";
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
    public static void signIn(final Activity activity, final String email, final String password, final boolean wasCreated, final Runnable... after) {
        System.out.println("Attempting sign-in to " + email);
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    System.out.println("log in task failed.");
                    if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                        System.out.println("user null! creating account for " + email);
                        createAccount(activity, "Joe Ansel Ensky", email, password);
                    } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        System.out.println("user put invalid credentials! " + email);
                    }
                    return;
                }
                FirebaseUser user = task.getResult().getUser();
                if (user != null){
                    System.out.println("user is not null! querying data...");
                    System.out.println("setting 'me'");
                    me = new User(user);
                    queryUserData(user.getUid(),
                            new ResponseHandler<SavedUserData>() {
                                @Override
                                public void handle(SavedUserData u) {
                                    me.setSaveData(u);
                                    for (Runnable r : after) {
                                        r.run();
                                    }
                                }
                            }
                    );
                    loggedin = true;
                    System.out.println("Successfully logged into " + email);
                    // TODO update UI
                } else {
                    System.out.println("user null! creating account for " + email);
                    createAccount(activity, "Joe Ansel Ensky", email, password);
                }
            }
        });

    }

    public static void pushUserData(SavedUserData data) {
        System.out.println("Trying to push data");
        database.getReference("users").child(data.userId).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                queryUserData(me.fbuser.getUid(), new ResponseHandler<SavedUserData>() {
                    @Override
                    public void handle(SavedUserData savedUserData) {
                        System.out.println("Pushed data successfully!");
                        System.out.println(savedUserData.username + " " + savedUserData.email + " " + savedUserData.userId);
                    }
                });
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
        database.getReference("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                System.out.println("ondatachange called!");
                if (!ds.exists()) {
                    System.out.println("but data was null......");
                    return;
                }

                System.out.println("pinging data snapshot: [" + ds.getValue().toString() + "]");
                SavedUserData result = ds.getValue(SavedUserData.class);
                consumer.handle(result);
                System.out.println("pinged data snapshot");
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
        DatabaseReference value = table.child("zip_" + bline.to.zip).child("beeline_" + bline.id);
        value.setValue(bline);
    }

    public static void pushNotification(Notification notif) {
        DatabaseReference table = database.getReference("notifications");
        DatabaseReference value = table.child("user_" + me.getId()).child(notif.id + "");
        value.setValue(notif);
    }

    /**
     * Removes a Beeline from database and user.
     * @param zip Zip code.
     * @param id Beeline ID.
     * @param runnables Things to do after completion.
     */
    public static void removeBeeline(int zip, long id, Runnable... runnables) {
        database.getReference("beelines").child("zip_" + zip).child("beeline_" + id).removeValue();
        for (Runnable r : runnables) {
            r.run();
        }
    }

    /**
     * Removes a Beeline from database and user.
     * @param bline Beeline to remove.
     * @param runnables Things to do after completion.
     */
    public static void removeBeeline(Beeline bline, Runnable... runnables) {
        removeBeeline(bline.to.zip, bline.id, runnables);
    }

    public static void queryBeelinesNear(int zip, final ResponseHandler<List<Beeline>> consumer, final Discriminator<Beeline> discrim) {
        List<Beeline> list = new ArrayList<>();
        if (database == null)
            database = FirebaseDatabase.getInstance();

        DatabaseReference table = database.getReference("beelines").child("zip_" + zip);
        System.out.print("querying zip " + zip);

        table.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.print("data changed - getting children of " + dataSnapshot.getKey());
                List<Beeline> result = new ArrayList<>();
                // get all beelines in zip
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    System.out.println("got a beeline of key " + ds.getKey());
                    Beeline beeline = ds.getValue(Beeline.class);
                    // filter beelines by discriminator
                    if (discrim == null || discrim.acceptable(beeline))
                        result.add(beeline);
                }
                consumer.handle(result);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public static void queryMyBeelines(final ResponseHandler<List<Beeline>> consumer) {
        if (me.saveData.myBeelines == null)
            return;

        // concurrency needed for multiple queries
        final Map<Beeline, Boolean> set = new ConcurrentHashMap<>();

        for (final Map.Entry<String, List<Long>> entry : me.saveData.myBeelines.entrySet()) {
            final Set<Long> targets = new HashSet<>(entry.getValue()); // for faster lookups

            queryBeelinesNear(Integer.parseInt(entry.getKey()), new ResponseHandler<List<Beeline>>() {
                @Override
                public void handle(List<Beeline> beelines) {
                    for (Beeline b : beelines)
                        set.put(b, true);
                }
            }, new Discriminator<Beeline>() {
                @Override
                public boolean acceptable(Beeline beeline) {
                    return targets.contains(beeline.id);
                }
            });
        }

        consumer.handle(new ArrayList<>(set.keySet()));
    }

    /**
     * Queries a list of Beelines for the specified zip code.
     * @param zip Zip code.
     * @param consumer The thing that happens after fetching the data.
     */
    public static void queryBeelinesNear(int zip, final ResponseHandler<List<Beeline>> consumer) {
        queryBeelinesNear(zip, consumer, null);
    }

    public static void printBeelines(int zip) {
        queryBeelinesNear(zip, new ResponseHandler<List<Beeline>>() {
            @Override
            public void handle(List<Beeline> beelines) {
                for (Beeline bl : beelines) {
                    System.out.println(bl.toString());
                }
            }
        });
    }

    public static void queryNotifications(final ResponseHandler<ArrayList<Notification>> consumer) {
        DatabaseReference table = database.getReference("notifications").child("user_" + me.getId());
        System.out.print("querying notifs for user " + me.getId());

        table.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Notification> result = new ArrayList<>();
                System.out.print("data changed - getting children of " + dataSnapshot.getKey());

                // get all beelines in zip
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    System.out.println("got a beeline of key " + ds.getKey());
                    Notification notif = ds.getValue(Notification.class);
                    result.add(notif);
                }
                consumer.handle(result);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

}
