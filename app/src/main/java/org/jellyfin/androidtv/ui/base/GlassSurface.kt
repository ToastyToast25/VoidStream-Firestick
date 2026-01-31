package org.jellyfin.androidtv.ui.base

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A glassmorphism surface composable.
 *
 * Creates a frosted-glass effect with:
 * - Semi-transparent white overlay (12% white by default)
 * - Backdrop blur (on Android 12+, fallback to solid dark on older)
 * - Subtle white border (20% white)
 *
 * @param modifier Modifier for the surface.
 * @param cornerRadius Corner radius of the glass card.
 * @param backgroundColor Override the glass background color.
 * @param borderColor Override the border color.
 * @param borderWidth Border width.
 * @param blurRadius Blur radius for the backdrop (Android 12+ only).
 * @param content Content inside the glass surface.
 */
@Composable
fun GlassSurface(
	modifier: Modifier = Modifier,
	cornerRadius: Dp = 12.dp,
	backgroundColor: Color = JellyfinTheme.colorScheme.glass,
	borderColor: Color = JellyfinTheme.colorScheme.glassBorder,
	borderWidth: Dp = 1.dp,
	blurRadius: Dp = 24.dp,
	content: @Composable BoxScope.() -> Unit,
) {
	val shape = RoundedCornerShape(cornerRadius)

	// Backdrop blur is only available on Android 12+ (API 31+).
	// On older versions, use a slightly more opaque background as fallback.
	val effectiveBackground = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
		backgroundColor
	} else {
		// Fallback: darker semi-transparent background to simulate glass
		Color(0x33000000) // 20% black
	}

	val blurModifier = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
		Modifier.blur(blurRadius)
	} else {
		Modifier
	}

	Box(
		modifier = modifier
			.then(blurModifier)
			.clip(shape)
			.background(effectiveBackground, shape)
			.border(borderWidth, borderColor, shape),
		content = content,
	)
}

/**
 * A glass surface specifically styled for overlays and control bars.
 * Uses heavier scrim for better readability over media content.
 */
@Composable
fun GlassOverlay(
	modifier: Modifier = Modifier,
	cornerRadius: Dp = 0.dp,
	content: @Composable BoxScope.() -> Unit,
) {
	GlassSurface(
		modifier = modifier,
		cornerRadius = cornerRadius,
		backgroundColor = Color(0x33000000), // 20% black for overlays
		borderColor = Color.Transparent,
		borderWidth = 0.dp,
		blurRadius = 12.dp,
		content = content,
	)
}
