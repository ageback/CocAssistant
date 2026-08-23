package free.bigflowertiger.cocassistant.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import free.bigflowertiger.cocassistant.alarm.AlarmScheduler
import free.bigflowertiger.cocassistant.data.room.TimerDatabase
import free.bigflowertiger.cocassistant.data.room.dao.TimerDao
import free.bigflowertiger.cocassistant.domain.TimerRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideTimerDatabase(
        @ApplicationContext appContext: Context
    ): TimerDatabase = Room.databaseBuilder(
        context = appContext,
        klass = TimerDatabase::class.java,
        name = "TimerDB"
    ).build()

    @Singleton
    @Provides
    fun provideCounterDao(db: TimerDatabase) = db.counterDao()

    @Singleton
    @Provides
    fun provideTimerRepository(
        timerDao: TimerDao,
        alarmScheduler: AlarmScheduler
    ) = TimerRepository(
        timerDao, alarmScheduler
    )

}