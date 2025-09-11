package ru.netology.cryptotrackercoingecko

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import ru.netology.cryptotrackercoingecko.data.settings.SettingsManager
import java.util.Locale
import javax.inject.Inject

@HiltAndroidApp
class CryptoApp : Application() {

    @Inject
    lateinit var settingsManager: SettingsManager

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()

        // Инициализация локализации после создания зависимостей
        setupLocale()

        AndroidThreeTen.init(this)
        SettingsManager(this).setupInitialTheme()
    }

    private fun setupLocale() {
        val locale = Locale(settingsManager.currentLanguage)
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}