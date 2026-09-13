package free.bigflowertiger.cocassistant.ui.timer

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.AlarmClock
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import free.bigflowertiger.cocassistant.data.DatastoreRepository
import free.bigflowertiger.cocassistant.ui.timer.inputpad.DurationScales
import free.bigflowertiger.cocassistant.ui.timer.inputpad.DurationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.find
import kotlin.time.Duration

@HiltViewModel
class TimerViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _durationState = mutableStateOf(DurationState())
    val durationState: State<DurationState> = _durationState

    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState = _uiState.asStateFlow()

    init {
        effect {
            val selectedAppId = DatastoreRepository.selectedAlarmAppId.get()
            val resetAfterStart = DatastoreRepository.resetAfterStart.getOrDefault()
            val appList = queryAlarmApps()
            val selectedApp = selectedAppId?.let {
                appList.find { app -> app.packageName == it }
            }
            _uiState.update { stt ->
                stt.copy(
                    selectedAppInfo = selectedApp,
                    resetAfterStart = resetAfterStart,
                    alarmApps = appList,
                    scales = listOf(
                        DurationScales("不加速", "不加速", 1),
                        DurationScales("建筑工人10倍速", "10倍加速", 10),
                        DurationScales("实验室24倍速", "24倍加速", 24)
                    )
                )
            }
        }
    }

    fun onDurationEvent(event: DurationEvent) {
        when (event) {
            is DurationEvent.AppendDigit -> {
                appendDigit(event.digit)
            }

            DurationEvent.AppendDoubleZero -> {
                appendDoubleZero()
            }

            DurationEvent.Backspace -> {
                backspace()
            }

            DurationEvent.Clear -> {
                clear()
            }
        }

    }

    fun onUiEvent(event: TimerUiEvent) {
        when (event) {
            is TimerUiEvent.SetSelectedApp -> {
                _uiState.update { it.copy(selectedAppInfo = event.app) }
                effect {
                    DatastoreRepository.selectedAlarmAppId.set(event.app.packageName)
                }
            }

            is TimerUiEvent.CheckResetAfterStart -> {
                _uiState.update { it.copy(resetAfterStart = event.autoReset) }
                effect {
                    DatastoreRepository.resetAfterStart.set(event.autoReset)
                }
            }

            is TimerUiEvent.SetScale -> {
                val selectedScale = uiState.value.scales[event.index]
                changeScale(selectedScale.scale)
                _uiState.update {
                    it.copy(
                        selectedScale = selectedScale,
                        timerTitle = selectedScale.title
                    )
                }
            }

            is TimerUiEvent.SetTimerTitle -> TODO()
        }
    }

    fun appendDigit(digit: Int) {
        require(digit in 0..9)

        val newValue = _durationState.value.digits * 10 + digit

        if (newValue <= 999999) {
            _durationState.value = _durationState.value.copy(digits = newValue)
        }
    }


    fun changeScale(scale: Int) {
        _durationState.value = _durationState.value.copy(scale = scale)
    }

    fun appendDoubleZero() {
        val newValue = _durationState.value.digits * 100

        if (newValue <= 999999) {
            _durationState.value = _durationState.value.copy(digits = newValue)
        }
    }

    fun backspace() {
        _durationState.value = _durationState.value.copy(
            digits = _durationState.value.digits / 10
        )
    }

    fun clear() {
        _durationState.value = _durationState.value.copy(digits = 0)
    }

    fun queryAlarmApps(): List<AlarmAppInfo> {
        val packageManager = context.packageManager
        val intent = Intent(AlarmClock.ACTION_SET_TIMER)

        return packageManager
            .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            .map {
                val appInfo = it.activityInfo.applicationInfo
                AlarmAppInfo(
                    packageName = appInfo.packageName,
                    name = appInfo.loadLabel(packageManager).toString(),
                    icon = appInfo.loadIcon(packageManager)
                )
            }
    }

    fun queryAlarmAppNames(): List<Pair<String, String>> {
        val intent = Intent(AlarmClock.ACTION_SET_TIMER)
        val apps = context.packageManager.queryIntentActivities(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        )
        return apps.map {
            val packageName = it.activityInfo.packageName
            val appName = it.loadLabel(context.packageManager).toString()

            appName to packageName
        }
    }
}

fun ViewModel.effect(block: suspend () -> Unit) = viewModelScope
    .launch { block() }

fun Duration.toHms(): Triple<Int, Int, Int> {
    return this.toComponents { hours, minutes, seconds, _ ->
        Triple(hours.toInt(), minutes, seconds)
    }
}
