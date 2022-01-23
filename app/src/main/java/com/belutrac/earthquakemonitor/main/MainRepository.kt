package com.belutrac.earthquakemonitor.main

import com.belutrac.earthquakemonitor.Earthquake
import com.belutrac.earthquakemonitor.api.EqJsonResponse
import com.belutrac.earthquakemonitor.api.service
import com.belutrac.earthquakemonitor.database.EqDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository(private val database : EqDatabase) {

    suspend fun fetchEarthquakes(sortByMagnitude : Boolean) : MutableList<Earthquake> {
        return withContext(Dispatchers.IO){
            val eqJsonResponse = service.getLastHourEarthquakes()
            val eqList =  parseEqResult(eqJsonResponse)

            database.eqDao.insertAll(eqList)
          fetchEarthquakesByDatabase(sortByMagnitude)
        }
    }

    suspend fun fetchEarthquakesByDatabase(sortByMagnitude : Boolean) : MutableList<Earthquake> {
        return withContext(Dispatchers.IO) {
            if (sortByMagnitude) {
                database.eqDao.getEarthquakeByMagnitude()
            } else {
                database.eqDao.getEarthquake()
            }
        }
    }

    private fun parseEqResult(eqJsonResponse: EqJsonResponse) : MutableList<Earthquake>{
        val eqList = mutableListOf<Earthquake>()
        val featuresList = eqJsonResponse.features

        for (feature in featuresList)
        {
            val properties = feature.properties
            val id = feature.id
            val mag = properties.mag
            val place = properties.place
            val time = properties.time
            val geometry = feature.geometry
            val longitude = geometry.longitude
            val latitude = geometry.latitude

            eqList.add(Earthquake(id,place,mag,time,latitude,longitude))
        }

        return eqList
    }

}