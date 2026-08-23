package free.bigflowertiger.cocassistant.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timers")
data class TimerEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "start_time")
    val startTime: Long,

    @ColumnInfo(name = "end_time")
    val endTime: Long,

    @ColumnInfo(name = "status")
    val status: TimerStatus
)

enum class TimerStatus{
    RUNNING,
    PAUSED,
    FINISHED,
    CANCELLED
}