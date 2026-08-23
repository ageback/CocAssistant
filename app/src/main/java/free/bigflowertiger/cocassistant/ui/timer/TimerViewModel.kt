package free.bigflowertiger.cocassistant.ui.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import free.bigflowertiger.cocassistant.alarm.AlarmScheduler
import free.bigflowertiger.cocassistant.data.room.entity.TimerEntity
import free.bigflowertiger.cocassistant.domain.TimerRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val repository: TimerRepository,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {
    init {
        createForTest()
    }

    fun createForTest() {
        createTimer("AA", 10000)
        createTimer("BB", 10000)
    }

    val timersFlow: StateFlow<List<TimerEntity>> = repository.observeRunningTimers()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun createTimer(title: String, durationMillis: Long) = viewModelScope.launch {
        repository.createTimer(title, durationMillis)
    }

    fun cancelTimer(timerId: String) = viewModelScope.launch {
        repository.cancelTimer(timerId)
    }

    fun canScheduleExactAlarms(): Boolean = alarmScheduler.canScheduleExactAlarms()
}