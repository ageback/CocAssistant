package free.bigflowertiger.cocassistant.ui.timer.inputpad

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DurationDisplay(
    hours: Int,
    minutes: Int,
    seconds: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        DurationNumber(hours)
        DurationUnit("小时")

        DurationNumber(minutes)
        DurationUnit("分钟")

        DurationNumber(seconds)
        DurationUnit("秒")
    }
}

@Composable
private fun DurationNumber(value: Int) {
    Text(
        text = "%02d".format(value),
        fontSize = 48.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun DurationUnit(text: String) {
    Text(
        text = text,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(
            start = 3.dp,
            end = 8.dp,
            bottom = 5.dp
        )
    )
}