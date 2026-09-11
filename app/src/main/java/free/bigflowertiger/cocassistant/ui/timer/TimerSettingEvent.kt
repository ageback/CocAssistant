package free.bigflowertiger.cocassistant.ui.timer

sealed class TimerSettingEvent {
    data class ChangeSelectedApp(val app: AlarmAppInfo) : TimerSettingEvent()
    data class CheckResetAfterStart(val autoReset: Boolean): TimerSettingEvent()
}