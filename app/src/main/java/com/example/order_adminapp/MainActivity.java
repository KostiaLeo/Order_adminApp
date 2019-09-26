package com.example.order_adminapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

import static android.app.Notification.DEFAULT_ALL;

public class MainActivity extends AppCompatActivity implements RecyclerUserAdapter.UserItemClicked {

    private RecyclerView userRecycler;
    private ArrayList<User> myUsers = new ArrayList<>();
    public static final int NOTIFICATION_ID = 1;
    private int notAllowLength = 0;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference colRef = firestore.collection("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
//--------------------------- firebase ----------------------------------------
        userRecycler = findViewById(R.id.users);
        userRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        final ArrayList<User> localUsrs = this.myUsers;
        colRef.whereEqualTo("allow", "0").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                myUsers.clear();
                localUsrs.clear();
                for (int i = 0; i < queryDocumentSnapshots.toObjects(User.class).size(); i++) {
                    User addingUser = queryDocumentSnapshots.toObjects(User.class).get(i);
                        localUsrs.add(addingUser);
                }
                setRecycler(myUsers);
            }
        });
        preSendNotif();
    }

    private void setRecycler(ArrayList<User> users) {
        RecyclerUserAdapter recyclerUserAdapter = new RecyclerUserAdapter(users, this);
        userRecycler.setAdapter(recyclerUserAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void itemClickedCallback(int itemPosition) {

    }


    private void preSendNotif(){
        colRef.whereEqualTo("allow", "0").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                notAllowLength = queryDocumentSnapshots.size();
                sendNotif(notAllowLength);
            }
        });
    }

    private void sendNotif(final int notAllowLength){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder
                = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(true)
                .setDefaults(DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setTicker("Hearty365")
                .setContentTitle("New request(s)")
                .setContentText("You've got " + notAllowLength + " new messages");
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }
}

