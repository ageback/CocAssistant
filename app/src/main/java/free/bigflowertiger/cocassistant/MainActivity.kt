package free.bigflowertiger.cocassistant

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import free.bigflowertiger.cocassistant.ui.screen.SpeedCalcScreen
import free.bigflowertiger.cocassistant.ui.theme.CocAssistantTheme
import free.bigflowertiger.cocassistant.ui.timer.TimerScreen

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
                val locationPermissionState =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        rememberMultiplePermissionsState(
                            listOf(
                                android.Manifest.permission.POST_NOTIFICATIONS
                            )
                        )
                    } else {
                        TODO("VERSION.SDK_INT < TIRAMISU")
                    }
                LaunchedEffect(Unit) {
                    locationPermissionState.launchMultiplePermissionRequest()
                }

                if (locationPermissionState.allPermissionsGranted) {
                    TimerScreen()
                }
            }
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