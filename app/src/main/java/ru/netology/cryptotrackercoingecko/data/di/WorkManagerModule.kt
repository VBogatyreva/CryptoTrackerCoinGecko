package ru.netology.cryptotrackercoingecko.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.netology.cryptotrackercoingecko.data.workers.WorkManagerScheduler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkManagerModule {

    @Provides
    @Singleton
    fun provideWorkManagerScheduler(@ApplicationContext context: Context): WorkManagerScheduler {
        return WorkManagerScheduler(context)
    }
}