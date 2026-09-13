package free.bigflowertiger.cocassistant.ui.timer

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.AlarmClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import free.bigflowertiger.cocassistant.data.DatastoreRepository
import free.bigflowertiger.cocassistant.ui.timer.inputpad.DurationInputController
import free.bigflowertiger.cocassistant.ui.timer.inputpad.DurationScales
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.find

@HiltViewModel
class TimerViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    val controller: DurationInputController = DurationInputController()

    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState = _uiState.asStateFlow()

//    private val _durationState = MutableStateFlow(DurationState())
//    val durationState = _durationState.asStateFlow()

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
                        DurationScales("建筑工人10倍速", "10倍加速", 10, true),
                        DurationScales("实验室24倍速", "24倍加速", 24, true)
                    )
                )
            }
        }
    }

    fun onDurationEvent(event: DurationEvent) {
        when (event) {
            is DurationEvent.AppendDigit -> {
                controller.appendDigit(event.digit)
            }

            DurationEvent.AppendDoubleZero -> {
                controller.appendDoubleZero()
            }

            DurationEvent.Backspace -> {
                controller.backspace()
            }

            DurationEvent.Clear -> {
                controller.clear()
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
                controller.changeScale(selectedScale.scale)
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