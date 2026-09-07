package free.bigflowertiger.cocassistant.ui.timer.inputpad

/**
 * 定时器倍数
 * @property title 计时器名称
 * @property label 按钮label
 * @property multiples 倍数
 */
data class DurationMultiples(
    val title: String,
    val label:String,
    val multiples: Int
)