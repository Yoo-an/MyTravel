package com.example.mytravel;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.example.mytravel.db.AppDatabase;
import com.example.mytravel.db.TravelDAO;
import com.example.mytravel.models.Travel;

public class UpdateTravelActivity extends AppCompatActivity {

    public static String EXTRA_TRAVEL_ID = "travel_id";

    private EditText mCountryEditText;
    private EditText mCommentEditText;
    private EditText mCityEditText;
    private TextView mContinent;
    private Button mUpdateButton;
    private Toolbar mToolbar;
    private TravelDAO mTravelDAO;
    private Travel TRAVEL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);

        // Get the DAO here!

        mTravelDAO = Room.databaseBuilder(this,AppDatabase.class,"db-contacts").allowMainThreadQueries().build().getTravelDAO();


        // Create UI widget objects
        mContinent = findViewById(R.id.updateContinent);
        mCountryEditText = findViewById(R.id.travel_edit_country);
        mCityEditText = findViewById(R.id.travel_edit_city);
        mCommentEditText = findViewById(R.id.travel_edit_comment);
        mUpdateButton = findViewById(R.id.updateButton);
        mToolbar = findViewById(R.id.toolbar);

        // Retrieve the particular travel item from database using id
        TRAVEL = mTravelDAO.getTravelWithId(getIntent().getIntExtra(EXTRA_TRAVEL_ID, -1));

        // Populate screen with data from travel
        initViews();
    }

    private void initViews() {
        setSupportActionBar(mToolbar);
        mContinent.setText(TRAVEL.getContinent());
        mCountryEditText.setText(TRAVEL.getCountry());
        mCityEditText.setText(TRAVEL.getCity());
        mCommentEditText.setText(TRAVEL.getComment());

        final int id = TRAVEL.id;

        // Add listener to 'update' button
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String continent = mContinent.getText().toString();
                String country = mCountryEditText.getText().toString();
                String city = mCityEditText.getText().toString();
                String comment = mCommentEditText.getText().toString();

                // Check the user entered data, if not return
                if (country.length() == 0 || city.length() == 0) {
                    Toast.makeText(UpdateTravelActivity.this, "Please make sure to enter country and city!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create travel object to store updated data
                Travel travel = mTravelDAO.getTravelWithId(id);
                travel.setContinent(continent);
                travel.setCountry(country);
                travel.setCity(city);
                travel.setComment(comment);
                mTravelDAO.update(travel);


                // Update row in database table
                try {
                    setResult(RESULT_OK);
                    finish();
                } catch (SQLiteConstraintException e) {
                    Toast.makeText(UpdateTravelActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_update_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete: {
                // Delete row from table
                mTravelDAO.delete(TRAVEL);
                setResult(RESULT_OK);
                finish();
                break;
            }
            case R.id.map:{
                Intent intent = new Intent(UpdateTravelActivity.this,MapsActivity.class);
                intent.putExtra(MapsActivity.EXTRA_TRAVEL_ID,TRAVEL.getId());

                startActivity(intent);
                Log.i("abc","map activity start");
                setResult(RESULT_OK);
                finish();
                break;
            }
            case R.id.camera:{
                Intent intent = new Intent(UpdateTravelActivity.this,CameraActivity.class);
                intent.putExtra(MapsActivity.EXTRA_TRAVEL_ID,TRAVEL.getId());

                startActivity(intent);
                Log.i("abc","camera activity start");
                setResult(RESULT_OK);
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}