package com.belutrac.earthquakemonitor.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.belutrac.earthquakemonitor.Earthquake
import com.belutrac.earthquakemonitor.R
import com.belutrac.earthquakemonitor.databinding.ActivityDetailBinding
import java.text.SimpleDateFormat
import java.util.*

const val EARTHQUAKE_KEY = "earthquake"
class detailActivity : AppCompatActivity() {

    companion object{
        const val EARTHQUAKE_KEY = "eq"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var binding = ActivityDetailBinding.inflate(layoutInflater)
        val bundle = intent.extras!!
        val earthquake = bundle.getParcelable<Earthquake>(EARTHQUAKE_KEY)!!
        binding.eqMagnitude.text = getString(R.string.magnitude_format,earthquake.magnitude)
        binding.latText.text = getString(R.string.latLong_format,earthquake.latitude)
        binding.longText.text = getString(R.string.latLong_format,earthquake.longitude)
        binding.placeText.text = earthquake.place
        val simpleDateFormat = SimpleDateFormat("dd/MMM/yyyy hh:mm:ss", Locale.getDefault())
        binding.date.text =  simpleDateFormat.format(Date(earthquake.time))
        setContentView(binding.root)
    }
}