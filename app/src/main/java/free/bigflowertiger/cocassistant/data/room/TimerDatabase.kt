package free.bigflowertiger.cocassistant.data.room

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import free.bigflowertiger.cocassistant.data.room.dao.TimerDao
import free.bigflowertiger.cocassistant.data.room.entity.TimerEntity
import free.bigflowertiger.cocassistant.data.room.entity.TimerStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.uuid.Uuid

@Database(
    entities = [TimerEntity::class],
    version = 1,
    exportSchema = true
)
abstract class TimerDatabase : RoomDatabase() {
    abstract fun counterDao(): TimerDao

    companion object {
        private var instance: TimerDatabase? = null

        fun getDatabase(context: Context): TimerDatabase = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(
                context = appContext,
                klass = TimerDatabase::class.java,
                name = "TimerDB"
            ).build()
    }
}