package com.liverpool.test.liverpooltest.repository

import com.liverpool.test.liverpool.Constants
import com.liverpool.test.liverpooltest.repository.network.NetworkRepository

class ValidatorFactory {
    fun getValidator(selector : String) : Repository? = when {
        isRetrofit(selector) -> NetworkRepository()
        else -> null
    }

    fun isRetrofit(selector: String) = selector == Constants.IS_RETROFIT
}