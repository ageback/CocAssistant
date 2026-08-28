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
import androidx.compose.ui.unit.dp
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalGridApi::class)
@Composable
fun DurationSettingScreen(
    modifier: Modifier = Modifier
) {
    var inputValue by remember { mutableLongStateOf(0L) }
    var duration: Duration by remember { mutableStateOf(0.seconds) }


    fun getDuration(): Duration {
        val hours = inputValue / 10_000
        val minutes = (inputValue / 100) % 100
        val seconds = inputValue % 100
        return hours.hours + minutes.minutes + seconds.seconds
    }

    fun removeDigit() {
        inputValue /= 10
        duration = getDuration()
    }

    fun appendDigit(digit: Int) {
        inputValue = (inputValue * 10 + digit) % 1_000_000
        duration = getDuration()
    }
    Column(modifier = modifier) {
        Row() {
            Text(text = duration.toString())
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
                    appendDigit(number)
                }
            }

            TextCard(label = "00")
            TextCard(label = "0")
            TextCard(label = "X") {
                removeDigit()
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
            Button(onClick = {}) {
                Text(text = "启动计时器")
            }
        }
    }
}