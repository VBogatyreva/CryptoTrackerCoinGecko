package ru.netology.cryptotrackercoingecko.domain

class SearchCoinsUseCase (private val repository: CoinRepository) {

    suspend operator fun invoke(name: String): List<CoinInfo> = repository.searchCoins(name)
}