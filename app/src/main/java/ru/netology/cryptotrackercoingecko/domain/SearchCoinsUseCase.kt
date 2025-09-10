package ru.netology.cryptotrackercoingecko.domain

import kotlinx.coroutines.flow.Flow

class SearchCoinsUseCase (private val repository: CoinRepository) {

    operator fun invoke(name: String): Flow<List<CoinInfo>> = repository.searchCoins(name)
}