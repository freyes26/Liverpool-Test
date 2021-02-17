package com.liverpool.test.liverpooltest.repository.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo


//Check the status of the user's internet connection
object Connection {
    fun isConnected(context: Context):Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork : NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}