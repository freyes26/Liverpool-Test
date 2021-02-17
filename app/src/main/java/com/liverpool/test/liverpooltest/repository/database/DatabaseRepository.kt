package com.liverpool.test.liverpooltest.repository.database

import com.liverpool.test.liverpooltest.repository.Repository
import com.liverpool.test.liverpooltest.repository.network.json.response.PlpResults
import com.liverpool.test.liverpooltest.repository.network.json.response.Records

class DatabaseRepository : Repository {

    //In case the search results will be stored, this function should be used if you do not have internet
    override suspend fun getProducts(
        search: String,
        pageNumber: Int,
        itemsPerPage: Int
    ): List<Records>? {
        TODO("Not yet implemented")
    }
    // In case the order status is stored, this code should be used if you do not have internet
    override suspend fun getPlpState(search: String): PlpResults? {
        TODO("Not yet implemented")
    }
}