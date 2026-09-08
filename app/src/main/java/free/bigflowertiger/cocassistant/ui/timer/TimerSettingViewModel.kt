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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.find

@HiltViewModel
class TimerSettingViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _state = MutableStateFlow(TimerSettingState())
    val state = _state.asStateFlow()

    init {
        effect {
            val selectedAppId = DatastoreRepository.selectedAlarmAppId.get()
            val appList = queryAlarmApps()
            selectedAppId?.let { appId ->
                _state.update { stt ->
                    stt.copy(
                        selectedAppInfo = appList.find { app -> app.packageName == appId },
                        alarmApps = appList
                    )
                }
            }
        }
    }

    fun onEvent(event: TimerSettingEvent) {
        when (event) {
            is TimerSettingEvent.ChangeSelectedApp -> {
                _state.update { it.copy(selectedAppInfo = event.app) }
                effect {
                    DatastoreRepository.selectedAlarmAppId.set(event.app.packageName)
                }
            }
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