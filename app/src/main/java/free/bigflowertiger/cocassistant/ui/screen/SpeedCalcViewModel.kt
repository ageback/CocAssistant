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

    fun getActualResearchMinutes(input: String) {
        val input1 = input.toIntOrNull()
        _state.value = _state.value.copy(actualResearchMinutes = input1?.let { it / 24 } ?: 0)
    }
}