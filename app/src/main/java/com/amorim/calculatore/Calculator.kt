import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

/**
 * A simple calculator with a numeric keypad.
 *
 * When the equals button is pressed, the typed expression moves upward and turns gray,
 * while the computed result appears in dark green in the same area.
 * If the user then presses an operator, the result is used as the new expression,
 * turning the result text back to black and allowing further calculations.
 */
@Composable
fun Calculator() {
    // Holds the current typed expression.
    var expression by remember { mutableStateOf("") }
    // Holds the computed result.
    var result by remember { mutableStateOf("") }

    /**
     * Appends a character to the expression.
     * If a result is already displayed and no operator is pressed, resets the calculator.
     */
    fun onAppend(char: String) {
        if (result.isNotEmpty()) {
            // If a result is present and a digit or decimal is pressed,
            // start a new calculation.
            expression = char
            result = ""
        } else {
            expression += char
        }
    }

    /**
     * Clears both the expression and the result.
     */
    fun onClear() {
        expression = ""
        result = ""
    }

    /**
     * Appends an operator to the expression.
     * If a result is already displayed, uses the result as the new expression.
     * Prevents adding an operator if one already exists.
     */
    fun onOperation(operator: String) {
        if (result.isNotEmpty()) {
            // Use the computed result as the starting expression.
            expression = result
            result = ""
        }
        // Prevent adding an operator if the expression is empty or already contains one.
        if (expression.isEmpty() ||
            expression.contains("+") ||
            expression.contains("-") ||
            expression.contains("×") ||
            expression.contains("÷")
        ) return
        expression += operator
    }

    /**
     * Evaluates the expression and updates the result.
     * Only supports a single binary operation.
     * Formats whole numbers without a trailing ".0".
     */
    fun onEquals() {
        try {
            val computed = when {
                expression.contains("+") -> {
                    val parts = expression.split("+")
                    if (parts.size == 2) parts[0].toDouble() + parts[1].toDouble() else return
                }
                expression.contains("-") -> {
                    val parts = expression.split("-")
                    if (parts.size == 2) parts[0].toDouble() - parts[1].toDouble() else return
                }
                expression.contains("×") -> {
                    val parts = expression.split("×")
                    if (parts.size == 2) parts[0].toDouble() * parts[1].toDouble() else return
                }
                expression.contains("÷") -> {
                    val parts = expression.split("÷")
                    if (parts.size == 2) {
                        val divisor = parts[1].toDouble()
                        if (divisor != 0.0) parts[0].toDouble() / divisor else return
                    } else return
                }
                else -> return
            }
            result = if (computed % 1.0 == 0.0) {
                computed.toInt().toString()
            } else {
                computed.toString()
            }
        } catch (e: Exception) {
            result = "Error"
        }
    }

    // Animate upward offset for the expression text when a result is present.
    val expressionOffsetY by animateDpAsState(
        targetValue = if (result.isNotEmpty()) (-15).dp else 0.dp,
        animationSpec = tween(durationMillis = 300)
    )
    // Animate the expression text color: turn gray when a result is shown.
    val expressionTextColor by animateColorAsState(
        targetValue = if (result.isNotEmpty()) Color.Gray else Color.Black,
        animationSpec = tween(durationMillis = 300)
    )

    // Main layout: display area (top) and keypad (bottom)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.End
    ) {
        // Display area (approximately top 40% of the screen)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f),
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                // The expression text (input) moves upward and turns gray when a result is present.
                if (expression.isNotEmpty()) {
                    Text(
                        text = expression,
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = 42.sp,  // Smaller font for the expression
                            fontWeight = FontWeight.Bold,
                            color = expressionTextColor
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = expressionOffsetY)
                    )
                }
                // The computed result appears in dark green with a larger font.
                if (result.isNotEmpty()) {
                    Text(
                        text = result,
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = 62.sp,  // Larger font for the result
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32) // Dark green color
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Keypad area (approximately bottom 60% of the screen)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            val buttonModifier = Modifier.size(80.dp)
            val digitButtonColor = Color(0xFFEEEEEE)   // Light gray for digit buttons.
            val actionButtonColor = Color(0xFF64B5F6)  // Blue for action buttons.

            // Row 1: 7, 8, 9, ÷
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                CalculatorButton("7", buttonModifier, digitButtonColor, Color.Black) { onAppend("7") }
                CalculatorButton("8", buttonModifier, digitButtonColor, Color.Black) { onAppend("8") }
                CalculatorButton("9", buttonModifier, digitButtonColor, Color.Black) { onAppend("9") }
                CalculatorButton("÷", buttonModifier, actionButtonColor, Color.White) { onOperation("÷") }
            }
            // Row 2: 4, 5, 6, ×
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                CalculatorButton("4", buttonModifier, digitButtonColor, Color.Black) { onAppend("4") }
                CalculatorButton("5", buttonModifier, digitButtonColor, Color.Black) { onAppend("5") }
                CalculatorButton("6", buttonModifier, digitButtonColor, Color.Black) { onAppend("6") }
                CalculatorButton("×", buttonModifier, actionButtonColor, Color.White) { onOperation("×") }
            }
            // Row 3: 1, 2, 3, -
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                CalculatorButton("1", buttonModifier, digitButtonColor, Color.Black) { onAppend("1") }
                CalculatorButton("2", buttonModifier, digitButtonColor, Color.Black) { onAppend("2") }
                CalculatorButton("3", buttonModifier, digitButtonColor, Color.Black) { onAppend("3") }
                CalculatorButton("-", buttonModifier, actionButtonColor, Color.White) { onOperation("-") }
            }
            // Row 4: ., 0, C, +
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                CalculatorButton(".", buttonModifier, digitButtonColor, Color.Black) { onAppend(".") }
                CalculatorButton("0", buttonModifier, digitButtonColor, Color.Black) { onAppend("0") }
                CalculatorButton("C", buttonModifier, actionButtonColor, Color.White) { onClear() }
                CalculatorButton("+", buttonModifier, actionButtonColor, Color.White) { onOperation("+") }
            }
            // Row 5: Full-width equals button.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { onEquals() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = actionButtonColor),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                ) {
                    Text(
                        text = "=",
                        style = MaterialTheme.typography.headlineSmall.copy(fontSize = 24.sp, color = Color.White)
                    )
                }
            }
        }
    }
}

/**
 * A reusable composable for a calculator keypad button.
 *
 * @param label The button's label.
 * @param modifier Modifier for sizing and styling.
 * @param backgroundColor The background color of the button.
 * @param textColor The text color of the button.
 * @param onClick Action performed when the button is clicked.
 */
@Composable
fun CalculatorButton(
    label: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 24.sp, color = textColor)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    MaterialTheme {
        Calculator()
    }
}