package com.liverpool.test.liverpooltest.repository.network

import retrofit2.Retrofit

class ApiRetrofit(val retrofit : Retrofit) {
    fun <T> create(value : Class<T>): T {
        return retrofit.create(value)
    }
}