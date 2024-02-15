package free.bigflowertiger.cocassistant.ui.screen

sealed class SpeedCalcEvent {
    data object StartTimer : SpeedCalcEvent()
    data object PauseTimer : SpeedCalcEvent()
    data object ResumeTimer : SpeedCalcEvent()
    data object StopTimer : SpeedCalcEvent()

    data class ChangeInputHours(val hours: String) : SpeedCalcEvent()

    data class ChangeInputMinutes(val minutes: String) : SpeedCalcEvent()

    /**
     * 加速倍数
     */
    data class ChangeSpeedMultiple(val multiple: String) : SpeedCalcEvent()
}