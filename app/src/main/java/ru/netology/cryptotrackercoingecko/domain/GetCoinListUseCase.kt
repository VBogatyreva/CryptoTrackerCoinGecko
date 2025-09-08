package ru.netology.cryptotrackercoingecko.domain

class GetCoinListUseCase (private val repository: CoinRepository) {

//    fun getCoinList(): List<CoinInfo> {
//        return repository.getCoinList()
//    }

    operator fun invoke(): List<CoinInfo> = repository.getCoinList()
}