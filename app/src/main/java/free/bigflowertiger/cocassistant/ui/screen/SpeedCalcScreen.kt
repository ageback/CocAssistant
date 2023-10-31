package free.bigflowertiger.cocassistant.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SpeedCalcScreen(
    viewModel: SpeedCalcViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    var inputHour by remember { mutableStateOf("") }
    var inputMinute by remember { mutableStateOf("") }
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
                viewModel.getActualResearchMinutes(inputHour, inputMinute)
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
                viewModel.getActualResearchMinutes(inputHour, inputMinute)
            }
        )

        Text(
            modifier = Modifier.padding(8.dp),
            fontSize = 20.sp,
            text = state.actualResearchMinutes.toString()
        )
    }
}
