package com.liverpool.test.liverpooltest.repository.network.json.response

import java.text.NumberFormat

class Records(
        val productId: String,
        val skuRepositoryId: String,
        val productDisplayName: String,
        val productType: String,
        val productRatingCount: Int,
        val productAvgRating: Double,
        val listPrice: Double,
        val minimumListPrice: Double,
        val maximumListPrice: Double,
        val promoPrice: Double,
        val minimumPromoPrice: Double,
        val maximumPromoPrice: Double,
        val isHybrid: Boolean,
        val isMarketPlace: Boolean,
        val isImportationProduct: Boolean,
        val brand: String,
        val seller: String,
        val category: String,
        val smImage: String,
        val lgImage: String,
        val xlImage: String,
        val groupType: String,
        val variantsColor: List<VariantsColor>
) {
    fun getPrice() = NumberFormat.getCurrencyInstance().format(listPrice)
}