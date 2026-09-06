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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import free.bigflowertiger.cocassistant.ui.timer.inputpad.DurationInputController
import kotlin.time.Duration

@OptIn(ExperimentalGridApi::class)
@Composable
fun DurationSettingScreen(
    modifier: Modifier = Modifier,
    onSave: (duration: Duration) -> Unit
) {
    val controller by remember { mutableStateOf(DurationInputController()) }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = controller.displayText,
                style = MaterialTheme.typography.headlineLarge
            )
        }
        Grid(
            config = {
                repeat(3) {
                    column(0.33f)
                }
                repeat(6) {
                    row(GridTrackSize.Auto)
                }
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
            TextCard(label = "X") {
                controller.backspace()
            }

            Button(onClick = {}) {
                Text(text = "1分钟")
            }
            Button(onClick = {}) {
                Text(text = "10分钟")
            }
            Button(onClick = {}) {
                Text(text = "30分钟")
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .gridItem(columnSpan = 3),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        onSave(controller.duration)
                    }
                ) {
                    Text(text = "启动计时器")
                }
            }
        }
    }
}

