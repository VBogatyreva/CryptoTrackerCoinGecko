package ru.netology.cryptotrackercoingecko.domain

class GetCoinDetailsUseCase (private val repository: CoinRepository) {

//    fun getCoinDetail(id: String): CoinInfo {
//        return repository.getCoinDetail(id)
//    }

    operator fun invoke(id: String): CoinInfo = repository.getCoinDetail(id)

}