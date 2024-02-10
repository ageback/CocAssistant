package free.bigflowertiger.cocassistant.nitification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import free.bigflowertiger.cocassistant.R

const val COUNTE_DOWN_CHANNEL = "CountDown"

object NotificationHelper {
    fun createCountdownNotification(context: Context) {
        val builder = NotificationCompat.Builder(context, COUNTE_DOWN_CHANNEL)
        val notification = builder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_stat_timer)
            .setContentText("研究完成")
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        val channel = NotificationChannel(
            COUNTE_DOWN_CHANNEL,
            COUNTE_DOWN_CHANNEL,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(0, notification)
    }
}