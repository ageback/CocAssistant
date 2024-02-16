package free.bigflowertiger.cocassistant.ui.screen

import android.media.RingtoneManager
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PauseCircle
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.StopCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = true) {
        viewModel.uiEventFlow.collectLatest { event ->
            when (event) {
                is SpeedCalcViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(message = event.msg)
                }

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
            value = state.inputHours,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            onValueChange = {
                viewModel.onEvent(SpeedCalcEvent.ChangeInputHours(it))
            }
        )
        OutlinedTextField(
            label = {
                Text(text = "研究时间（分钟）")
            },
            value = state.inputMinutes,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            onValueChange = {
                viewModel.onEvent(SpeedCalcEvent.ChangeInputMinutes(it))
            }
        )

        OutlinedTextField(
            label = {
                Text(text = "加速倍数")
            },
            value = state.speedMultiple,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number
            ),
            onValueChange = {
                viewModel.onEvent(SpeedCalcEvent.ChangeSpeedMultiple(it))
            }
        )

        Row {
            IconButton(
                modifier = Modifier.padding(horizontal = 8.dp),
                enabled = state.timerStatus == TimeStatus.Stopped,
                onClick = { viewModel.onEvent(SpeedCalcEvent.StartTimer) }
            ) {
                Icon(imageVector = Icons.Outlined.PlayCircle, contentDescription = "Start")
            }
            IconButton(
                modifier = Modifier.padding(horizontal = 8.dp),
                enabled = state.timerStatus != TimeStatus.Stopped,
                onClick = {
                    if (state.timerStatus != TimeStatus.Paused) {
                        viewModel.onEvent(SpeedCalcEvent.PauseTimer)
                    } else {
                        viewModel.onEvent(SpeedCalcEvent.ResumeTimer)
                    }
                }
            ) {
                val icon = when (state.timerStatus) {
                    TimeStatus.Started -> Icons.Outlined.PauseCircle
                    TimeStatus.Paused -> Icons.Outlined.RestartAlt
                    TimeStatus.Resumed -> Icons.Outlined.PauseCircle
                    TimeStatus.Stopped -> Icons.Outlined.PauseCircle
                }
                Icon(imageVector = icon, contentDescription = "Pause/Resume")
            }
            IconButton(
                modifier = Modifier.padding(horizontal = 8.dp),
                enabled = state.timerStatus != TimeStatus.Stopped,
                onClick = { viewModel.onEvent(SpeedCalcEvent.StopTimer) }
            ) {
                Icon(imageVector = Icons.Outlined.StopCircle, contentDescription = "Stop")
            }
        }

        Text(text = "剩余时间：")
        Text(
            text = "${state.timeRemaining / 60} 分钟",
            style = MaterialTheme.typography.displayMedium,
            fontSize = 24.sp
        )
        Text(
            text = "${state.timeRemaining % 60} 秒",
            style = MaterialTheme.typography.displayMedium,
            fontSize = 24.sp
        )
    }
}
