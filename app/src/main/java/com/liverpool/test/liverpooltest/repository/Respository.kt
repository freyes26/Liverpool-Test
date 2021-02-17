package com.liverpool.test.liverpooltest.repository

import com.liverpool.test.liverpooltest.repository.network.json.response.PlpResults
import com.liverpool.test.liverpooltest.repository.network.json.response.Records

interface Repository {
    suspend fun getProducts(search: String, pageNumber: Int, itemsPerPage: Int): List<Records>?

    suspend fun getPlpState(search: String): PlpResults?
}