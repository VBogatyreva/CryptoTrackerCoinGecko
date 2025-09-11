package ru.netology.cryptotrackercoingecko.data.mapper

import ru.netology.cryptotrackercoingecko.data.database.CoinInfoDbModel
import ru.netology.cryptotrackercoingecko.data.network.CoinDto
import javax.inject.Inject

class CoinInfoMapper @Inject constructor () {

    private val currency: String = "USD"

    fun map(from: CoinDto): CoinInfoDbModel {
        return CoinInfoDbModel(
            id = from.id,
            fromSymbol = from.symbol.uppercase(),
            toSymbol = currency.uppercase(),
            price = from.currentPrice?.let { formatPrice(it) },
            lastUpdate = parseDateToTimestamp(from.lastUpdated ?: ""),
            highDay = from.high24h?.let { formatPrice(it) },
            lowDay = from.low24h?.let { formatPrice(it) },
            lastMarket = "CoinGecko",
            imageUrl = from.image
        )
    }

    private fun formatPrice(price: Double): String {
        return "%.2f".format(price).replace(",", ".")
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