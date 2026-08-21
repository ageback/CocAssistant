package free.bigflowertiger.cocassistant.data.room

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import free.bigflowertiger.cocassistant.data.room.dao.CounterDao
import free.bigflowertiger.cocassistant.data.room.entity.CounterTimer
import free.bigflowertiger.cocassistant.data.room.entity.TimerStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [CounterTimer::class],
    version = 1,
    exportSchema = true
)
abstract class CounterDatabase : RoomDatabase() {
    abstract fun counterDao(): CounterDao

    companion object {
        private var instance: CounterDatabase? = null

        fun getDatabase(context: Context): CounterDatabase = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(
                context = appContext,
                klass = CounterDatabase::class.java,
                name = "CounterDB"
            ).addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    val counterDao = instance?.counterDao()
                    val currentTime = System.currentTimeMillis()
                    // 创建默认单词本
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            counterDao?.insertTimer(
                                CounterTimer(
                                    title = "默认",
                                    startTime = currentTime,
                                    endTime = currentTime,
                                    status = TimerStatus.Stopped
                                )
                            )
                        } catch (ex: Exception) {
                            Log.e("PrePopData", ex.message ?: "unknown error")
                        }
                    }
                }
            }).build()
    }
}