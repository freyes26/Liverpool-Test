package com.liverpool.test.liverpooltest.repository

import com.liverpool.test.liverpool.Constants
import com.liverpool.test.liverpooltest.repository.database.DatabaseRepository
import com.liverpool.test.liverpooltest.repository.network.NetworkRepository

class ValidatorFactory {


    //This method allows you to obtain a repository depending on the selector
    fun getValidator(selector: String): Repository? = when {
        isRetrofit(selector) -> NetworkRepository()
        isRetrofit(selector) -> DatabaseRepository()
        else -> null
    }

    fun isRetrofit(selector: String) = selector == Constants.IS_RETROFIT
    fun isDatabase(selector: String) = selector == Constants.IS_DATABASE
}