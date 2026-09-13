package free.bigflowertiger.cocassistant.ui.timer.inputpad

import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import javax.inject.Singleton
import kotlin.time.Duration

@Stable
@Singleton
class DurationInputController(
    initialDuration: Duration = Duration.ZERO,
    private val maxHours: Int = 99
) {
    private val _state = mutableStateOf(
        DurationState(digits = durationToDigits(initialDuration))
    )
    val state: State<DurationState> = _state

    fun appendDigit(digit: Int) {
        require(digit in 0..9)

        val newValue = _state.value.digits * 10 + digit

        if (newValue <= 999999) {
            _state.value = _state.value.copy(digits = newValue)
        }
    }


    fun changeScale(scale: Int) {
        _state.value = _state.value.copy(scale = scale)
    }

    fun appendDoubleZero() {
        val newValue = _state.value.digits * 100

        if (newValue <= 999999) {
            _state.value = _state.value.copy(digits = newValue)
        }
    }

    fun backspace() {
        _state.value = _state.value.copy(
            digits = _state.value.digits / 10
        )
    }

    fun clear() {
        _state.value = _state.value.copy(digits = 0)
    }

    fun setDuration(duration: Duration) {
        _state.value = _state.value.copy(
            digits = durationToDigits(duration)
        )
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

fun Duration.toHms(): Triple<Int, Int, Int> {
    return this.toComponents { hours, minutes, seconds, _ ->
        Triple(hours.toInt(), minutes, seconds)
    }
}
