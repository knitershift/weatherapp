package com.example.learnapp.activities

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.learnapp.R
import com.example.learnapp.isNetworkAvailable
import com.example.learnapp.models.WeatherInfo
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_weather.*
import okhttp3.*
import java.io.IOException
import java.net.HttpURLConnection
import android.os.Handler




class WeatherActivity : AppCompatActivity() {

    var client = OkHttpClient()
    var cityKey: String? = null
    var temperature: String? = null

    companion object {
        const val TEMPERATURE_KEY:String = "temperature_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        extractInputData()
        showLoadingAnimation()
        getWeather()
    }

    private fun extractInputData() {
        val bundle: Bundle = intent.extras!!
        cityKey =  bundle.getString("CityKey")
        tvCityName.text =  bundle.getString("CityNameUkr")
    }

    private fun showLoadingAnimation() {
        llWrapWeather.visibility = View.GONE
        llAnimationWrapper.visibility = View.VISIBLE

        //load animation into image view
        Glide.with(applicationContext)
            .load(R.drawable.loading)
            .into(ivLoadingAnimation)
    }

    private fun hideLoadingAnimation() {
        llAnimationWrapper.visibility = View.GONE
        llWrapWeather.visibility = View.VISIBLE
    }

    private fun getWeather() {

        if(!isNetworkAvailable(applicationContext))
        {
            Toast.makeText(applicationContext, getString(R.string.CheckInternetConnection), Toast.LENGTH_SHORT).show()
            return
        }

        val requestURL: String = HttpUrl.parse(getString(R.string.API_URL))!!.newBuilder()
            .addQueryParameter("q", "$cityKey,ua")
            .addQueryParameter("mode", "json")
            .addQueryParameter("lang", "ua")
            .addQueryParameter("appID", getString(R.string.API_KEY))
            .build().toString()

        val request = Request.Builder().url(requestURL).build()

        client.newCall(request).enqueue(
        object : Callback {

            override fun onResponse(call: Call, response: Response) {

                if (response.code() == HttpURLConnection.HTTP_OK) {

                    val json: String = response.body()!!.string()
                    val weatherInfo: WeatherInfo = Gson().fromJson(json, WeatherInfo::class.java)
                    initWeatherInfoUI(weatherInfo)

                } else {

                    runOnUiThread {
                        Toast.makeText(applicationContext, "Bad Request", Toast.LENGTH_SHORT).show()
                    }

                }

            }

            override fun onFailure(call: Call, e: IOException) {
                Log.i("MyWeather", "Fail")
            }

        })
    }

    private fun initWeatherInfoUI(weatherInfo: WeatherInfo) {

        runOnUiThread {
            val str:String = "%.0f".format(weatherInfo.main.temp)
            temperature = "$str °C"

            if(weatherInfo.main.temp > 0)
                temperature = "+$temperature"

            val iconURL = "http://openweathermap.org/img/w/${weatherInfo.weather[0].icon}.png"

            // set weather icon
            Glide.with(applicationContext)
                .load(iconURL)
                .into(ivWeatherIcon)

            tvDescription.text = weatherInfo.weather[0].description
            tvMinTemperature.text = "${weatherInfo.main.temp_min} °C"
            tvMaxTemperature.text = "${weatherInfo.main.temp_max} °C"

            tvClouds.text       = "${weatherInfo.clouds.all}%"
            tvHumidity.text     = "${weatherInfo.main.humidity}%"
            tvWindSpeed.text    = "${weatherInfo.wind.speed} м/сек"
            tvPressure.text     = "${weatherInfo.main.pressure} мм"
            tvTemperature.text  = temperature

            Handler().postDelayed({ hideLoadingAnimation() }, 500)
        }
    }

    // return to previous activity
    override fun onBackPressed() {

        // save city temperature in activity result intent object
        if(temperature != null)
        {
            val intent = Intent()
            intent.putExtra(TEMPERATURE_KEY, temperature)
            setResult(Activity.RESULT_OK, intent)
        }
        else
            setResult(Activity.RESULT_CANCELED)

        finish()
    }
}
