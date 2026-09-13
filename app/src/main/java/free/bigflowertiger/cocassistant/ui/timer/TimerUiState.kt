package free.bigflowertiger.cocassistant.ui.timer

import free.bigflowertiger.cocassistant.ui.timer.inputpad.DurationScales

data class TimerUiState(
    val selectedAppInfo: AlarmAppInfo? = null,
    val resetAfterStart: Boolean = true,
    val alarmApps: List<AlarmAppInfo> = emptyList(),
    val scales: List<DurationScales> = emptyList(),
    val selectedScale: DurationScales = DurationScales(),
    val timerTitle: String = ""
) {
    val detectAlarmApps: Boolean get() = alarmApps.isNotEmpty()
}