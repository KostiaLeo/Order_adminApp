package com.example.order_adminapp;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

public class MainActivity extends AppCompatActivity implements RecyclerUserAdapter.UserItemClicked {

    private AppBarConfiguration mAppBarConfiguration;
    private AlertDialog alertDialog;
    private RecyclerView userRecycler;
    private ArrayList<User> myUsers = new ArrayList<>();

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
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        //NavigationView navigationView = findViewById(R.id.nav_view);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
//                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);

//--------------------------- firebase ----------------------------------------
        userRecycler = findViewById(R.id.users);
        userRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        final ArrayList<User> localUsrs = this.myUsers;
       // DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("User");
//        reff.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                alertDialog = new AlertDialog.Builder(MainActivity.this).create();
//                alertDialog.setTitle("New order");
//                for(DataSnapshot ds : dataSnapshot.getChildren()){
//                    User user = ds.getValue(User.class);
////                    System.out.println(user.toString());
////                    alertDialog.setTitle(user.getName());
////                    alertDialog.setMessage(user.getMail());
//                    localUsrs.add(user);
//                }
////                LinearLayout lp = new LinearLayout(MainActivity.this);
////                lp.setOrientation(LinearLayout.VERTICAL);
////
////                final Button yes = new Button(MainActivity.this);
////                yes.setText("Yes");
////
////                lp.addView(yes);
////                final Button no = new Button(MainActivity.this);
////                yes.setText("No");
////
////                lp.addView(no);
////
////                alertDialog.setView(lp);
////                alertDialog.show();
//
//                //}
//                setRecycler(myUsers);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {}
//        });

        //final DocumentReference docRef = firestore.collection("users").document();
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        final CollectionReference colRef = firestore.collection("user");

//        final CollectionReference colRef = firestore.collection("user")
//                .document().update("allow", "yes");
//        final DocumentReference docRef = firestore.collection("user").document()

        colRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                myUsers.clear();
                localUsrs.clear();
                for (int i = 0; i < queryDocumentSnapshots.toObjects(User.class).size(); i++) {
                    User addingUser = queryDocumentSnapshots.toObjects(User.class).get(i);
                    if(!String.valueOf(addingUser.getAllow()).equals("yes") && !String.valueOf(addingUser.getAllow()).equals("no")) {
                        localUsrs.add(addingUser);
                    }
                }
                setRecycler(myUsers);
            }
        });
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

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}

