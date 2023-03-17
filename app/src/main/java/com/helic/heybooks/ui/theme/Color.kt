package com.helic.heybooks.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val blueBG = Color(0xFFF4F7FD)
val blueBGNight = Color(0xFF0C1B3A)
val blueText = Color(0xFF1E3054)
val blueTextNight = Color(0xFFF5CAC9)
val card = Color(0xFFFFFFFF)
val cardNight = Color(0xFF162544)

val blue = Color(0xFF006AF6)
val blueNight = Color(0xFF147EFF)


val RedColor = Color(0xFFEB5757)

val Colors.BackgroundColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) blueBG else blueBGNight

val Colors.TextColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) blueText else blueTextNight

val Colors.CardColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) card else cardNight

val Colors.ButtonColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color.Black else Color.White

val Colors.ButtonTextColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color.White else Color.Black