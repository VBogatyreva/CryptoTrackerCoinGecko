package ru.netology.cryptotrackercoingecko.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchCoinsUseCase @Inject constructor (private val repository: CoinRepository) {

    operator fun invoke(name: String): Flow<List<CoinInfo>> = repository.searchCoins(name)
}