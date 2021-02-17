package com.liverpool.test.liverpooltest.repository.network

import com.liverpool.test.liverpool.Constants
import com.liverpool.test.liverpooltest.repository.Repository

class NetworkRepository  : Repository {
    override suspend fun getProducts(
        search: String,
        pageNumber: Int,
        itemsPerPage: Int
    ): List<Records>? {
        val options : MutableMap<String,String> = mutableMapOf()
        options[Constants.parameters.Force_PHP] = "true"
        options[Constants.parameters.SEARCH_STRING] = search
        options[Constants.parameters.PAGE_NUMBER] = pageNumber.toString()
        options[Constants.parameters.ITEMS_PER_PAGE] = itemsPerPage.toString()
        return BaseAppication.application.rest.getProducts(options)?.plpResults?.records
    }

    override suspend fun getPlpState(search: String): PlpResults? {
        val options : MutableMap<String,String> = mutableMapOf()
        options[Constants.parameters.Force_PHP] = "true"
        options[Constants.parameters.SEARCH_STRING] = search
        return BaseAppication.application.rest.getProducts(options)?.plpResults
    }
}