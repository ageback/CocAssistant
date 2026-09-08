package free.bigflowertiger.cocassistant

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TimerApp : Application() {
    @Inject
    override fun onCreate() {
        super.onCreate()
        Inner.instance = this
        context = applicationContext
    }

    private object Inner {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: TimerApp
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
}