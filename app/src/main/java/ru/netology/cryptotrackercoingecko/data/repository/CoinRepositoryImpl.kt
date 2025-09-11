package ru.netology.cryptotrackercoingecko.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.netology.cryptotrackercoingecko.data.database.CoinInfoDao
import ru.netology.cryptotrackercoingecko.data.database.CoinInfoDbModel
import ru.netology.cryptotrackercoingecko.data.mapper.CoinInfoMapper
import ru.netology.cryptotrackercoingecko.data.network.CoinApiService
import ru.netology.cryptotrackercoingecko.domain.CoinInfo
import ru.netology.cryptotrackercoingecko.domain.CoinRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoinRepositoryImpl @Inject constructor(
    private val dao: CoinInfoDao,
    private val apiService: CoinApiService,
    private val mapper: CoinInfoMapper
) : CoinRepository {

    override fun getCoinList(): Flow<List<CoinInfo>> {
        return dao.getPriceList()
            .map { dbModels -> dbModels.map { mapDbModelToDomain(it) } }
    }

    override suspend fun getCoinDetail(id: String): CoinInfo {
        val dbModel = dao.getPriceInfoAboutCoin(id)
            ?: throw Exception("Coin details are not found in database")

        return mapDbModelToDomain(dbModel)
    }

    override suspend fun refreshCoinList() {
        try {
            val response = apiService.getCoinList()
            if (response.isSuccessful) {
                response.body()?.let { coinsDto ->
                    val dbModels = coinsDto.map { mapper.map(it) }
                    dao.insertPriceList(dbModels)
                } ?: throw Exception("Empty response body")
            } else {
                throw Exception("API error: ${response.code()} - ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            throw Exception("Network error: ${e.message}")
        }
    }

    override fun searchCoins(name: String): Flow<List<CoinInfo>> {
        return dao.getPriceList()
            .map { dbModels ->
                dbModels.filter {
                    it.fromSymbol.contains(name, ignoreCase = true) ||
                            it.id.contains(name, ignoreCase = true)
                }.map { mapDbModelToDomain(it) }
            }
    }

    override suspend fun needsRefresh(): Boolean {
        val coins = dao.getPriceList().first()
        if (coins.isEmpty()) return true                        // Нет данных - нужна синхронизация

        val lastUpdate = coins.maxOfOrNull { it.lastUpdate ?: 0L } ?: 0L
        val currentTime = System.currentTimeMillis()
        val twoHoursInMillis = 2 * 60 * 60 * 1000L

        return currentTime - lastUpdate > twoHoursInMillis       // Прошло больше 2 часов
    }

    private fun mapDbModelToDomain(dbModel: CoinInfoDbModel): CoinInfo {
        return CoinInfo(
            id = dbModel.id,
            fromSymbol = dbModel.fromSymbol,
            toSymbol = dbModel.toSymbol,
            price = dbModel.price,
            lastUpdate = dbModel.lastUpdate,
            highDay = dbModel.highDay,
            lowDay = dbModel.lowDay,
            lastMarket = dbModel.lastMarket,
            imageUrl = dbModel.imageUrl
        )
    }
}