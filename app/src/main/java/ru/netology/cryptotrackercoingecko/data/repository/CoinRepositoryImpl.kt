package ru.netology.cryptotrackercoingecko.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.netology.cryptotrackercoingecko.data.network.CoinApiFactory
import ru.netology.cryptotrackercoingecko.domain.CoinInfo
import ru.netology.cryptotrackercoingecko.domain.CoinRepository
import java.util.concurrent.TimeUnit


object CoinRepositoryImpl : CoinRepository {

    private val apiService = CoinApiFactory.coinGeckoApiService

    private val _coinList = MutableStateFlow<List<CoinInfo>>(emptyList())
    val coinList: StateFlow<List<CoinInfo>> = _coinList.asStateFlow()

    private var refreshInterval = TimeUnit.MINUTES.toMillis(5)
    private var lastRefreshTime = 0L

    override suspend fun getCoinList(): List<CoinInfo> {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastRefreshTime > refreshInterval || _coinList.value.isEmpty()) {
            refreshCoinList()
        }
        return _coinList.value
    }

    override suspend fun getCoinDetail(id: String): CoinInfo {
        return refreshCoinDetail(id) ?: throw Exception("Coin details aren't available")
    }

    override suspend fun refreshCoinList() {
        try {
            println("Attempting to fetch coins from CoinGecko...")
            val response = apiService.getCoinList()
            println("Response code: ${response.code()}")

            if (response.isSuccessful) {
                val coins = response.body()?.map { it.toCoinInfo() } ?: emptyList()
                println("Fetched ${coins.size} coins")

                _coinList.value = coins
                lastRefreshTime = System.currentTimeMillis()
            } else {
                val errorBody = response.errorBody()?.string()
                println("Error: $errorBody")
                throw Exception("Failed to fetch coin list: ${response.code()} - $errorBody")
            }
        } catch (e: Exception) {
            println("Exception: ${e.message}")
            throw Exception("Failed to fetch coin list: ${e.message}")
        }
    }

    private suspend fun refreshCoinDetail(id: String): CoinInfo? {
        try {
            val response = apiService.getCoinDetail(id)
            if (response.isSuccessful) {
                val coinDetail = response.body()
                return coinDetail?.toCoinInfo()
            } else {
                println("Error fetching coin detail: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            println("Exception fetching coin detail: ${e.message}")
        }
        return null
    }

    override suspend fun searchCoins(name: String): List<CoinInfo> {
        return _coinList.value.filter {
            it.fromSymbol.contains(name, ignoreCase = true)
        }
    }

    private fun ru.netology.cryptotrackercoingecko.data.network.CoinDetailResponse.toCoinInfo(): CoinInfo {
        return CoinInfo(
            id = this.id,
            fromSymbol = symbol.uppercase(),
            toSymbol = "USD",
            price = marketData?.currentPrice?.get("usd")?.toString(),
            lastUpdate = lastUpdated?.let { parseDateToTimestamp(it) }
                ?: System.currentTimeMillis(),
            highDay = marketData?.high24h?.get("usd")?.toString(),
            lowDay = marketData?.low24h?.get("usd")?.toString(),
            lastMarket = "CoinGecko",
            imageUrl = image?.large ?: image?.small ?: image?.thumb
        )
    }

    private fun parseDateToTimestamp(dateString: String): Long {
        return try {
            val format = java.text.SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                java.util.Locale.getDefault()
            )
            format.timeZone = java.util.TimeZone.getTimeZone("UTC")
            val date = format.parse(dateString)
            date?.time ?: System.currentTimeMillis()
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }
}