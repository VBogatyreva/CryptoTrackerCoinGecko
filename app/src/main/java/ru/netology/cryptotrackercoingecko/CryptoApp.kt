package ru.netology.cryptotrackercoingecko

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import ru.netology.cryptotrackercoingecko.data.repository.CoinRepositoryImpl

class CryptoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CoinRepositoryImpl.init(this)
        AndroidThreeTen.init(this)
    }
}