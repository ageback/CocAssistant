package free.bigflowertiger.cocassistant.ui.timer.inputpad

import free.bigflowertiger.cocassistant.ui.timer.toHms
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class DurationState(
    var digits: Long = 0,
    var scale: Int = 1
) {
    val maxHours = 99
    val hours: Int
        get() = (digits / 10_000).toInt()

    val minutes: Int
        get() = ((digits / 100) % 100).toInt()

    val seconds: Int
        get() = (digits % 100).toInt()

    val validTotalSeconds: Long
        get() {
            val h = digits / 10_000
            val m = (digits / 100) % 100
            val s = digits % 100

            val totalSeconds = h * 3600 + m * 60 + s
            val maxSeconds = maxHours * 3600L + 59 * 60 + 59

            return totalSeconds.coerceAtMost(maxSeconds)
        }

    val duration: Duration get() = validTotalSeconds.seconds

    val durationByScale: Long
        get() = duration.inWholeMilliseconds / scale

    val speedupTime: Triple<Int, Int, Int> get() = duration.div(scale).toHms()

}