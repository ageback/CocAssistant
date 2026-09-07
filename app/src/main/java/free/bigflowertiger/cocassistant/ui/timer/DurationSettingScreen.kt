package free.bigflowertiger.cocassistant.ui.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.GridTrackSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import free.bigflowertiger.cocassistant.ui.timer.inputpad.DurationDisplay
import free.bigflowertiger.cocassistant.ui.timer.inputpad.DurationInputController
import free.bigflowertiger.cocassistant.ui.timer.inputpad.DurationMultiples
import free.bigflowertiger.cocassistant.ui.timer.inputpad.TimerPreset
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalGridApi::class)
@Composable
fun DurationSettingScreen(
    modifier: Modifier = Modifier,
    onSave: (timerData: TimerData) -> Unit
) {
    val controller by remember { mutableStateOf(DurationInputController()) }

    var selectedMultipleIndex by remember { mutableIntStateOf(0) }
    var selectedPresetIndex by remember { mutableIntStateOf(0) }
    val options = listOf(
        DurationMultiples("常规", "不加速", 1),
        DurationMultiples("建筑工人10倍速", "10倍加速", 10),
        DurationMultiples("实验室24倍速", "24倍加速", 24)
    )

    val presets = listOf(
        TimerPreset("1分钟", 60),
        TimerPreset("10分钟", 60 * 10),
        TimerPreset("30分钟", 60 * 30)
    )

    var nameValue by remember { mutableStateOf("") }

    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                DurationDisplay(
                    controller.hours,
                    controller.minutes,
                    controller.seconds
                )
            }
            Grid(
                config = {
                    repeat(3) { column(0.33f) }
                    repeat(11) { row(GridTrackSize.Auto) }
                    gap(8.dp)
                }
            ) {
                repeat(9) { index ->
                    val number = index + 1
                    NumberCard(number = number) {
                        controller.appendDigit(number)
                    }
                }

                TextCard(label = "00") { controller.appendDoubleZero() }
                TextCard(label = "0") { controller.appendDigit(it.toInt()) }
                PainterIconCard { controller.backspace() }


                // 预设计时器
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .gridItem(columnSpan = 3),
                ) {
                    presets.forEachIndexed { index, timer ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = presets.size
                            ),
                            onClick = {
                                selectedPresetIndex = index
                                controller.setDuration(timer.durationSeconds.seconds)
                            },

                            selected = index == selectedPresetIndex,
                            label = { Text(timer.title) }
                        )
                    }
                }

                // 加速倍数
                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .gridItem(columnSpan = 3),
                ) {
                    options.forEachIndexed { index, multiple ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = options.size
                            ),
                            onClick = {
                                selectedMultipleIndex = index
                                nameValue = options[index].title
                            },
                            selected = index == selectedMultipleIndex,
                            label = { Text(multiple.label) }
                        )
                    }
                }
                OutlinedTextField(
                    value = nameValue,
                    onValueChange = {
                        nameValue = it
                    },
                    placeholder = {
                        Text(text = "计时器名称")
                    },
                    modifier = Modifier
                        .gridItem(columnSpan = 3)
                        .fillMaxWidth(),
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .gridItem(columnSpan = 3),
                    onClick = {
                        onSave(
                            TimerData(
                                nameValue,
                                controller.duration.inWholeMilliseconds /
                                        options[selectedMultipleIndex].multiples
                            )
                        )
                    }
                ) {
                    Text(text = "启动计时器")
                }
            }

            AlarmAppList("")
        }
    }

}