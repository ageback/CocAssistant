package free.bigflowertiger.cocassistant.ui.timer

sealed class TimerUiEvent {
    data class SetSelectedApp(val app: AlarmAppInfo) : TimerUiEvent()
    data class CheckResetAfterStart(val autoReset: Boolean) : TimerUiEvent()
    data class SetScale(val index: Int) : TimerUiEvent()
    data class SetTimerTitle(val timerTitle: String) : TimerUiEvent()
}