package free.bigflowertiger.cocassistant.ui.timer.inputpad

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DurationDisplay(
    hours: Int,
    minutes: Int,
    seconds: Int,
    color: Color = Color.Unspecified
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        DurationNumber(hours, color)

        DurationUnit("小时", color)

        DurationNumber(minutes, color)
        DurationUnit("分钟", color)

        DurationNumber(seconds, color)
        DurationUnit("秒", color)
    }
}

@Composable
private fun DurationNumber(
    value: Int,
    color: Color = Color.Unspecified
) {
    Text(
        text = "%02d".format(value),
        fontSize = 48.sp,
        fontWeight = FontWeight.Bold,
        color = color
    )
}

@Composable
private fun DurationUnit(
    text: String,
    color: Color = Color.Unspecified
) {
    Text(
        text = text,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(
            start = 3.dp,
            end = 8.dp,
            bottom = 5.dp
        ),
        color = color
    )
}