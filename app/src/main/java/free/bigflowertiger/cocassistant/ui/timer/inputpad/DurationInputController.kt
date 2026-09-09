package free.bigflowertiger.cocassistant.ui.timer.inputpad

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Stable
class DurationInputController(
    initialDuration: Duration = Duration.ZERO,
    private val maxHours: Int = 99
) {
    private var digits by mutableLongStateOf(
        durationToDigits(initialDuration)
    )
    private var multiples by mutableIntStateOf(1)

    val hours: Int
        get() = (digits / 10_000).toInt()

    val minutes: Int
        get() = ((digits / 100) % 100).toInt()

    val seconds: Int
        get() = (digits % 100).toInt()


    private val speedupDigits get() = digits / multiples
    val speedupHours: Int
        get() = (speedupDigits / 10_000).toInt()

    val speedupMinutes: Int
        get() = ((speedupDigits / 100) % 100).toInt()

    val speedupSeconds: Int
        get() = (speedupDigits % 100).toInt()

    val duration: Duration
        get() = hours.hours + minutes.minutes + seconds.seconds

    val isZero: Boolean get() = digits == 0L

    val displayText: String get() = "%02d小时%02d分%02d秒".format(hours, minutes, seconds)

    fun appendDigit(digit: Int) {
        require(digit in 0..9)

        val newValue = digits * 10 + digit

        if (newValue <= 999999 && isValid(newValue)) {
            digits = newValue
        }
    }

    fun changeMultiples(multiples: Int) {
        this.multiples = multiples
    }

    fun appendDoubleZero() {
        val newValue = digits * 100

        if (newValue <= 999999 &&
            isValid(newValue)
        ) {
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

    private fun isValid(value: Long): Boolean {
        val h = value / 10_000
        val m = (value / 100) % 100
        val s = value % 100

        return h <= maxHours &&
                m <= 59 &&
                s <= 59
    }

    private fun durationToDigits(duration: Duration): Long {
        val totalSeconds = duration.inWholeSeconds

        val h = totalSeconds / 3600
        val m = (totalSeconds / 60) % 60
        val s = totalSeconds % 60

        require(h <= maxHours)

        return h * 10_000 + m * 100 + s
    }

    fun getDurationByMultiple(multiple: Int): Long = duration.inWholeMilliseconds / multiple
}