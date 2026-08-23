package free.bigflowertiger.cocassistant.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import free.bigflowertiger.cocassistant.data.room.entity.TimerEntity
import free.bigflowertiger.cocassistant.data.room.entity.TimerStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerDao {
    @Query(
        """
        SELECT * FROM timers
        WHERE status = 'RUNNING'
        ORDER BY end_time ASC
        """
    )
    fun observeRunningTimers(): Flow<List<TimerEntity>>

    @Query(
        """
        SELECT * FROM timers
        WHERE status = 'RUNNING'
        """
    )
    suspend fun getRunningTimers(): List<TimerEntity>

    @Query("SELECT * FROM timers WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): TimerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimer(counterTimer: TimerEntity)

    @Update
    suspend fun update(timer: TimerEntity)

    @Query("DELETE FROM timers WHERE id = :id")
    suspend fun delete(id: String)

    @Query(
        """
        UPDATE timers
        SET status = :status
        WHERE id = :id
        """
    )
    suspend fun updateStatus(
        id: String,
        status: TimerStatus
    )
}