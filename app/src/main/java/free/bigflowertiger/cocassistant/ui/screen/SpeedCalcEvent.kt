package free.bigflowertiger.cocassistant.ui.screen

sealed class SpeedCalcEvent {
    data object StartTimer : SpeedCalcEvent()
    data object StopTimer : SpeedCalcEvent()

    data class ChangeInputHours(val hours: Int) : SpeedCalcEvent()

    data class ChangeInputMinutes(val minutes: Int) : SpeedCalcEvent()

    /**
     * 加速倍数
     */
    data class ChangeSpeedMultiple(val multiple: Int) : SpeedCalcEvent()
}