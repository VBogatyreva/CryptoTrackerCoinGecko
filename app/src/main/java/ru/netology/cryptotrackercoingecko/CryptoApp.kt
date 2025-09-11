package ru.netology.cryptotrackercoingecko

import android.app.Application
import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen
import ru.netology.cryptotrackercoingecko.data.repository.CoinRepositoryImpl
import ru.netology.cryptotrackercoingecko.data.settings.LocaleHelper
import ru.netology.cryptotrackercoingecko.data.settings.SettingsManager
import java.util.Locale

class CryptoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CoinRepositoryImpl.init(this)
        AndroidThreeTen.init(this)
        SettingsManager(this).setupInitialTheme()
    }
    override fun attachBaseContext(base: Context) {
        val settingsManager = SettingsManager(base)
        super.attachBaseContext(
            LocaleHelper.setLocale(base, settingsManager.currentLanguage).also {
                Locale.setDefault(Locale(settingsManager.currentLanguage))
            }
        )
    }
}