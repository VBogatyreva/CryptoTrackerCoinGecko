package ru.netology.cryptotrackercoingecko.data.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ru.netology.cryptotrackercoingecko.domain.CoinRepository

@HiltWorker
class RefreshDataWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val repository: CoinRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            if (repository.needsRefresh()) {
                repository.refreshCoinList()
                Result.success()
            } else {
                Result.success()
            }
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }
}