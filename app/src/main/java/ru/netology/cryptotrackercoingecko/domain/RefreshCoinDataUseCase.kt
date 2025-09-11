package ru.netology.cryptotrackercoingecko.domain

import javax.inject.Inject

class RefreshCoinDataUseCase @Inject constructor (private val repository: CoinRepository) {

    suspend operator fun invoke() = repository.refreshCoinList()
}