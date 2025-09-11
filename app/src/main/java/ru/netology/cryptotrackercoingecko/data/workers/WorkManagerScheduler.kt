package ru.netology.cryptotrackercoingecko.data.workers

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkManagerScheduler @Inject constructor(
    private val context: Context
) {

    fun schedulePeriodicSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val syncWorkRequest: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<RefreshDataWorker>(
                2, TimeUnit.HOURS,
                15, TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .addTag("coin_sync")
                .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            syncWorkRequest
        )
    }

    fun cancelPeriodicSync() {
        WorkManager.getInstance(context).cancelUniqueWork(RefreshDataWorker.WORK_NAME)
    }

    fun scheduleOneTimeSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncWorkRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(
            15, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .addTag("coin_sync_immediate")
            .build()

        WorkManager.getInstance(context).enqueue(syncWorkRequest)
    }
}