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
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import free.bigflowertiger.cocassistant.R
import free.bigflowertiger.cocassistant.ui.timer.inputpad.DurationDisplay
import free.bigflowertiger.cocassistant.ui.timer.inputpad.DurationInputController
import free.bigflowertiger.cocassistant.ui.timer.inputpad.DurationMultiples
import free.bigflowertiger.cocassistant.ui.timer.inputpad.SmallerDurationDisplay

@OptIn(ExperimentalGridApi::class)
@Composable
fun DurationSettingScreen(
    modifier: Modifier = Modifier,
    onSave: (timerData: TimerData, app: AlarmAppInfo?) -> Unit
) {
    val viewModel = hiltViewModel<TimerSettingViewModel>()
    val state by viewModel.state.collectAsState()
    val controller by remember { mutableStateOf(DurationInputController()) }


    var selectedMultipleIndex by remember { mutableIntStateOf(0) }
    val multiples = listOf(
        DurationMultiples("不加速", "不加速", 1),
        DurationMultiples("建筑工人10倍速", "10倍加速", 10),
        DurationMultiples("实验室24倍速", "24倍加速", 24)
    )


    // 定时器名称
    var timerTitle by remember { mutableStateOf("不加速") }

    val (showAppListDialog, toggleAppListDialog) = remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            if (state.detectAlarmApps) {
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
            if (!state.detectAlarmApps) {
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = "未检测到其他时钟程序，本程序无法独立启动倒计时！",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            state.selectedAppInfo?.let {
                AlarmAppItem(it, true)
            }
            DurationDisplay(
                controller.hours,
                controller.minutes,
                controller.seconds
            )
            SmallerDurationDisplay(
                hours = controller.speedupTime.first,
                minutes = controller.speedupTime.second,
                seconds = controller.speedupTime.third,
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
                    value = timerTitle,
                    onValueChange = {
                        timerTitle = it
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
                        controller.appendDigit(number)
                    }
                }

                TextCard(label = "00") { controller.appendDoubleZero() }
                TextCard(label = "0") { controller.appendDigit(it.toInt()) }
                PainterIconCard(
                    onLongClick = {
                        controller.clear()
                    }
                ) { controller.backspace() }

                // 加速倍数
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .gridItem(columnSpan = 3),
                ) {
                    multiples.forEachIndexed { index, multiple ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = multiples.size
                            ),
                            onClick = {
                                selectedMultipleIndex = index
                                timerTitle = multiples[index].title
                                controller.changeMultiples(multiples[index].multiples)
                            },
                            selected = index == selectedMultipleIndex,
                            label = { Text(multiple.label) }
                        )
                    }
                }

                Row(
                    modifier = Modifier.gridItem(columnSpan = 3),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = state.resetAfterStart,
                        onCheckedChange = {
                            viewModel.onEvent(TimerSettingEvent.CheckResetAfterStart(it))
                        }
                    )
                    Text(
                        text = "启动后清零",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.clickable(onClick = {
                            viewModel.onEvent(TimerSettingEvent.CheckResetAfterStart(!state.resetAfterStart))
                        })
                    )
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .gridItem(columnSpan = 3),
                    onClick = {
                        onSave(
                            TimerData(
                                timerTitle,
                                controller.durationByScale
                            ),
                            state.selectedAppInfo
                        )
                        if (state.resetAfterStart) {
                            controller.clear()
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
                        apps = state.alarmApps,
                        onAppSelected = {
                            toggleAppListDialog(false)
                            viewModel.onEvent(TimerSettingEvent.ChangeSelectedApp(it))
                        }
                    )
                },
                confirmButton = {}
            )
        }
    }
}