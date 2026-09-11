package free.bigflowertiger.cocassistant.ui.timer.inputpad

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Stable
class DurationInputController(
    initialDuration: Duration = Duration.ZERO,
    private val maxHours: Int = 99
) {
    private var digits by mutableLongStateOf(
        durationToDigits(initialDuration)
    )

    /**
     * 加速倍数
     */
    private var scale by mutableIntStateOf(1)

    val hours: Int
        get() = (digits / 10_000).toInt()

    val minutes: Int
        get() = ((digits / 100) % 100).toInt()

    val seconds: Int
        get() = (digits % 100).toInt()

    val speedupTime: Triple<Int, Int, Int> get() = duration.div(scale).toHms()

    private val duration: Duration get() = getValidTotalSeconds().seconds


    val durationByScale: Long
        get() {
            digits = durationToDigits(duration)
            return duration.inWholeMilliseconds / scale
        }


    fun appendDigit(digit: Int) {
        require(digit in 0..9)

        val newValue = digits * 10 + digit

        if (newValue <= 999999) {
            digits = newValue
        }
    }

    fun getValidTotalSeconds(): Long {
        val h = digits / 10_000
        val m = (digits / 100) % 100
        val s = digits % 100

        val totalSeconds = h * 3600 + m * 60 + s
        val maxSeconds = maxHours * 3600L + 59 * 60 + 59

        return totalSeconds.coerceAtMost(maxSeconds)
    }

    fun changeMultiples(multiples: Int) {
        this.scale = multiples
    }

    fun appendDoubleZero() {
        val newValue = digits * 100

        if (newValue <= 999999) {
            digits = newValue
        }
    }

    fun backspace() {
        digits /= 10
    }

    fun clear() {
        digits = 0
    }

    fun setDuration(duration: Duration) {
        digits = durationToDigits(duration)
    }

    private fun durationToDigits(duration: Duration): Long {
        val totalSeconds = duration.inWholeSeconds
        val h = totalSeconds / 3600
        val m = (totalSeconds / 60) % 60
        val s = totalSeconds % 60

        require(h <= maxHours)

        return h * 10_000 + m * 100 + s
    }

    private fun Duration.toHms(): Triple<Int, Int, Int> {
        return this.toComponents { hours, minutes, seconds, _ ->
            Triple(hours.toInt(), minutes, seconds)
        }
    }

}