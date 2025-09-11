package ru.netology.cryptotrackercoingecko.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoinListUseCase @Inject constructor (private val repository: CoinRepository) {

    operator fun invoke(): Flow<List<CoinInfo>> = repository.getCoinList()
}