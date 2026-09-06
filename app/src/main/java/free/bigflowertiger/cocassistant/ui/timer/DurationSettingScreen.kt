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

@OptIn(ExperimentalGridApi::class)
@Composable
fun DurationSettingScreen(
    modifier: Modifier = Modifier,
    onSave: (durationMillis: Long) -> Unit
) {
    val controller by remember { mutableStateOf(DurationInputController()) }

    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf(
        DurationMultiples("不加速", 1),
        DurationMultiples("10倍加速", 10),
        DurationMultiples("24倍加速", 24)
    )
    Scaffold { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
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
                    repeat(10) { row(GridTrackSize.Auto) }
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

                Button(onClick = {}) { Text(text = "1分钟") }
                Button(onClick = {}) { Text(text = "10分钟") }
                Button(onClick = {}) { Text(text = "30分钟") }

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
                            onClick = { selectedIndex = index },
                            selected = index == selectedIndex,
                            label = { Text(multiple.title) }
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .gridItem(columnSpan = 3),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            onSave(controller.duration.inWholeMilliseconds / options[selectedIndex].multiples)
                        }
                    ) {
                        Text(text = "启动计时器")
                    }
                }
            }
        }
    }

}
