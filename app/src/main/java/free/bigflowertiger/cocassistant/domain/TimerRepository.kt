package free.bigflowertiger.cocassistant.domain

import free.bigflowertiger.cocassistant.alarm.AlarmScheduler
import free.bigflowertiger.cocassistant.data.room.dao.TimerDao
import free.bigflowertiger.cocassistant.data.room.entity.TimerEntity
import free.bigflowertiger.cocassistant.data.room.entity.TimerStatus
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.uuid.Uuid

@Singleton
class TimerRepository @Inject constructor(
    private val timerDao: TimerDao,
    private val alarmScheduler: AlarmScheduler
) {
    fun observeRunningTimers() = timerDao.observeRunningTimers()

    suspend fun createTimer(title: String, durationMillis: Long): TimerEntity {
        require(durationMillis > 0) { "durationMillis 必须大于0" }
        val now = System.currentTimeMillis()
        val timer = TimerEntity(
            id = Uuid.random().toString(),
            title = title,
            startTime = now,
            endTime = now + durationMillis,
            status = TimerStatus.RUNNING
        )
        timerDao.insertTimer(timer)
        alarmScheduler.schedule(timer)
        return timer
    }

    suspend fun cancelTimer(timerId: String) {
        alarmScheduler.cancel(timerId)
        timerDao.updateStatus(timerId, TimerStatus.CANCELLED)
    }
}