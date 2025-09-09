package ru.netology.cryptotrackercoingecko.domain

data class CoinInfo(

    val id: String,
    val fromSymbol: String,   // Базовая валюта (какую монету покупаем).
    val toSymbol: String?,    // Котируемая валюта (за какую валюту покупаем).
    val price: String?,       // Текущая цена в указанной валюте
    val lastUpdate: Long?,    // Время последнего обновления
    val highDay: String?,     // Максимальная цена за 24 часа
    val lowDay: String?,      // Минимальная цена за 24 часа
    val lastMarket: String?,  // Последняя биржа, где прошла сделка
    val imageUrl: String?     // URL изображения монеты
)
