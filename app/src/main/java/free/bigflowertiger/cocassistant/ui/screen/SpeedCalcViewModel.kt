package free.bigflowertiger.cocassistant.ui.screen

import android.os.CountDownTimer
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
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
            timer = object : CountDownTimer(
                state.value.timeRemaining * 1000,
                1000L
            ) {
                override fun onTick(millisUntilFinished: Long) {
                    _state.value = _state.value.copy(timeRemaining = millisUntilFinished / 1000)
                }

                override fun onFinish() {
                    viewModelScope.launch {
                        // 在计时器完成时播放系统默认的通知铃声
                        _uiEventFlow.emit(UiEvent.OnTimerFinish)
                    }
                }
            }.start()
        }

        is SpeedCalcEvent.StopTimer -> {
            timer?.cancel()
            _state.value = _state.value.copy(timeRemaining = 0)
        }

        is SpeedCalcEvent.CalcActualResearchMinutes -> {
            getAcceleratedTime(event.hours, event.minutes)
        }
    }

    private fun getAcceleratedTime(hours: Long, minutes: Long) {
        _state.value = _state.value.copy(timeRemaining = (hours * 60 + minutes) * 60 / 24)
    }

    sealed class UiEvent {
        data object OnTimerFinish : UiEvent()
    }
}
