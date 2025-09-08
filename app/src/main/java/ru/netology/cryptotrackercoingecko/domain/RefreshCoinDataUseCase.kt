package ru.netology.cryptotrackercoingecko.domain

class RefreshCoinDataUseCase (private val repository: CoinRepository) {

//    fun refreshCoin() {
//        repository.refreshCoinList()
//    }

    operator fun invoke() = repository.refreshCoinList()
}