package free.bigflowertiger.cocassistant.ui.screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SpeedCalcViewModel @Inject constructor() : ViewModel() {
    private val _state = mutableStateOf(SpeedCalcState())
    val state: State<SpeedCalcState> = _state

    fun getActualResearchMinutes(hours: String, minutes: String) {
        val input1 = hours.toIntOrNull() ?: 0
        val input2 = minutes.toIntOrNull() ?: 0
        _state.value = _state.value.copy(actualResearchMinutes = input1 * 60 / 24 + input2)
    }
}