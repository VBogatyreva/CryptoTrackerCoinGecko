package ru.netology.cryptotrackercoingecko.data.network

import com.google.gson.annotations.SerializedName
import ru.netology.cryptotrackercoingecko.domain.CoinInfo

data class CoinDto(
    @SerializedName("id") val id: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("name") val name: String,
    @SerializedName("current_price") val currentPrice: Double?,
    @SerializedName("last_updated") val lastUpdated: String?,
    @SerializedName("high_24h") val high24h: Double?,
    @SerializedName("low_24h") val low24h: Double?,
    @SerializedName("image") val image: String?
) {
    fun toCoinInfo(currency: String = "usd"): CoinInfo {
        return CoinInfo(
            id = this.id,
            fromSymbol = symbol.uppercase(),
            toSymbol = currency.uppercase(),
            price = currentPrice?.let { "%.2f".format(it).replace(",", ".") },
            lastUpdate = lastUpdated?.let { parseDateToTimestamp(it) } ?: System.currentTimeMillis(),
            highDay = high24h?.let { "%.2f".format(it).replace(",", ".") },
            lowDay = low24h?.let { "%.2f".format(it).replace(",", ".") },
            lastMarket = "CoinGecko",
            imageUrl = image
        )
    }

    private fun parseDateToTimestamp(dateString: String): Long {
        return try {
            val format = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.getDefault())
            format.timeZone = java.util.TimeZone.getTimeZone("UTC")
            val date = format.parse(dateString)
            date?.time ?: System.currentTimeMillis()
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }
}