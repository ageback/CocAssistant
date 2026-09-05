package free.bigflowertiger.cocassistant.alarm

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.RequiresPermission
import dagger.hilt.android.AndroidEntryPoint
import free.bigflowertiger.cocassistant.data.room.dao.TimerDao
import free.bigflowertiger.cocassistant.data.room.entity.TimerStatus
import free.bigflowertiger.cocassistant.notification.TimerNotificationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TimerAlarmReceiver : BroadcastReceiver() {
//    @Inject
//    lateinit var timerDao: TimerDao

    @Inject
    lateinit var notificationManager: TimerNotificationManager

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent) {
        val timerId = intent.getStringExtra(AlarmScheduler.EXTRA_TIMER_ID) ?: return
        Log.d(
            "NNN",
            "收到alarm: id=$timerId, now=${System.currentTimeMillis()}"
        )
        notificationManager.testNotificaiton()
//        val pendingResult = goAsync()

/*
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val timer = timerDao.getById(timerId) ?: return@launch

                */
/*
                * 非 RUNNING 状态说明：
                *
                * 可能已经被用户取消
                * 或者这个 alarm 是旧的
                *//*

                if (timer.status != TimerStatus.RUNNING) {
                    return@launch
                }


                */
/*
                 * 发通知
                 *//*

                notificationManager.showTimerFinished(timer)
                */
/*
                * 更新数据库状态
                *//*

                timerDao.updateStatus(timerId, TimerStatus.FINISHED)

            } finally {
                pendingResult.finish()
            }
        }
*/
    }
}