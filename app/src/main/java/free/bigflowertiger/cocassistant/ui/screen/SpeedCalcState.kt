package free.bigflowertiger.cocassistant.ui.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PauseCircle
import androidx.compose.material.icons.outlined.RestartAlt

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
            TimeStatus.Started -> Icons.Outlined.PauseCircle
            TimeStatus.Paused -> Icons.Outlined.RestartAlt
            TimeStatus.Resumed -> Icons.Outlined.PauseCircle
            TimeStatus.Stopped -> Icons.Outlined.PauseCircle
        }
}