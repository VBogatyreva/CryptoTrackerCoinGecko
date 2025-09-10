package ru.netology.cryptotrackercoingecko.domain

import kotlinx.coroutines.flow.Flow

class GetCoinListUseCase (private val repository: CoinRepository) {

    operator fun invoke(): Flow<List<CoinInfo>> = repository.getCoinList()
}