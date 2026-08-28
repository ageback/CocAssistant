package free.bigflowertiger.cocassistant.ui.timer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.GridTrackSize
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalGridApi::class)
@Composable
fun DurationSettingScreen(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row() {
            Text(text = "5小时45分27秒")
        }
        Grid(
            config = {
                repeat(3) {
                    column(0.33f)
                }
                repeat(5) {
                    row(GridTrackSize.Auto)
                }
                gap(8.dp)
            }
        ) {
            repeat(12) { index ->
                TextCard(
                    label = "${index + 1}",
                    modifier = modifier,
                )
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