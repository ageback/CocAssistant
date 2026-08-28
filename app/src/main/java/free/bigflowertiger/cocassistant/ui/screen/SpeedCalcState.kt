package free.bigflowertiger.cocassistant.ui.screen

import free.bigflowertiger.cocassistant.R


data class SpeedCalcState(
    val timerStatus: TimeStatus = TimeStatus.Stopped,
    /**
     * 倒计时剩余时间，秒
     */
    val timeRemaining: Long = 0,

    /**
     * 加速倍数
     */
    val speedMultiple: String = "24",

    /**
     * 输入小时数
     */
    var inputHours: String = "",
    /**
     * 输入分钟数
     */
    var inputMinutes: String = ""
) {
    val startEnabled get() = timeRemaining > 0 && timerStatus == TimeStatus.Stopped
    val pauseResumeEnabled get() = timerStatus != TimeStatus.Stopped
    val stopEnabled get() = timerStatus != TimeStatus.Stopped

    val inputEnabled get() = timerStatus == TimeStatus.Stopped

    val pauseResumeIcon
        get() = when (timerStatus) {
            TimeStatus.Started -> R.drawable.pause_circle_24px
            TimeStatus.Paused -> R.drawable.resume_24px
            TimeStatus.Resumed -> R.drawable.pause_circle_24px
            TimeStatus.Stopped -> R.drawable.pause_circle_24px
        }
}