package com.belutrac.earthquakemonitor.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.belutrac.earthquakemonitor.Earthquake
import com.belutrac.earthquakemonitor.api.ApiResponseStatus
import com.belutrac.earthquakemonitor.database.getDatabase
import kotlinx.coroutines.*
import java.net.UnknownHostException

private val TAG = MainViewModel::class.java.simpleName

class MainViewModel(application: Application,private val sortType : Boolean) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val repository = MainRepository(database)

    private val _status = MutableLiveData<ApiResponseStatus>()
    val status: LiveData<ApiResponseStatus>
        get() = _status

    private var _eqList = MutableLiveData<MutableList<Earthquake>>()
    val eqList : LiveData<MutableList<Earthquake>>
        get() = _eqList

    init {
        reloadEarthquakesFromDatabase(sortType)
    }

    fun reloadEarthquakes()
    {
        viewModelScope.launch {
            try {
                _status.value = ApiResponseStatus.LOADING
                _eqList.value = repository.fetchEarthquakes(sortType)
                _status.value = ApiResponseStatus.DONE
            } catch (e: UnknownHostException) {
                _status.value = ApiResponseStatus.ERROR
                Log.d(TAG, "No internet connection")
            }
        }
    }

    fun reloadEarthquakesFromDatabase(sortByMagnitude : Boolean) {
        viewModelScope.launch {
                _eqList.value = repository.fetchEarthquakesByDatabase(sortByMagnitude )
                if (_eqList.value!!.isEmpty())
                {
                    reloadEarthquakes()
                }
        }

    }

}
