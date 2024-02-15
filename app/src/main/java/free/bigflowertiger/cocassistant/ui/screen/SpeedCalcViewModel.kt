package free.bigflowertiger.cocassistant.ui.screen

import android.os.CountDownTimer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import free.bigflowertiger.cocassistant.worker.WorkerHelper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpeedCalcViewModel @Inject constructor() : ViewModel() {
    private val _state = mutableStateOf(SpeedCalcState())
    val state: State<SpeedCalcState> = _state
    private var timer: CountDownTimer? = null

    private val _uiEventFlow = MutableSharedFlow<UiEvent>()
    val uiEventFlow = _uiEventFlow.asSharedFlow()

    fun onEvent(event: SpeedCalcEvent) = when (event) {
        is SpeedCalcEvent.StartTimer -> {
            resetTimer()
            timer?.start()
        }

        is SpeedCalcEvent.StopTimer -> {
            resetTimer()
        }

        is SpeedCalcEvent.ResumeTimer -> {
            timer?.start()
        }
        is SpeedCalcEvent.PauseTimer -> {
            timer?.cancel()
        }

        is SpeedCalcEvent.ChangeSpeedMultiple -> {
            onInput(event.multiple) {
                _state.value = _state.value.copy(speedMultiple = it)
            }
        }

        is SpeedCalcEvent.ChangeInputHours -> {
            onInput(event.hours) {
                _state.value = _state.value.copy(inputHours = it)
            }

        }

        is SpeedCalcEvent.ChangeInputMinutes -> {
            onInput(event.minutes) {
                _state.value = _state.value.copy(inputMinutes = it)
            }
        }
    }

    private fun resetTimer() {
        timer?.cancel()
        calcAcceleratedTime()
        timer = object : CountDownTimer(
            state.value.timeRemaining * 1000,
            1000L
        ) {
            override fun onTick(millisUntilFinished: Long) {
                _state.value = _state.value.copy(timeRemaining = millisUntilFinished / 1000)
            }

            override fun onFinish() {
                WorkerHelper.setCountDownWorker()
            }
        }
    }

    private fun onInput(input: String, block: (String) -> Unit) =
        try {
            val intNumber = if (input.isBlank()) "" else input.toInteger()
            block(intNumber.toString())
            calcAcceleratedTime()
        } catch (ex: Exception) {
            viewModelScope.launch {
                _uiEventFlow.emit(UiEvent.ShowSnackbar("请输入一个整数。"))
            }
        }

    private fun String.toInteger(): Int {
        return toIntOrNull() ?: throw Exception("Not an integer")
    }

    private fun calcAcceleratedTime() {
        _state.value =
            _state.value.copy(
                timeRemaining = ((state.value.inputHours.toLongOrNull() ?: 0) * 60L
                        + (state.value.inputMinutes.toLongOrNull() ?: 0)) * 60
                        / (state.value.speedMultiple.toLongOrNull() ?: 1)
            )
    }

    sealed class UiEvent {
        data class ShowSnackbar(val msg: String) : UiEvent()
        data object OnTimerFinish : UiEvent()
    }
}
