package free.bigflowertiger.cocassistant.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import free.bigflowertiger.cocassistant.R
import free.bigflowertiger.cocassistant.data.room.entity.TimerEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimerNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val notificationManager = NotificationManagerCompat.from(context)

    init {
        createChannel()
    }

    private fun createChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "倒计时提醒",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "倒计时结束提醒"
        }

        notificationManager.createNotificationChannel(channel)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showTimerFinished(timer: TimerEntity) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notifications_active_24px)
            .setContentTitle(timer.title)
            .setContentText("倒计时结束")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(timer.id.hashCode(), notification)
    }

    companion object {
        const val CHANNEL_ID = "timer_finished"
    }
}