package free.bigflowertiger.cocassistant.ui.timer

sealed class DurationEvent {
    data class AppendDigit(val digit: Int) : DurationEvent()
    data object AppendDoubleZero : DurationEvent()
    data object Clear : DurationEvent()
    data object Backspace: DurationEvent()
}