package com.example.learnapp.activities

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import com.example.learnapp.R
import com.example.learnapp.adapters.CitiesAdapter
import com.example.learnapp.isNetworkAvailable
import com.example.learnapp.models.CityInfo
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text


class MainActivity : AppCompatActivity(), View.OnClickListener
{
    // constant values
    companion object { const val WEATHER_INFO_REQUEST = 1 }

    lateinit var temperatureLabel:TextView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cities = getCitiesList()
        val mAdapter = CitiesAdapter(cities, applicationContext, this, this)
        val mLayoutManager = LinearLayoutManager(applicationContext)

        val animation = AnimationUtils.loadLayoutAnimation(applicationContext, R.anim.layout_animation_fall_down)
        rvCities.adapter = mAdapter
        rvCities.layoutManager = mLayoutManager
        rvCities.itemAnimator = DefaultItemAnimator()
        rvCities.layoutAnimation = animation
    }

    private fun getCitiesList(): ArrayList<CityInfo> {

        return arrayListOf(
            CityInfo("Львів", "Lviv"),
            CityInfo("Київ", "Kiev"),
            CityInfo("Вінниця", "Vinnytsia"),
            CityInfo("Одеса", "Odessa"),
            CityInfo("Харків", "Kharkiv")
        )
    }

    override fun onResume()
    {
        super.onResume()

        if(!isNetworkAvailable(applicationContext))
            Toast.makeText(applicationContext, "Check internet connection", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if(requestCode == WEATHER_INFO_REQUEST && resultCode == Activity.RESULT_OK)
        {
            val temperature: String = data!!.getStringExtra(WeatherActivity.TEMPERATURE_KEY)
            temperatureLabel.visibility = View.VISIBLE
            temperatureLabel.text = temperature
        }
    }

    override fun onClick(v: View?) {

        val intent = Intent(applicationContext, WeatherActivity::class.java)

        temperatureLabel =  v!!.findViewById(R.id.tvTemperature)

        val bundle = Bundle()
        val obj = v.tag as CityInfo
        bundle.putString("CityKey", obj.key)
        bundle.putString("CityNameUkr", obj.nameUkr)
        intent.putExtras(bundle)

        startActivityForResult(intent, WEATHER_INFO_REQUEST, bundle)
    }
}
