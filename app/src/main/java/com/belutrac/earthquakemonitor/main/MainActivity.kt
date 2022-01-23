package com.belutrac.earthquakemonitor.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.belutrac.earthquakemonitor.Earthquake
import com.belutrac.earthquakemonitor.R
import com.belutrac.earthquakemonitor.api.ApiResponseStatus
import com.belutrac.earthquakemonitor.api.WorkerUtil
import com.belutrac.earthquakemonitor.databinding.ActivityMainBinding

private const val SORT_TYPE_KEY = "sort_type"
private val TAG = MainActivity::class.java.simpleName

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        val sortType = getSortType()

        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(application, sortType)
        )[MainViewModel::class.java]

        val adapter = EqAdarter(this)
        binding.eqRecycler.adapter = adapter //Seteo el adapter
        setContentView(binding.root)

        adapter.onItemClickListener = {
            Toast.makeText(this,it.place, Toast.LENGTH_SHORT).show()
        }

        binding.eqRecycler.layoutManager = LinearLayoutManager(this) //Creo un linearLayoutManager

        WorkerUtil.scheduleSync(this)

        viewModel.eqList.observe(this) { eqList ->
            adapter.submitList(eqList)
            handleEmptyView(eqList, binding)
        }

        viewModel.status.observe(this, {
            when (it) {
                ApiResponseStatus.LOADING -> {
                    binding.loadingWheel.visibility = View.VISIBLE
                }
                ApiResponseStatus.DONE -> {
                    binding.loadingWheel.visibility = View.GONE
                }
                ApiResponseStatus.NOT_INTERNET_CONNECTION -> {
                    binding.loadingWheel.visibility = View.GONE
                }
                ApiResponseStatus.ERROR ->{
                    Log.d(TAG,"Error")
                }
            }

        })
    }

    private fun getSortType(): Boolean {
        val prefs = getPreferences(MODE_PRIVATE)
        return prefs.getBoolean(SORT_TYPE_KEY, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId

        if(itemId == R.id.main_menu_sort_time)
        {
            viewModel.reloadEarthquakesFromDatabase(false)
            saveSortByType(false)
        }
        else if (itemId == R.id.main_menu_sort_magnitude)
        {
            viewModel.reloadEarthquakesFromDatabase(true)
            saveSortByType(true)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleEmptyView(eqList: MutableList<Earthquake>, binding: ActivityMainBinding) {
        if (eqList.isEmpty()) {
            binding.emptyViewText.visibility = View.VISIBLE
        } else {
            binding.emptyViewText.visibility = View.GONE
        }
    }

    private fun saveSortByType(sortByMagnitude : Boolean)
    {
        val prefs = getPreferences( MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean(SORT_TYPE_KEY,sortByMagnitude)
        editor.apply()
    }
}