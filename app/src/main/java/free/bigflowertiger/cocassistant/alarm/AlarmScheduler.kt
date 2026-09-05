package free.bigflowertiger.cocassistant.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.AlarmManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import free.bigflowertiger.cocassistant.data.room.entity.TimerEntity
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.jvm.java

@Singleton
class AlarmScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    fun canScheduleExactAlarms(): Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        alarmManager.canScheduleExactAlarms()
    } else {
        true
    }

    fun schedule(timer: TimerEntity) {
        check(canScheduleExactAlarms()) { "未授予精确闹钟权限" }

        val pendingIntent = getTimerPendingIntent(timer.id)
        val triggerAt = timer.endTime

        Log.d(
            "NNN",
            "schedule: id=${timer.id}, endTime=$triggerAt, now=${System.currentTimeMillis()}"
        )
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAt,
            pendingIntent
        )
        Log.d(
            "NNN",
            "schedule 完成: id=${timer.id}"
        )
    }


    fun cancel(timerId: String) {
        val pendingIntent = getTimerPendingIntent(timerId)
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    private fun getTimerPendingIntent(timerId: String): PendingIntent {
        val intent = Intent(context, TimerAlarmReceiver::class.java)
            .apply {
                action = ACTION_TIMER_ALARM
                putExtra(EXTRA_TIMER_ID, timerId)
            }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            timerId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return pendingIntent
    }


    companion object {
        const val ACTION_TIMER_ALARM = "free.bigerflowtiger.cocassistant.timer.ACTION_TIMER_ALARM"
        const val EXTRA_TIMER_ID = "timer_id"
    }
}