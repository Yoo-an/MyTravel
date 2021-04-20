package com.example.mytravel.models;

import androidx.room.Entity;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

@Entity (tableName = "Travel")
public class Travel {

    @PrimaryKey (autoGenerate=true)
    public int id;

    private String country;
    private String city;
    private String comment;
    private String continent;

    public void setContinent(String continent){this.continent = continent;}

    public String getContinent(){return continent;}

    public void setCountry(String country){this.country = country;}

    public String getCountry(){return country;}

    public void setCity(String city){this.city = city;}

    public String getCity(){return city;}

    public void setComment(String comment){this.comment = comment;}

    public String getComment(){return comment;}


    public int getId(){return id;}


}
