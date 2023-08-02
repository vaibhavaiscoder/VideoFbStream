package com.example.videofbstream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseDatabase database;
    String title, vurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("myvideos");


        FirebaseRecyclerOptions<FileModel> options =
                new FirebaseRecyclerOptions.Builder<FileModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("myvideos"), FileModel.class)
                        .build();

        FirebaseRecyclerAdapter<FileModel, MyViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FileModel, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull FileModel model) {
                holder.setVideo(getApplication(), model.getTitle(), model.getVurl());

                holder.setOnClickListener(new MyViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        title = getItem(position).getTitle();
                        vurl = getItem(position).getVurl();
                        Intent intent = new Intent(MainActivity.this, FullScreen.class);
                        intent.putExtra("tat",title);
                        intent.putExtra("ur",vurl);
                        startActivity(intent);
                    }

                    // To delete videos from database
//                    @Override
//                    public void onItemLongClick(View view, int position) {
//                        title = getItem(position).getTitle();
//                        showDeleteDialog(title);
//                    }
                });
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_item, parent, false);
                return new MyViewHolder(view);
            }
        };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    // For searching in firebase database

    private void firebaseSearch(String searchTitle) {
        String query = searchTitle.toLowerCase();
        Query firebaseQuery = databaseReference.orderByChild("title").startAt(query).endAt(query + "\uf8ff");

        FirebaseRecyclerOptions<FileModel> options =
                new FirebaseRecyclerOptions.Builder<FileModel>()
                        .setQuery(firebaseQuery, FileModel.class)
                        .build();

        FirebaseRecyclerAdapter<FileModel, MyViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FileModel, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull FileModel model) {
                holder.setVideo(getApplication(), model.getTitle(), model.getVurl());

                holder.setOnClickListener(new MyViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        title = getItem(position).getTitle();
                        vurl = getItem(position).getVurl();
                        Intent intent = new Intent(MainActivity.this, FullScreen.class);
                        intent.putExtra("tat",title);
                        intent.putExtra("ur",vurl);
                        startActivity(intent);
                    }

                    // To delete videos from database
//                    @Override
//                    public void onItemLongClick(View view, int position) {
//                        title = getItem(position).getTitle();
//                        showDeleteDialog(title);
//
//                    }
                });
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_item, parent, false);
                return new MyViewHolder(view);
            }
        };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.search_firebase);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
//    private void showDeleteDialog(String title){
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setTitle("Delete");
//        builder.setMessage("Are you sure to delete this data");
//        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                Query query = databaseReference.orderByChild("title").equalTo(title);
//                query.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
//                            dataSnapshot1.getRef().removeValue();
//                        }
//                        Toast.makeText(MainActivity.this, "video Deleted", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        });
//        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }
}