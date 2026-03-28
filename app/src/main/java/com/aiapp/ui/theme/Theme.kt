package com.aiapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val Purple40 = Color(0xFF6650a4)
private val PurpleGrey40 = Color(0xFF625b71)
private val Pink40 = Color(0xFF7D5260)

private val Purple80 = Color(0xFFD0BCFF)
private val PurpleGrey80 = Color(0xFFCCC2DC)
private val Pink80 = Color(0xFFEFB8C8)

private val Blue40 = Color(0xFF1976D2)
private val Blue80 = Color(0xFF90CAF9)
private val Green40 = Color(0xFF388E3C)
private val Green80 = Color(0xFFA5D6A7)
private val Orange40 = Color(0xFFF57C00)
private val Orange80 = Color(0xFFFFCC80)
private val Red40 = Color(0xFFD32F2F)
private val Red80 = Color(0xFFEF9A9A)

private fun getColorScheme(colorName: String, darkTheme: Boolean) = when (colorName) {
    "purple" -> if (darkTheme) darkColorScheme(
        primary = Purple80, secondary = PurpleGrey80, tertiary = Pink80
    ) else lightColorScheme(
        primary = Purple40, secondary = PurpleGrey40, tertiary = Pink40
    )
    "blue" -> if (darkTheme) darkColorScheme(
        primary = Blue80, secondary = Blue80, tertiary = Blue80
    ) else lightColorScheme(
        primary = Blue40, secondary = Blue40, tertiary = Blue40
    )
    "green" -> if (darkTheme) darkColorScheme(
        primary = Green80, secondary = Green80, tertiary = Green80
    ) else lightColorScheme(
        primary = Green40, secondary = Green40, tertiary = Green40
    )
    "orange" -> if (darkTheme) darkColorScheme(
        primary = Orange80, secondary = Orange80, tertiary = Orange80
    ) else lightColorScheme(
        primary = Orange40, secondary = Orange40, tertiary = Orange40
    )
    "red" -> if (darkTheme) darkColorScheme(
        primary = Red80, secondary = Red80, tertiary = Red80
    ) else lightColorScheme(
        primary = Red40, secondary = Red40, tertiary = Red40
    )
    else -> null
}

@Composable
fun AIAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeColor: String = "system",
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    
    val colorScheme = when {
        themeColor == "system" && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> getColorScheme(themeColor, darkTheme) ?: if (darkTheme) darkColorScheme() else lightColorScheme()
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
