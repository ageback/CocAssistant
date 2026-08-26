package free.bigflowertiger.cocassistant.ui.timer

import android.content.Intent
import android.os.Build
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kez.picker.PickerDefaults
import com.kez.picker.duration.DurationPicker
import com.kez.picker.duration.rememberDurationPickerState
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

@Composable
fun TimerScreen() {
    val context = LocalContext.current
    val viewModel = hiltViewModel<TimerViewModel>()
    val timers by viewModel.timersFlow.collectAsState()
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

    Scaffold() { paddingValues ->
        val items = remember {
            PickerDefaults.durationPickerItems(
//                hourItems = (0..10).toList(),
//                minuteItems = (0..50 step 1).toList(),
                minDuration = 0.minutes,
                maxDuration = 24.hours
            )
        }
        val state = rememberDurationPickerState(
            items = items,
            initialDuration = 45.minutes
        )
        DurationPicker(
            state = state,
            items = items,
            onSelectedDurationChange = { duration ->
                // The repaired value is already committed to state.selectedDuration.
            }
        )
//        LazyColumn(modifier = Modifier.padding(paddingValues)) {
//            items(items = timers, key = { it.id }) { timer ->
//                TimerItem(timer)
//            }
//        }

    }
}