package com.example.mytravel;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.mytravel.db.AppDatabase;
import com.example.mytravel.db.TravelDAO;
import com.example.mytravel.models.Travel;


public class CreateTravelActivity extends AppCompatActivity {

    private EditText mCountryEditText;
    private EditText mCityEditText;
    //private EditText mDateEditText;
    private EditText mCommentEditText;
    private Spinner mContinent;
    private Button mSaveButton;
    private TravelDAO mTravelDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Get the DAO here!
        mTravelDAO = Room.databaseBuilder(this,AppDatabase.class,"db-contacts").allowMainThreadQueries().build().getTravelDAO();

        // Create UI widget objects
        mContinent = findViewById(R.id.continent);
        mCountryEditText = findViewById(R.id.travel_edit_country);
        mCityEditText = findViewById(R.id.travel_edit_city);
        mCommentEditText = findViewById(R.id.travel_edit_comment);
        //mDateEditText = findViewById(R.id.travel_edit_date);
        mSaveButton = findViewById(R.id.saveButton);

        // Add a listener to 'save' button
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String continent = mContinent.getSelectedItem().toString();
                String country = mCountryEditText.getText().toString();
                String city = mCityEditText.getText().toString();
                String comment = mCommentEditText.getText().toString();
                //String date = mDateEditText.getText().toString();

                // Check data was entered, if not pop toast and return
                if (city.length() == 0 || country.length() == 0) {
                    Toast.makeText(CreateTravelActivity.this, "Please make sure all details are correct", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create new to-do object and set fields
				Travel travel = new Travel();

                travel.setContinent(continent);
                travel.setCountry(country);
                travel.setCity(city);
                travel.setComment(comment);
                //travel.setDate(date);
                mTravelDAO.insert(travel);


                //Insert into database
                try {
                    
                    setResult(RESULT_OK);
                    finish();
                } catch (SQLiteConstraintException e) {
                    Toast.makeText(CreateTravelActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}