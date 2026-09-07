package free.bigflowertiger.cocassistant.ui.timer

data class TimerData(
    val timerNamer: String,
    val durationMillis: Long
) {
    val durationSeconds get() = durationMillis / 1000
}