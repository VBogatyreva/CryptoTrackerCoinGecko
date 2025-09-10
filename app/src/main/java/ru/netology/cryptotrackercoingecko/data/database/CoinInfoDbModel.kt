package ru.netology.cryptotrackercoingecko.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coin_info")
data class CoinInfoDbModel(
    @PrimaryKey
    val id: String,
    val fromSymbol: String,
    val toSymbol: String?,
    val price: String?,
    val lastUpdate: Long?,
    val highDay: String?,
    val lowDay: String?,
    val lastMarket: String?,
    val imageUrl: String?
)
