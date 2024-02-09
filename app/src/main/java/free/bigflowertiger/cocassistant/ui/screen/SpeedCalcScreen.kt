package free.bigflowertiger.cocassistant.ui.screen

import android.media.RingtoneManager
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SpeedCalcScreen(
    viewModel: SpeedCalcViewModel = hiltViewModel()
) {
    // 获取系统默认的通知铃声的 Uri
    val ringtoneUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val context = LocalContext.current
    // 创建 Ringtone 对象
    val ringtone = remember { RingtoneManager.getRingtone(context, ringtoneUri) }

    val state = viewModel.state.value
    var inputHour by remember { mutableStateOf("") }
    var inputMinute by remember { mutableStateOf("") }

    LaunchedEffect(key1 = true) {
        viewModel.uiEventFlow.collectLatest { event ->
            when (event) {
                is SpeedCalcViewModel.UiEvent.OnTimerFinish -> {
                    ringtone.play()
                }
            }

        }
    }
    Column(modifier = Modifier.padding(4.dp)) {
        OutlinedTextField(
            label = {
                Text(text = "研究时间（小时）")
            },
            value = inputHour,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            onValueChange = {
                inputHour = it
                viewModel.onEvent(
                    SpeedCalcEvent.CalcActualResearchMinutes(
                        inputHour.toLongOrNull() ?: 0,
                        inputMinute.toLongOrNull() ?: 0
                    )
                )
            }
        )
        OutlinedTextField(
            label = {
                Text(text = "研究时间（分钟）")
            },
            value = inputMinute,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            onValueChange = {
                inputMinute = it
                viewModel.onEvent(
                    SpeedCalcEvent.CalcActualResearchMinutes(
                        inputHour.toLongOrNull() ?: 0,
                        inputMinute.toLongOrNull() ?: 0
                    )
                )
            }
        )

        Row {
            Button(
                modifier = Modifier.padding(horizontal = 8.dp),
                onClick = { viewModel.onEvent(SpeedCalcEvent.StartTimer) }) {
                Text(text = "Start Count")
            }
            Button(onClick = { viewModel.onEvent(SpeedCalcEvent.StopTimer) }) {
                Text(text = "Stop Count")
            }
        }


        Text(
            text = "Time Remaining: ${state.timeRemaining} seconds",
            style = MaterialTheme.typography.displayMedium,
            fontSize = 24.sp
        )
    }
}
