package free.bigflowertiger.cocassistant.ui.timer

import free.bigflowertiger.cocassistant.ui.timer.inputpad.DurationMultiples

data class TimerSettingState(
    val selectedAppInfo: AlarmAppInfo? = null,
    val alarmApps: List<AlarmAppInfo> = emptyList(),
    val multiples: List<DurationMultiples> = emptyList(),
)