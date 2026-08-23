package free.bigflowertiger.cocassistant.alarm

import free.bigflowertiger.cocassistant.data.room.dao.TimerDao
import free.bigflowertiger.cocassistant.data.room.entity.TimerStatus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmRestoreManager @Inject constructor(
    private val alarmScheduler: AlarmScheduler,
    private val timerDao: TimerDao
) {

    suspend fun restore() {
        val timers = timerDao.getRunningTimers()
        val now = System.currentTimeMillis()
        timers.forEach { timer ->
            if (timer.endTime <= now) {
                timerDao.updateStatus(timer.id, TimerStatus.FINISHED)
            } else {
                alarmScheduler.schedule(timer)
            }
        }
    }
}