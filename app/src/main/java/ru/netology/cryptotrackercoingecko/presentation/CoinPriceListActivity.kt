package ru.netology.cryptotrackercoingecko.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.netology.cryptotrackercoingecko.R
import ru.netology.cryptotrackercoingecko.domain.CoinInfo

class CoinPriceListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_price_list)

        val recyclerView = findViewById<RecyclerView>(R.id.rvCoinPriceList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val coins = listOf(
            CoinInfo(
                fromSymbol = "BTC",
                toSymbol = "USD",
                price = "43250.75",
                lastUpdate = System.currentTimeMillis(),
                highDay = "43500.20",
                lowDay = "42800.50",
                lastMarket = "binance",
                imageUrl = "https://coin-images.coingecko.com/coins/images/1/small/bitcoin.png"
            ),
            CoinInfo(
                fromSymbol = "ETH",
                toSymbol = "USD",
                price = "3000.45",
                lastUpdate = System.currentTimeMillis() - 3600000, // час назад
                highDay = "3050.80",
                lowDay = "2980.30",
                lastMarket = "coinbase",
                imageUrl = "https://coin-images.coingecko.com/coins/images/279/small/ethereum.png"
            ),
            CoinInfo(
                fromSymbol = "ADA",
                toSymbol = "USD",
                price = "0.45",
                lastUpdate = System.currentTimeMillis() - 1800000, // 30 минут назад
                highDay = "0.48",
                lowDay = "0.43",
                lastMarket = "kraken",
                imageUrl = "https://coin-images.coingecko.com/coins/images/975/small/cardano.png"
            ),
            CoinInfo(
                fromSymbol = "DOT",
                toSymbol = "USD",
                price = "6.75",
                lastUpdate = System.currentTimeMillis() - 900000, // 15 минут назад
                highDay = "6.90",
                lowDay = "6.60",
                lastMarket = "kucoin",
                imageUrl = "https://coin-images.coingecko.com/coins/images/12171/small/polkadot.png"
            ),
            CoinInfo(
                fromSymbol = "SOL",
                toSymbol = "USD",
                price = "95.30",
                lastUpdate = System.currentTimeMillis() - 120000, // 2 минуты назад
                highDay = "96.50",
                lowDay = "94.20",
                lastMarket = "ftx",
                imageUrl = "https://coin-images.coingecko.com/coins/images/4128/small/solana.png"
            )
        )
        recyclerView.adapter = CoinAdapter(coins)
    }
}