package com.example.order_adminapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RecyclerUserAdapter extends RecyclerView.Adapter<RecyclerUserAdapter.ViewHolder> {
    private UserItemClicked callback;
    private DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Basket");
    private ArrayList<User> userList;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference colRef = firestore.collection("user");

    public RecyclerUserAdapter(List<User> userList, UserItemClicked callback) {//, DatabaseReference reff
        this.userList = (ArrayList<User>) userList;
        this.callback = callback;
    }
    @NonNull
    @Override
    public RecyclerUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION && callback != null) {
                    callback.itemClickedCallback(adapterPosition);
                }
            }
        });
        return holder;
    }

//-------------------------------------------------------------------------------------------------------------

    @Override
    public int getItemCount() {
        if (userList == null) {
            return 0;
        }
        return userList.size();
    }

    //----------метод для заполнения каждого елемента списка -----------------------------------------------------
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)  {
        final User user = userList.get(position);
        holder.nameTv.setText(user.getName());
        holder.mailtV.setText(user.getMail());
        holder.yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//                final CollectionReference colRef = firestore.collection("user");
//                colRef.document(user.getName()).update("allow", true);
                yes_no("yes", user, position);
            }
        });
        holder.no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//                final CollectionReference colRef = firestore.collection("user");
//                colRef.document(user.getName()).update("allow", false);
                yes_no("no", user, position);
            }
        });
    }

    private void yes_no(String iter, User user, int position){
        colRef.document(user.getName()).update("allow", iter);
        userList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, userList.size());
    }

    interface UserItemClicked {
        void itemClickedCallback(int itemPosition);
    }

    //--------- метод, соединяющий UI c елементами ресайклера --------------------
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv, mailtV;
        Button yes, no;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.name);
            mailtV = itemView.findViewById(R.id.mail);
            yes = itemView.findViewById(R.id.yes);
            no = itemView.findViewById(R.id.no);
        }
    }
}

