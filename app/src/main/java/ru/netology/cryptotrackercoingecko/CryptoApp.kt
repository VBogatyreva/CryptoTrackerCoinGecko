package ru.netology.cryptotrackercoingecko

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class CryptoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}