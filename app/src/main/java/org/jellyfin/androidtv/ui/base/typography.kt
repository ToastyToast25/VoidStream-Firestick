package org.jellyfin.androidtv.ui.base

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object TypographyDefaults {
	val Default: TextStyle = TextStyle.Default

	/** Hero titles on detail pages and carousel. */
	val Hero: TextStyle = TextStyle(
		fontSize = 56.sp,
		fontWeight = FontWeight.ExtraBold,
		letterSpacing = (-0.5).sp,
	)

	/** Section headers on home screen and library. */
	val SectionHeader: TextStyle = TextStyle(
		fontSize = 32.sp,
		fontWeight = FontWeight.Bold,
		letterSpacing = (-0.3).sp,
	)

	/** Card titles. */
	val CardTitle: TextStyle = TextStyle(
		fontSize = 18.sp,
		fontWeight = FontWeight.SemiBold,
	)

	/** Body text — descriptions, summaries. */
	val BodyLarge: TextStyle = TextStyle(
		fontSize = 16.sp,
		fontWeight = FontWeight.Normal,
		letterSpacing = 0.2.sp,
	)

	/** Metadata labels — year, runtime, rating. */
	val Metadata: TextStyle = TextStyle(
		fontSize = 14.sp,
		fontWeight = FontWeight.Medium,
		letterSpacing = 0.4.sp,
	)

	/** Small labels — badges, timestamps. */
	val Label: TextStyle = TextStyle(
		fontSize = 12.sp,
		fontWeight = FontWeight.Medium,
		letterSpacing = 0.5.sp,
	)

	/** Button text. */
	val Button: TextStyle = TextStyle(
		fontSize = 18.sp,
		fontWeight = FontWeight.SemiBold,
	)

	/** Large button text (CTA). */
	val ButtonLarge: TextStyle = TextStyle(
		fontSize = 20.sp,
		fontWeight = FontWeight.SemiBold,
	)
}

@Immutable
data class Typography(
	val default: TextStyle = TypographyDefaults.Default,
	val hero: TextStyle = TypographyDefaults.Hero,
	val sectionHeader: TextStyle = TypographyDefaults.SectionHeader,
	val cardTitle: TextStyle = TypographyDefaults.CardTitle,
	val bodyLarge: TextStyle = TypographyDefaults.BodyLarge,
	val metadata: TextStyle = TypographyDefaults.Metadata,
	val label: TextStyle = TypographyDefaults.Label,
	val button: TextStyle = TypographyDefaults.Button,
	val buttonLarge: TextStyle = TypographyDefaults.ButtonLarge,
)

val LocalTypography = staticCompositionLocalOf { Typography() }
