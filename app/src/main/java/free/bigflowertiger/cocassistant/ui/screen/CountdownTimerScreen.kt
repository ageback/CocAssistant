package free.bigflowertiger.cocassistant.ui.screen

import android.media.RingtoneManager
import android.net.Uri
import android.os.CountDownTimer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun CountdownTimerScreen() {
    var timeRemaining by remember { mutableLongStateOf(10 * 1000L) }
    var timer by remember { mutableStateOf<CountDownTimer?>(null) }

    // 获取系统默认的通知铃声的 Uri
    val ringtoneUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val context = LocalContext.current
    // 创建 Ringtone 对象
    val ringtone = remember { RingtoneManager.getRingtone(context, ringtoneUri) }

    timer = object : CountDownTimer(timeRemaining, 1000L) {
        override fun onTick(millisUntilFinished: Long) {
            timeRemaining = millisUntilFinished / 1000
        }

        override fun onFinish() {
            // 在计时器完成时播放系统默认的通知铃声
            ringtone.play()
        }
    }
    LaunchedEffect(key1 = Unit, block = {
        timer?.start()
    })
    DisposableEffect(timer) {
        onDispose {
            timer?.cancel()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Time Remaining: $timeRemaining seconds",
                style = MaterialTheme.typography.displayMedium,
                fontSize = 24.sp
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CountdownTimerScreenPreview() {
    CountdownTimerScreen()
}
