package ru.netology.cryptotrackercoingecko.domain

import javax.inject.Inject

class GetCoinDetailsUseCase @Inject constructor (private val repository: CoinRepository) {

    suspend operator fun invoke(id: String): CoinInfo = repository.getCoinDetail(id)
}