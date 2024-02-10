package free.bigflowertiger.cocassistant.worker

import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import free.bigflowertiger.cocassistant.MyApp
import java.util.concurrent.TimeUnit

const val WORK_NAME_COUNT_DOWN = "COUNT_DOWN"

object WorkerHelper {
    private val workManager = WorkManager.getInstance(MyApp.context)

    fun setCountDownWorker() {
        val request = OneTimeWorkRequest.Builder(CounterDownWorker::class.java)
            .setInitialDelay(0, TimeUnit.SECONDS)
            .build()
        workManager.enqueueUniqueWork(WORK_NAME_COUNT_DOWN, ExistingWorkPolicy.REPLACE, request)
    }
}