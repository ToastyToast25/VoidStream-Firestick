package org.jellyfin.androidtv.ui.base

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween

/**
 * Shared animation specifications for the VoidStream premium UI.
 */
object AnimationSpecs {
	// Focus transitions
	val FocusScale = spring<Float>(
		dampingRatio = 0.7f,
		stiffness = Spring.StiffnessMedium,
	)

	val FocusAlpha = tween<Float>(durationMillis = 200)

	// Content reveals
	val ContentReveal = tween<Float>(durationMillis = 320)
	const val ContentStaggerDelayMs = 40

	// Page transitions
	val PageTransition = tween<Float>(durationMillis = 400)

	// Carousel
	val CarouselCrossfade = tween<Float>(durationMillis = 600)
	const val CarouselAutoAdvanceMs = 8000L

	// Overlay fade
	val OverlayFadeIn = tween<Float>(durationMillis = 280)
	val OverlayFadeOut = tween<Float>(durationMillis = 200)

	// Player controls auto-hide
	const val PlayerControlsAutoHideMs = 4000L
	const val QuickInfoAutoHideMs = 2000L

	// Scale values
	const val FocusedScale = 1.08f
	const val PressedScale = 1.05f
	const val UnfocusedScale = 1.0f

	// Parallax
	const val ParallaxBackgroundRate = 0.3f
	const val ParallaxBackdropZoomStart = 1.0f
	const val ParallaxBackdropZoomEnd = 1.2f
}
