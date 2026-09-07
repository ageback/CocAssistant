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
import free.bigflowertiger.cocassistant.ui.timer.DurationSettingScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
//        WindowCompat.setDecorFitsSystemWindows(window,false)

        super.onCreate(savedInstanceState)
        if (!isIgnoringBatteryOptimizations(this)) {
            requestIgnoreBatteryOptimizations(this)
        }
        setContent {
            CocAssistantTheme {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                    val notificationPermissionState =
                        rememberMultiplePermissionsState(
                            listOf(
                                android.Manifest.permission.POST_NOTIFICATIONS
                            )
                        )

                    LaunchedEffect(Unit) {
                        notificationPermissionState.launchMultiplePermissionRequest()
                    }

                    if (notificationPermissionState.allPermissionsGranted) {
                        DurationSettingScreen { data ->
                            startOSTimer(
                                data.timerNamer,
                                data.durationSeconds
                            )
                        }
                    }

                } else {
                    // Android 12 及以下无需 POST_NOTIFICATIONS
                    DurationSettingScreen { data ->
                        startOSTimer(
                            data.timerNamer,
                            data.durationSeconds
                        )
                    }
                }
            }
        }
    }

    fun startOSTimer(
        alarmMessage: String,
        durationSeconds: Long,
        skipUi: Boolean = true
    ) {
        val alarmApps = queryAlarmAppNames()
        val intent = Intent(AlarmClock.ACTION_SET_TIMER).apply {
            // 这里必须是 Int 类型，不能用 Long. 否则调用失败。
            putExtra(AlarmClock.EXTRA_LENGTH, durationSeconds.toInt())
            putExtra(AlarmClock.EXTRA_MESSAGE, alarmMessage)
            putExtra(AlarmClock.EXTRA_SKIP_UI, skipUi)
            if (alarmApps.isNotEmpty()) {
                setPackage(alarmApps[0].second)
            }
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    fun queryAlarmAppNames(): List<Pair<String, String?>> {
        val intent = Intent(AlarmClock.ACTION_SET_TIMER)
        val apps = packageManager.queryIntentActivities(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        )
        return apps.map {
            val packageName = it.activityInfo.packageName
            val appName = it.loadLabel(packageManager).toString()

            appName to packageName
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