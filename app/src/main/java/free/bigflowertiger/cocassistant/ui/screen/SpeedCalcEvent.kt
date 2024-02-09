package free.bigflowertiger.cocassistant.ui.screen

sealed class SpeedCalcEvent {
    data object StartTimer : SpeedCalcEvent()
    data object StopTimer : SpeedCalcEvent()

    /**
     * 计算实际研究速度（加速24倍）
     */
    data class CalcActualResearchMinutes(val hours: Long, val minutes: Long) : SpeedCalcEvent()
}