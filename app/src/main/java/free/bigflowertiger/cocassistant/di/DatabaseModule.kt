package free.bigflowertiger.cocassistant.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import free.bigflowertiger.cocassistant.data.room.CounterDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideTimerDatabase(@ApplicationContext appContext: Context) =
        CounterDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideCounterDao(db: CounterDatabase) = db.counterDao()

}