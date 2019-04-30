package com.example.learnapp

import android.content.Context
import android.net.ConnectivityManager


fun isNetworkAvailable(ctx: Context) : Boolean
{
    val connectivityManager = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}