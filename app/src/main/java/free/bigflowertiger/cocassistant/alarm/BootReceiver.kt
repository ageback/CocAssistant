package free.bigflowertiger.cocassistant.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import free.bigflowertiger.cocassistant.data.room.dao.TimerDao
import free.bigflowertiger.cocassistant.data.room.entity.TimerStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {
    @Inject
    lateinit var timerDao: TimerDao

    @Inject
    lateinit var alarmScheduler: AlarmScheduler
    override fun onReceive(context: Context, intent: Intent) {
        if (
            intent.action != Intent.ACTION_BOOT_COMPLETED &&
            intent.action != Intent.ACTION_MY_PACKAGE_REPLACED
        ) {
            return
        }

        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val timers = timerDao.getRunningTimers()
                val now = System.currentTimeMillis()
                timers.forEach { timer ->
                    if (timer.endTime <= now) {
                        timerDao.updateStatus(timer.id, TimerStatus.FINISHED)
                    } else {
                        alarmScheduler.schedule(timer)
                    }
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}