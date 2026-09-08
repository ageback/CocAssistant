package free.bigflowertiger.cocassistant

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.AlarmClock
import android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.core.net.toUri
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import free.bigflowertiger.cocassistant.ui.theme.CocAssistantTheme
import free.bigflowertiger.cocassistant.ui.timer.AlarmAppInfo
import free.bigflowertiger.cocassistant.ui.timer.DurationSettingScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
//        WindowCompat.setDecorFitsSystemWindows(window,false)

        super.onCreate(savedInstanceState)
//        if (!isIgnoringBatteryOptimizations(this)) {
//            requestIgnoreBatteryOptimizations(this)
//        }
        setContent {
            CocAssistantTheme {
                DurationSettingScreen { data, app ->
                    startOSTimer(
                        data.timerNamer,
                        data.durationSeconds,
                        app
                    )
                }
            }
        }
    }

    fun startOSTimer(
        alarmMessage: String,
        durationSeconds: Long,
        app: AlarmAppInfo?,
        skipUi: Boolean = true
    ) {
        val intent = Intent(AlarmClock.ACTION_SET_TIMER).apply {
            // 这里必须是 Int 类型，不能用 Long. 否则调用失败。
            putExtra(AlarmClock.EXTRA_LENGTH, durationSeconds.toInt())
            putExtra(AlarmClock.EXTRA_MESSAGE, alarmMessage)
            putExtra(AlarmClock.EXTRA_SKIP_UI, skipUi)
            app?.let {
                setPackage(it.packageName)
            }
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }


    @SuppressLint("BatteryLife")
    fun requestIgnoreBatteryOptimizations(context: Context) {

        val powerManager = context.getSystemService(PowerManager::class.java)

        if (powerManager.isIgnoringBatteryOptimizations(context.packageName)) {
            return
        }

        val intent = Intent(ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
            data = "package:${context.packageName}".toUri()
        }

        context.startActivity(intent)
    }

    fun isIgnoringBatteryOptimizations(context: Context): Boolean {
        val powerManager = context.getSystemService(PowerManager::class.java)
        return powerManager.isIgnoringBatteryOptimizations(context.packageName)
    }

    fun requestExactAlarmPermission(
        context: Context
    ) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            val intent = Intent(
                ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                "package:${context.packageName}".toUri()
            )

            context.startActivity(intent)
        }
    }

}