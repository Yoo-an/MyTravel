package com.example.mytravel.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.mytravel.models.Travel;


@Database(entities = {Travel.class}, version = 1)

public abstract class AppDatabase extends RoomDatabase {
    public abstract TravelDAO getTravelDAO();
}
