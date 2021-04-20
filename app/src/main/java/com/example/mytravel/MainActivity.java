package com.example.mytravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Dao;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.mytravel.db.AppDatabase;
import com.example.mytravel.db.TravelDAO;
import com.example.mytravel.models.Travel;

import java.util.ArrayList;

//reference: The following code is from Android Example: our lab 7
public class MainActivity extends AppCompatActivity {

    private static final int RC_CREATE_TRAVEL = 1;
    private static final int RC_UPDATE_TRAVEL = 2;

    private RecyclerView mTravelRecyclerView;
    private TravelRecyclerAdapter mTravelRecyclerAdapter;
    private FloatingActionButton mAddTravelFloatingActionButton;
    private TravelDAO mTravelDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get DAO
        mTravelDAO = Room.databaseBuilder(this, AppDatabase.class,"db-contacts").allowMainThreadQueries().build().getTravelDAO();

        // Depending on the continent, give a different colour
        int colors[] = {ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, android.R.color.holo_orange_light),
                ContextCompat.getColor(this, android.R.color.holo_green_light),
                ContextCompat.getColor(this, android.R.color.holo_blue_dark)};

        //Configure Recyclerview
        mTravelRecyclerView = findViewById(R.id.contactsRecyclerView);
        mTravelRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //create Adapter
        mTravelRecyclerAdapter = new TravelRecyclerAdapter(this,new ArrayList<Travel>(),colors);
        //Launches update activity when user long-clicks item
        mTravelRecyclerAdapter.addActionCallback(new TravelRecyclerAdapter.ActionCallback(){
            @Override
            public void onLongClickListener(Travel travel){
                Intent intent = new Intent(MainActivity.this,UpdateTravelActivity.class);

                intent.putExtra(UpdateTravelActivity.EXTRA_TRAVEL_ID,travel.getId());

                startActivityForResult(intent,RC_UPDATE_TRAVEL);
            }
        });
        mTravelRecyclerView.setAdapter(mTravelRecyclerAdapter);

        //Include a FAB
        mAddTravelFloatingActionButton = findViewById(R.id.addContactFloatingActionButton);
        //Launches create activity when user clicks on it
        mAddTravelFloatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this,CreateTravelActivity.class);
                startActivityForResult(intent,RC_CREATE_TRAVEL);
            }
        });
        //update display with all travels currently stored in database
        loadTravels();

    }

    private void loadTravels(){
        mTravelRecyclerAdapter.updateData(mTravelDAO.getTravels());
    }

    //reference complete

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        //really just refreshing the screen after data changes
        if(requestCode == RC_CREATE_TRAVEL && resultCode == RESULT_OK){
            loadTravels();
        } else if(requestCode == RC_UPDATE_TRAVEL && resultCode==RESULT_OK){
            loadTravels();
        }
    }
}
