package free.bigflowertiger.cocassistant.ui.timer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.GridTrackSize
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import free.bigflowertiger.cocassistant.ui.timer.inputpad.DurationInputController
import kotlin.math.sign
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalGridApi::class)
@Composable
fun DurationSettingScreen(
    modifier: Modifier = Modifier,
    onSave: (duration: Duration) -> Unit
) {
    val controller by remember { mutableStateOf(DurationInputController()) }
//    var inputValue by remember { mutableStateOf("") }
//    var duration: Duration by remember { mutableStateOf(0.seconds) }
    val context = LocalContext.current

    Column(modifier = modifier) {
        Row() {
            Text(text = controller.displayText)
        }
        Grid(
            config = {
                repeat(3) {
                    column(0.33f)
                }
                repeat(4) {
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

            TextCard(label = "00") { controller.appendDigit(it.toInt()) }
            TextCard(label = "0") { controller.appendDigit(it.toInt()) }
            TextCard(label = "X") {
                controller.backspace()
            }
        }
        Row {
            Button(onClick = {}) {
                Text(text = "1分钟")
            }
            Button(onClick = {}) {
                Text(text = "10分钟")
            }
            Button(onClick = {}) {
                Text(text = "30分钟")
            }
        }
        Row {
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

