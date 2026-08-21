package free.bigflowertiger.cocassistant.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import free.bigflowertiger.cocassistant.data.room.entity.CounterTimer

@Dao
interface CounterDao {
    @Insert
    fun insertTimer(counterTimer: CounterTimer)
}