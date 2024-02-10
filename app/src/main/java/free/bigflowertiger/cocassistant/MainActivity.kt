package free.bigflowertiger.cocassistant

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import free.bigflowertiger.cocassistant.ui.screen.SpeedCalcScreen
import free.bigflowertiger.cocassistant.ui.theme.CocAssistantTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CocAssistantTheme {

                val locationPermissionState =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        rememberMultiplePermissionsState(
                            listOf(
                                android.Manifest.permission.POST_NOTIFICATIONS
                            )
                        )
                    } else {
                        TODO("VERSION.SDK_INT < TIRAMISU")
                    }
                LaunchedEffect(Unit) {
                    locationPermissionState.launchMultiplePermissionRequest()
                }

                if (locationPermissionState.allPermissionsGranted) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Column {
                            SpeedCalcScreen()
                        }
                    }
                }
            }
        }
    }
}