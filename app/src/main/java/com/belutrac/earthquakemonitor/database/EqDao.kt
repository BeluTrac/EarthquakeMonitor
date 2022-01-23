package com.example.earthquakemonitor.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.belutrac.earthquakemonitor.Earthquake

@Dao //Para cada entity debo tener una interfaz Dao

interface EqDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(eqList : MutableList<Earthquake>)

    @Query("SELECT * FROM earthquakes")
    fun getEarthquake(): MutableList<Earthquake>

    @Query("SELECT * FROM earthquakes order by magnitude ASC")
    fun getEarthquakeByMagnitude(): MutableList<Earthquake>
}