package free.bigflowertiger.cocassistant.ui.timer

import free.bigflowertiger.cocassistant.ui.timer.inputpad.DurationScales

data class TimerSettingState(
    val selectedAppInfo: AlarmAppInfo? = null,
    val resetAfterStart: Boolean = true,
    val alarmApps: List<AlarmAppInfo> = emptyList(),
    val scales: List<DurationScales> = emptyList(),
    val selectedScale: DurationScales = DurationScales()
) {
    val detectAlarmApps: Boolean get() = alarmApps.isNotEmpty()
}