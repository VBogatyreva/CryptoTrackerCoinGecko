package ru.netology.cryptotrackercoingecko.domain

class GetCoinDetailsUseCase (private val repository: CoinRepository) {

    suspend operator fun invoke(id: String): CoinInfo = repository.getCoinDetail(id)
}