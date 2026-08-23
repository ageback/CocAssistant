package free.bigflowertiger.cocassistant.ui.timer

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import free.bigflowertiger.cocassistant.data.room.entity.TimerEntity
import free.bigflowertiger.cocassistant.data.room.entity.TimerStatus
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun TimerItem(
    timer: TimerEntity
) {

    var now by remember {
        mutableLongStateOf(
            System.currentTimeMillis()
        )
    }

    LaunchedEffect(Unit) {

        while (true) {

            now = System.currentTimeMillis()

            delay(1000.milliseconds)
        }
    }

    val remaining =
        (timer.endTime - now)
            .coerceAtLeast(0)

    val seconds =
        remaining / 1000

    val minutes =
        seconds / 60

    val second =
        seconds % 60

    Text(
        text = "%02d:%02d".format(
            minutes,
            second
        )
    )
}

@Composable
@Preview
fun TimerItemPreview() {
    TimerItem(
        timer = TimerEntity(
            id = "1",
            title = "123",
            startTime = System.currentTimeMillis(),
            endTime = System.currentTimeMillis(),
            status = TimerStatus.RUNNING
        )
    )
}