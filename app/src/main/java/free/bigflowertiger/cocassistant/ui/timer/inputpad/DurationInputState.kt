package free.bigflowertiger.cocassistant.ui.timer.inputpad

import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class DurationInputState(
    initialDuration: Duration = Duration.ZERO,
    val maxHours: Int = 99
) {
    private var digits: Long = durationToDigits(initialDuration)

    val hours: Int get() = (digits / 10_000).toInt()
    val minutes: Int get() = ((digits / 100) % 100).toInt()
    val seconds: Int get() = (digits % 100).toInt()

    val duration: Duration get() = hours.hours + minutes.minutes + seconds.seconds
    val isZero: Boolean get() = digits == 0L
    val displayText: String get() = "%02d:%02d:%02d".format(hours, minutes, seconds)

    /**
     * 输入一个数字
     */
    fun appendDigit(digit: Int): Boolean {
        require(digit in 0..9)
        val newDigits = digits * 10 + digit

        // 最多 6 位
        if (newDigits > 999999) return false
        if (!isValidDigits(newDigits)) return false

        digits = newDigits
        return true
    }

    /**
     * 输入 00
     */
    fun appendDoubleZero(): Boolean {
        val newDigits = digits * 100
        if (newDigits > 999999) return false
        if (!isValidDigits(newDigits)) return false

        digits = newDigits
        return true
    }

    /**
     * 删除最后一个数字
     */
    fun backspace() {
        digits /= 10
    }

    /**
     * 清零
     */
    fun clear() {
        digits = 0
    }

    /**
     * 直接设置 Duration
     */
    fun setDuration(duration: Duration) {
        require(duration.isFinite())
        require(duration > Duration.ZERO)

        val totalSeconds = duration.inWholeSeconds

        val h = totalSeconds / 3600
        val m = (totalSeconds / 60) % 60
        val s = totalSeconds % 60

        require(h <= maxHours)

        digits = h * 10_000 + m * 100 + s
    }

    private fun isValidDigits(value: Long): Boolean {
        val h = value / 10_000
        val m = (value / 100) % 100
        val s = value % 100

        return h <= maxHours && m <= 59 && s <= 59
    }


    private fun durationToDigits(duration: Duration): Long {
        val totalSeconds = duration.inWholeSeconds

        val h = totalSeconds / 3600
        val m = (totalSeconds / 60) % 60
        val s = totalSeconds % 60

        require(h <= maxHours)

        return h * 10_000 + m * 100 + s
    }
}