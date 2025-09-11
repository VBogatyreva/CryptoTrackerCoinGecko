package ru.netology.cryptotrackercoingecko.data.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.cryptotrackercoingecko.data.repository.CoinRepositoryImpl
import ru.netology.cryptotrackercoingecko.data.settings.SettingsManager
import ru.netology.cryptotrackercoingecko.domain.CoinRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideSettingsManager(context: Context): SettingsManager = SettingsManager(context)

    @Provides
    @Singleton
    fun provideCoinRepository(
        coinRepositoryImpl: CoinRepositoryImpl
    ): CoinRepository {
        return coinRepositoryImpl
    }
}