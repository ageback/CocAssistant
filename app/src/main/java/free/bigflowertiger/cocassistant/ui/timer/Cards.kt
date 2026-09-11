@file:OptIn(ExperimentalGridApi::class)

package free.bigflowertiger.cocassistant.ui.timer


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import free.bigflowertiger.cocassistant.R

@Composable
fun NumberCard(
    number: Int,
    onClick: (Int) -> Unit
) {
    TextCard(
        number.toString(),
        onClick = { onClick(number) }
    )
}

@Composable
fun PainterIconCard(
    modifier: Modifier = Modifier,
    containerColor: Color = CardDefaults.cardColors().containerColor,
    contentColor: Color = CardDefaults.cardColors().contentColor,
    shape: Shape = CardDefaults.shape,
    onLongClick: () -> Unit = {},
    onClick: (String) -> Unit = {}
) {
    ClickableCard(
        label = "",
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        shape = shape,
        onClick = onClick,
        onLongClick = onLongClick
    ) {
        Icon(
            painter = painterResource(R.drawable.backspace_48px),
            modifier = Modifier.size(32.dp),
            contentDescription = "回退"
        )
    }
}

@Composable
fun TextCard(
    label: String,
    modifier: Modifier = Modifier,
    containerColor: Color = CardDefaults.cardColors().containerColor,
    contentColor: Color = CardDefaults.cardColors().contentColor,
    shape: Shape = CardDefaults.shape,
    onClick: (String) -> Unit = {},
) {
    ClickableCard(
        label = label,
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        shape = shape,
        onClick = onClick
    ) {
        Text(
            label,
            color = contentColor,
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ClickableCard(
    label: String,
    modifier: Modifier = Modifier,
    containerColor: Color = CardDefaults.cardColors().containerColor,
    contentColor: Color = CardDefaults.cardColors().contentColor,
    shape: Shape = CardDefaults.shape,
    onClick: (String) -> Unit = {},
    onLongClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .combinedClickable(
                interactionSource = interactionSource,
                onClick = { onClick(label) },
                onLongClick = onLongClick
            )
            .semantics {
                role = Role.Button
            }
            .clip(shape)
            .background(containerColor)
    ) {
        content()
    }
}