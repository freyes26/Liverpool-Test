package com.liverpool.test.liverpooltest.repository.network

import com.liverpool.test.liverpooltest.repository.network.json.response.ResponseGetProducts
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RestApi {
    @GET("/appclienteservices/services/v3/plp?")
    suspend fun getProducts(@QueryMap options: Map<String, String>) : ResponseGetProducts?
}