package ru.netology.cryptotrackercoingecko.domain

interface CoinRepository {

    fun getCoinList(): List<CoinInfo>

    fun getCoinDetail(id: String): CoinInfo

    fun refreshCoinList()

    fun searchCoins(name: String): List<CoinInfo>
}