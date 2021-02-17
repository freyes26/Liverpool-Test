package com.liverpool.test.liverpooltest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.liverpool.test.liverpool.Constants
import com.liverpool.test.liverpooltest.repository.ValidatorFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val repository by lazy { ValidatorFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        GlobalScope.launch {
            try{
                val result = withContext(Dispatchers.IO){
                    repository.getValidator(Constants.IS_RETROFIT)?.getPlpState("iphone")
                }
                if(result != null) {
                    Log.d(Constants.LOG_TAG,result.plpState.toString())
                }
                else{
                    Log.d(Constants.LOG_TAG,"algo salio mal")
                }
            }catch (e : Throwable){
                Log.d(Constants.LOG_TAG, e.message.toString())
            }


        }
    }
}