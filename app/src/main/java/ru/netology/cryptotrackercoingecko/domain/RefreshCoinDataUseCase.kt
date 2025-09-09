package ru.netology.cryptotrackercoingecko.domain

class RefreshCoinDataUseCase (private val repository: CoinRepository) {

    suspend operator fun invoke() = repository.refreshCoinList()
}