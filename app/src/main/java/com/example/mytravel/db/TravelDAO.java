package com.example.mytravel.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mytravel.models.Travel;

import java.util.List;

@Dao
public interface TravelDAO {
    @Insert
    public void insert(Travel... travels);

    @Update
    public void update(Travel... travels);

    @Delete
    public void delete(Travel... travels);

    @Query("SELECT * FROM Travel")
    public List<Travel> getTravels();

    @Query("SELECT * FROM Travel WHERE id = :number")
    public Travel getTravelWithId(int number);
}
