package org.jellyfin.androidtv.ui.base.modifier

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jellyfin.androidtv.ui.base.AnimationSpecs
import org.jellyfin.androidtv.ui.base.JellyfinTheme

/**
 * Premium focus glow effect for TV navigation.
 *
 * Applies scale, glow border, elevation shadow, and surface tint on focus.
 *
 * @param cornerRadius Corner radius for the glow border and shadow shape.
 * @param focusedScale Scale factor when focused (default 1.08).
 * @param glowSpread Extra spread beyond the border for the glow effect.
 * @param enabled Whether this modifier is active.
 */
fun Modifier.focusGlow(
	cornerRadius: Dp = 12.dp,
	focusedScale: Float = AnimationSpecs.FocusedScale,
	glowSpread: Dp = 8.dp,
	enabled: Boolean = true,
): Modifier = if (!enabled) this else composed {
	val colors = JellyfinTheme.colorScheme
	var isFocused by remember { mutableStateOf(false) }

	val scale by animateFloatAsState(
		targetValue = if (isFocused) focusedScale else AnimationSpecs.UnfocusedScale,
		animationSpec = AnimationSpecs.FocusScale,
		label = "focusScale",
	)

	val borderWidth by animateDpAsState(
		targetValue = if (isFocused) 3.dp else 1.dp,
		label = "focusBorderWidth",
	)

	val elevation by animateDpAsState(
		targetValue = if (isFocused) 12.dp else 0.dp,
		label = "focusElevation",
	)

	val borderColor = if (isFocused) colors.focusBorder else colors.focusBorderUnfocused
	val glowColor = if (isFocused) colors.focusGlow else Color.Transparent
	val shape = RoundedCornerShape(cornerRadius)

	this
		.onFocusChanged { isFocused = it.isFocused }
		.focusable()
		.scale(scale)
		.shadow(elevation, shape, clip = false)
		.drawBehind {
			if (isFocused) {
				// Outer glow halo
				val spread = glowSpread.toPx()
				drawRoundRect(
					color = glowColor,
					topLeft = Offset(-spread, -spread),
					size = Size(size.width + spread * 2, size.height + spread * 2),
					cornerRadius = CornerRadius(cornerRadius.toPx() + spread),
					style = Stroke(width = spread),
				)
			}
		}
		.border(borderWidth, borderColor, shape)
}

/**
 * Simplified focus scale effect without the glow border.
 * Useful for items that already have their own border treatment.
 */
fun Modifier.focusScale(
	focusedScale: Float = AnimationSpecs.FocusedScale,
): Modifier = composed {
	var isFocused by remember { mutableStateOf(false) }

	val scale by animateFloatAsState(
		targetValue = if (isFocused) focusedScale else AnimationSpecs.UnfocusedScale,
		animationSpec = AnimationSpecs.FocusScale,
		label = "focusScale",
	)

	this
		.onFocusChanged { isFocused = it.isFocused }
		.focusable()
		.scale(scale)
}
