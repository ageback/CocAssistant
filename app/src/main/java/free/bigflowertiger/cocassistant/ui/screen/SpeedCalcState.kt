package free.bigflowertiger.cocassistant.ui.screen

data class SpeedCalcState(
    /**
     * 倒计时剩余时间，秒
     */
    val timeRemaining: Long = 0,

    /**
     * 加速倍数
     */
    val speedMultiple: String = "",

    /**
     * 输入小时数
     */
    var inputHours: String = "",
    /**
     * 输入分钟数
     */
    var inputMinutes: String = ""
)