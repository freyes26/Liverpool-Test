package com.liverpool.test.liverpooltest

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import com.liverpool.test.liverpool.Constants
import com.liverpool.test.liverpooltest.repository.database.SearchDatabase
import com.liverpool.test.liverpooltest.repository.network.ApiRetrofit
import com.liverpool.test.liverpooltest.repository.network.RestApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BaseApplication : Application() {
    companion object {
        lateinit var application: BaseApplication
        lateinit var context: Context
    }

    private val retrofitConfig: Retrofit by lazy {
        Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.retrofitConstants.BASE_URL)
                .build()
    }

    val rest by lazy { ApiRetrofit(application.retrofitConfig).create(RestApi::class.java) }
    val database by lazy { SearchDatabase.getDatabase(applicationContext) }


    override fun onCreate() {
        super.onCreate()
        application = this
        context = this.applicationContext
        Stetho.initializeWithDefaults(this)
    }
}