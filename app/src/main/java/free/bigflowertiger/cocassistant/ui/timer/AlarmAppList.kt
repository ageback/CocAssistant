package free.bigflowertiger.cocassistant.ui.timer

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.provider.AlarmClock
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap

data class AlarmAppInfo(
    val packageName: String,
    val name: String,
    val icon: Drawable
)

@Composable
fun AlarmAppList(
    selectedPackageName: String?,
    onAppSelected: (AlarmAppInfo) -> Unit = {}
) {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val intent = Intent(AlarmClock.ACTION_SET_TIMER)

    val apps = packageManager
        .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        .map {
            val appInfo = it.activityInfo.applicationInfo

            AlarmAppInfo(
                packageName = appInfo.packageName,
                name = appInfo.loadLabel(packageManager).toString(),
                icon = appInfo.loadIcon(packageManager)
            )
        }
    LazyColumn {
        items(
            items = apps,
            key = { it.packageName }
        ) { app ->
            val selected = app.packageName == selectedPackageName
            AlarmAppItem(app, selected) { onAppSelected(it) }
        }
    }
}

@Composable
fun AlarmAppItem(
    app: AlarmAppInfo,
    selected: Boolean,
    onAppSelected: (AlarmAppInfo) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onAppSelected(app)
            }
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            bitmap = app.icon
                .toBitmap()
                .asImageBitmap(),
            contentDescription = app.name,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = app.name,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )

        RadioButton(
            selected = selected,
            onClick = {
                onAppSelected(app)
            }
        )
    }
}