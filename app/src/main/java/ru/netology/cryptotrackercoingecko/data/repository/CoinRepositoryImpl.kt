package ru.netology.cryptotrackercoingecko.data.repository

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.netology.cryptotrackercoingecko.data.database.AppDatabase
import ru.netology.cryptotrackercoingecko.data.database.CoinInfoDao
import ru.netology.cryptotrackercoingecko.data.database.CoinInfoDbModel
import ru.netology.cryptotrackercoingecko.data.mapper.CoinInfoMapper
import ru.netology.cryptotrackercoingecko.data.network.CoinApiFactory
import ru.netology.cryptotrackercoingecko.domain.CoinInfo
import ru.netology.cryptotrackercoingecko.domain.CoinRepository

object CoinRepositoryImpl : CoinRepository {

    private lateinit var database: AppDatabase
    private lateinit var dao: CoinInfoDao
    private val apiService = CoinApiFactory.coinGeckoApiService
    private val mapper = CoinInfoMapper()

    fun init(context: Context) {
        database = AppDatabase.getInstance(context.applicationContext)
        dao = database.coinPriceInfoDao()
    }

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