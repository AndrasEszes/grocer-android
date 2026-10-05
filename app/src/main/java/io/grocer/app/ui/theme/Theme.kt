package io.grocer.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GrocerColors = lightColorScheme(
    primary = Color(0xFF2E7D32),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFC8E6C9),
    onPrimaryContainer = Color(0xFF0B3D0F),
    secondary = Color(0xFFF57C00),
    error = Color(0xFFC62828),
)

@Composable
fun GrocerTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = GrocerColors, content = content)
}
