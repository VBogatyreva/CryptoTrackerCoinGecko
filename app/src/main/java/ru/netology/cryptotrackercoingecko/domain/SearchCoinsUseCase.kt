package ru.netology.cryptotrackercoingecko.domain

class SearchCoinsUseCase (private val repository: CoinRepository) {

//    fun searchCoins(name: String): List<CoinInfo> {
//        return repository.searchCoins(name)
//    }

    operator fun invoke(name: String): List<CoinInfo> = repository.searchCoins(name)
}