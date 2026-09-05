package free.bigflowertiger.cocassistant.ui.timer

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.AlarmClock
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import free.bigflowertiger.cocassistant.R
import free.bigflowertiger.cocassistant.data.room.entity.TimerStatus
import kotlin.time.Duration

@OptIn(ExperimentalGridApi::class)
@Composable
fun TimerScreen(
    onSave: (duration: Duration) -> Unit
) {
    val context = LocalContext.current
    val (showTimerDialog, toggleTimerDialog) = remember { mutableStateOf(false) }
    val viewModel = hiltViewModel<TimerViewModel>()
    val timers by viewModel.timersFlow.collectAsState()
    val activeTimersCount = timers.filter { it.status == TimerStatus.RUNNING }.size

    fun requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(
                ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                "package:${context.packageName}".toUri()
            )

            context.startActivity(intent)
        }
    }

    LaunchedEffect(Unit) {
        if (!viewModel.canScheduleExactAlarms()) {
            requestExactAlarmPermission()
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { toggleTimerDialog(true) }) {
                Icon(painterResource(R.drawable.add_24px), "")
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item { Text(text = "$activeTimersCount 个启用的计时器") }
            items(items = timers, key = { it.id }) { timer ->
                TimerItem(timer)
            }
        }
    }

    if (showTimerDialog) {
        AlertDialog(
            onDismissRequest = { toggleTimerDialog(false) },
            confirmButton = {},
            dismissButton = {},
            text = {
                DurationSettingScreen { duration ->
//                    viewModel.createTimer(duration = duration)
                    onSave(duration)
                    toggleTimerDialog(false)
                }
            }
        )
    }
//        GridCards()

}

