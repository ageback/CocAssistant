package free.bigflowertiger.cocassistant.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SpeedCalcScreen(
    viewModel: SpeedCalcViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    var input by remember { mutableStateOf("") }
    Column(modifier = Modifier.padding(4.dp)) {
        Row {
            OutlinedTextField(
                label = {
                    Text(text = "研究时间（分钟）")
                },
                value = input,
                onValueChange = {
                    input = it
                    viewModel.getActualResearchMinutes(it)
                }
            )

            Text(text = state.actualResearchMinutes.toString())
        }
    }
}
