package free.bigflowertiger.cocassistant.ui.timer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.GridTrackSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import free.bigflowertiger.cocassistant.R
import free.bigflowertiger.cocassistant.ui.timer.TimerUiEvent.CheckResetAfterStart
import free.bigflowertiger.cocassistant.ui.timer.inputpad.DurationDisplay
import free.bigflowertiger.cocassistant.ui.timer.inputpad.SmallerDurationDisplay

@OptIn(ExperimentalGridApi::class)
@Composable
fun TimerScreen(
    modifier: Modifier = Modifier,
    onSave: (timerData: TimerData, app: AlarmAppInfo?) -> Unit
) {
    val viewModel = hiltViewModel<TimerViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val durationState by viewModel.durationState

    var selectedScaleIndex by remember { mutableIntStateOf(0) }


    val (showAppListDialog, toggleAppListDialog) = remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            if (uiState.detectAlarmApps) {
                FloatingActionButton(
                    onClick = {
                        toggleAppListDialog(true)
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_stat_timer),
                        contentDescription = "选择闹钟程序"
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            if (!uiState.detectAlarmApps) {
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = "未检测到其他时钟程序，本程序无法独立启动倒计时！",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            uiState.selectedAppInfo?.let {
                AlarmAppItem(it, true)
            }
            DurationDisplay(
                durationState.hours,
                durationState.minutes,
                durationState.seconds
            )

            SmallerDurationDisplay(
                hours = durationState.speedupTime.first,
                minutes = durationState.speedupTime.second,
                seconds = durationState.speedupTime.third,
                color = MaterialTheme.colorScheme.error
            )

            Grid(
                config = {
                    repeat(3) { column(0.33f) }
                    repeat(10) { row(GridTrackSize.Auto) }
                    gap(8.dp)
                }
            ) {
                OutlinedTextField(
                    value = uiState.timerTitle,
                    onValueChange = {
                        viewModel.onUiEvent(TimerUiEvent.SetTimerTitle(it))
                    },
                    label = {
                        Text("计时器名称")
                    },
                    modifier = Modifier
                        .gridItem(columnSpan = 3)
                        .fillMaxWidth(),
                )
                repeat(9) { index ->
                    val number = index + 1
                    NumberCard(number = number) {
                        viewModel.onDurationEvent(DurationEvent.AppendDigit(number))
                    }
                }

                TextCard(label = "00") { viewModel.onDurationEvent(DurationEvent.AppendDoubleZero) }
                TextCard(label = "0") {
                    viewModel.onDurationEvent(DurationEvent.AppendDigit(it.toInt()))
                }
                PainterIconCard(
                    onLongClick = {
                        viewModel.onDurationEvent(DurationEvent.Clear)
                    }
                ) {
                    viewModel.onDurationEvent(DurationEvent.Backspace)
                }

                // 加速倍数
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .gridItem(columnSpan = 3),
                ) {
                    uiState.scales.forEachIndexed { index, scale ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = uiState.scales.size
                            ),
                            onClick = {
                                selectedScaleIndex = index
                                viewModel.onUiEvent(TimerUiEvent.SetScale(index))
                            },
                            selected = index == selectedScaleIndex,
                            label = { Text(scale.label) }
                        )
                    }
                }

                Row(
                    modifier = Modifier.gridItem(columnSpan = 3),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = uiState.resetAfterStart,
                        onCheckedChange = {
                            viewModel.onUiEvent(CheckResetAfterStart(it))
                        }
                    )
                    Text(
                        text = "启动后清零",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.clickable(
                            onClick = {
                                viewModel.onUiEvent(
                                    CheckResetAfterStart(autoReset = !uiState.resetAfterStart)
                                )
                            }
                        )
                    )
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .gridItem(columnSpan = 3),
                    onClick = {
                        onSave(
                            TimerData(
                                uiState.timerTitle,
                                durationState.durationByScale
                            ),
                            uiState.selectedAppInfo
                        )
                        if (uiState.resetAfterStart) {
                            viewModel.onDurationEvent(DurationEvent.Clear)
                        }
                    }
                ) {
                    Text(text = "启动计时器")
                }
            }

        }


        if (showAppListDialog) {
            AlertDialog(
                onDismissRequest = { toggleAppListDialog(false) },
                text = {
                    AlarmAppList(
                        selectedPackageName = "",
                        apps = uiState.alarmApps,
                        onAppSelected = {
                            toggleAppListDialog(false)
                            viewModel.onUiEvent(TimerUiEvent.SetSelectedApp(it))
                        }
                    )
                },
                confirmButton = {}
            )
        }
    }
}