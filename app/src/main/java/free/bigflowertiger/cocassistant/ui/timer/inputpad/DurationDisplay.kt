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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun SmallerDurationDisplay(
    hours: Int,
    minutes: Int,
    seconds: Int,
    color: Color = Color.Unspecified,
    durationFontSize: TextUnit = 36.sp,
    unitFontSize: TextUnit = 18.sp
) {
    DurationDisplay(
        hours = hours,
        minutes = minutes,
        seconds = seconds,
        color = color,
        durationFontSize = durationFontSize,
        unitFontSize = unitFontSize
    )
}

@Composable
fun DurationDisplay(
    hours: Int,
    minutes: Int,
    seconds: Int,
    color: Color = Color.Unspecified,
    durationFontSize: TextUnit = 48.sp,
    unitFontSize: TextUnit = 24.sp
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        DurationNumber(hours, color, durationFontSize)

        DurationUnit("小时", color, unitFontSize)

        DurationNumber(minutes, color, durationFontSize)
        DurationUnit("分钟", color, unitFontSize)

        DurationNumber(seconds, color, durationFontSize)
        DurationUnit("秒", color, unitFontSize)
    }
}

@Composable
private fun DurationNumber(
    value: Int,
    color: Color = Color.Unspecified,
    durationFontSize: TextUnit
) {
    Text(
        text = "%02d".format(value),
        fontSize = durationFontSize,
        fontWeight = FontWeight.Bold,
        color = color
    )
}

@Composable
private fun DurationUnit(
    text: String,
    color: Color = Color.Unspecified,
    unitFontSize: TextUnit
) {
    Text(
        text = text,
        fontSize = unitFontSize,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(
            start = 3.dp,
            end = 8.dp,
            bottom = 5.dp
        ),
        color = color
    )
}