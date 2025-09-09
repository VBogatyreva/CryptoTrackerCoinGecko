package ru.netology.cryptotrackercoingecko.domain

class GetCoinListUseCase (private val repository: CoinRepository) {

    suspend operator fun invoke(): List<CoinInfo> = repository.getCoinList()
}