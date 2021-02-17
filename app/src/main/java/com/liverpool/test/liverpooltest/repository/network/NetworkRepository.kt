package com.liverpool.test.liverpooltest.repository.network

import com.liverpool.test.liverpool.Constants
import com.liverpool.test.liverpooltest.BaseApplication
import com.liverpool.test.liverpooltest.repository.Repository
import com.liverpool.test.liverpooltest.repository.network.json.response.PlpResults
import com.liverpool.test.liverpooltest.repository.network.json.response.Records

class NetworkRepository : Repository {


    //Get a list of the search results products
    //receives as parameter the search string, the page number and the number of items per page
    override suspend fun getProducts(
            search: String,
            pageNumber: Int,
            itemsPerPage: Int
    ): List<Records>? {
        val options: MutableMap<String, String> = mutableMapOf()
        options[Constants.parameters.Force_PHP] = "true"
        options[Constants.parameters.SEARCH_STRING] = search
        options[Constants.parameters.PAGE_NUMBER] = pageNumber.toString()
        options[Constants.parameters.ITEMS_PER_PAGE] = itemsPerPage.toString()
        return BaseApplication.application.rest.getProducts(options)?.plpResults?.records
    }


    //Get the number of results obtained from the search,
    //receives the search string as parameters
    override suspend fun getPlpState(search: String): PlpResults? {
        val options: MutableMap<String, String> = mutableMapOf()
        options[Constants.parameters.Force_PHP] = "true"
        options[Constants.parameters.SEARCH_STRING] = search
        return BaseApplication.application.rest.getProducts(options)?.plpResults
    }
}