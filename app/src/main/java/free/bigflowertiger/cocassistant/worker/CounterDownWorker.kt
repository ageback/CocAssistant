package free.bigflowertiger.cocassistant.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import free.bigflowertiger.cocassistant.MyApp.Companion.context
import free.bigflowertiger.cocassistant.nitification.NotificationHelper

@HiltWorker
open class CounterDownWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        NotificationHelper.createCountdownNotification(context = context)
        return Result.success()
    }
}