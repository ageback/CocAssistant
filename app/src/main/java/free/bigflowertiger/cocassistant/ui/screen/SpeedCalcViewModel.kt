package free.bigflowertiger.cocassistant.ui.screen

import android.os.CountDownTimer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import free.bigflowertiger.cocassistant.worker.WorkerHelper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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
            timer = object : CountDownTimer(
                state.value.timeRemaining * 1000,
                1000L
            ) {
                override fun onTick(millisUntilFinished: Long) {
                    _state.value = _state.value.copy(timeRemaining = millisUntilFinished / 1000)
                }

                override fun onFinish() {
//                    viewModelScope.launch {
//                        // 在计时器完成时播放系统默认的通知铃声
//                        _uiEventFlow.emit(UiEvent.OnTimerFinish)
//                    }
                    WorkerHelper.setCountDownWorker()
                }
            }.start()
        }

        is SpeedCalcEvent.StopTimer -> {
            timer?.cancel()
            _state.value = _state.value.copy(timeRemaining = 0)
        }

        is SpeedCalcEvent.ChangeSpeedMultiple -> {
            _state.value = _state.value.copy(speedMultiple = event.multiple)
            calcAcceleratedTime()
        }

        is SpeedCalcEvent.ChangeInputHours -> {
            _state.value = _state.value.copy(inputHours = event.hours)
            calcAcceleratedTime()
        }

        is SpeedCalcEvent.ChangeInputMinutes -> {
            _state.value = _state.value.copy(inputMinutes = event.minutes)
            calcAcceleratedTime()
        }

    }

    private fun calcAcceleratedTime() {
        _state.value =
            _state.value.copy(
                timeRemaining = (state.value.inputHours * 60L + state.value.inputMinutes) * 60 / state.value.speedMultiple
            )
    }

    sealed class UiEvent {
        data object OnTimerFinish : UiEvent()
    }
}
