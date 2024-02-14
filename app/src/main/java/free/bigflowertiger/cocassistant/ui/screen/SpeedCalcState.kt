package free.bigflowertiger.cocassistant.ui.screen

data class SpeedCalcState(
    /**
     * 倒计时剩余时间，秒
     */
    val timeRemaining: Long = 0,

    /**
     * 加速倍数
     */
    val speedMultiple: Int = 24,

    /**
     * 输入小时数
     */
    var inputHours: Int = 0,
    /**
     * 输入分钟数
     */
    var inputMinutes: Int = 0
)