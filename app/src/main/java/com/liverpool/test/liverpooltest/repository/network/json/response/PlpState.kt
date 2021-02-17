package com.liverpool.test.liverpooltest.repository.network.json.response

class PlpState(
        val categoryId: String,
        val currentSortOption: String,
        val currentFilters: String,
        val firstRecNum: Int,
        val lastRecNum: Int,
        val recsPerPage: Int,
        val totalNumRecs: Int,
        val originalSearchTerm: String
) {}