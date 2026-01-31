package org.jellyfin.androidtv.ui.base

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

fun colorScheme(): ColorScheme = ColorScheme(
	background = Color(0xFF000000),
	onBackground = Color(0xFFFFFFFF),
	button = Color(0xB3000000),
	onButton = Color(0xFFDDDDDD),
	buttonFocused = Color(0xE6CCCCCC),
	onButtonFocused = Color(0xFF444444),
	buttonDisabled = Color(0x33747474),
	onButtonDisabled = Color(0xFF686868),
	surfacePrimary = Color(0xFF000000),
	surfaceElevated = Color(0xFF0A0A0A),
	surfaceCard = Color(0xFF1A1A1A),
	glass = Color(0x1FFFFFFF),
	glassBorder = Color(0x33FFFFFF),
	scrimLight = Color(0x1A000000),
	scrimMedium = Color(0x80000000),
	scrimHeavy = Color(0xD9000000),
	focusBorder = Color(0xFFFFFFFF),
	focusBorderUnfocused = Color(0x26FFFFFF),
	focusGlow = Color(0x66FFFFFF),
	focusSurface = Color(0x0FFFFFFF),
	accent = Color(0xFF00A4DC),
	onAccent = Color(0xFFFFFFFF),
	accentSecondary = Color(0xFFAA5CC3),
	error = Color(0xFFCC3333),
	success = Color(0xFF4CAF50),
	warning = Color(0xFFFF9800),
	textPrimary = Color(0xFFFFFFFF),
	textSecondary = Color(0xB3FFFFFF),
	textTertiary = Color(0x80FFFFFF),
	textOnMedia = Color(0xE6FFFFFF),
	progressPlayed = Color(0xFFFFFFFF),
	progressBuffered = Color(0x4DFFFFFF),
	progressRemaining = Color(0x1AFFFFFF),
)

@Immutable
data class ColorScheme(
	val background: Color,
	val onBackground: Color,
	val button: Color,
	val onButton: Color,
	val buttonFocused: Color,
	val onButtonFocused: Color,
	val buttonDisabled: Color,
	val onButtonDisabled: Color,
	// Surface hierarchy
	val surfacePrimary: Color = Color(0xFF000000),
	val surfaceElevated: Color = Color(0xFF0A0A0A),
	val surfaceCard: Color = Color(0xFF1A1A1A),
	// Glass / overlay
	val glass: Color = Color(0x1FFFFFFF),
	val glassBorder: Color = Color(0x33FFFFFF),
	val scrimLight: Color = Color(0x1A000000),
	val scrimMedium: Color = Color(0x80000000),
	val scrimHeavy: Color = Color(0xD9000000),
	// Focus
	val focusBorder: Color = Color(0xFFFFFFFF),
	val focusBorderUnfocused: Color = Color(0x26FFFFFF),
	val focusGlow: Color = Color(0x66FFFFFF),
	val focusSurface: Color = Color(0x0FFFFFFF),
	// Accent
	val accent: Color = Color(0xFF00A4DC),
	val onAccent: Color = Color(0xFFFFFFFF),
	val accentSecondary: Color = Color(0xFFAA5CC3),
	// Semantic
	val error: Color = Color(0xFFCC3333),
	val success: Color = Color(0xFF4CAF50),
	val warning: Color = Color(0xFFFF9800),
	// Text hierarchy
	val textPrimary: Color = Color(0xFFFFFFFF),
	val textSecondary: Color = Color(0xB3FFFFFF),
	val textTertiary: Color = Color(0x80FFFFFF),
	val textOnMedia: Color = Color(0xE6FFFFFF),
	// Progress
	val progressPlayed: Color = Color(0xFFFFFFFF),
	val progressBuffered: Color = Color(0x4DFFFFFF),
	val progressRemaining: Color = Color(0x1AFFFFFF),
)

val LocalColorScheme = staticCompositionLocalOf { colorScheme() }
