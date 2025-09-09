package ru.netology.cryptotrackercoingecko.data.network

import com.google.gson.annotations.SerializedName

data class CoinDetailResponse(
    @SerializedName("id") val id: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("name") val name: String,
    @SerializedName("market_data") val marketData: MarketData?,
    @SerializedName("last_updated") val lastUpdated: String?,
    @SerializedName("image") val image: ImageData?
)

data class MarketData(
    @SerializedName("current_price") val currentPrice: Map<String, Double>?,
    @SerializedName("high_24h") val high24h: Map<String, Double>?,
    @SerializedName("low_24h") val low24h: Map<String, Double>?
)

data class ImageData(
    @SerializedName("thumb") val thumb: String?,
    @SerializedName("small") val small: String?,
    @SerializedName("large") val large: String?
)