package org.jellyfin.androidtv.ui.home.carousel

import android.os.Build
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.CarouselDefaults
import androidx.tv.material3.CarouselState
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.ShapeDefaults
import androidx.tv.material3.Text
import kotlinx.coroutines.launch
import org.jellyfin.androidtv.preference.UserPreferences
import org.jellyfin.androidtv.ui.base.GlassSurface
import org.jellyfin.androidtv.ui.base.JellyfinTheme
import org.jellyfin.androidtv.ui.composable.AsyncImage
import org.koin.compose.koinInject
import kotlin.random.Random

@Composable
private fun SnowfallEffect(
	modifier: Modifier = Modifier,
	snowflakeCount: Int = 30
) {
	val snowflakes = remember {
		List(snowflakeCount) {
			SnowflakeState(
				x = Random.nextFloat(),
				y = Random.nextFloat() * -0.3f,
				size = Random.nextFloat() * 5f + 3f,
				speed = Random.nextFloat() * 0.2f + 0.1f,
				drift = Random.nextFloat() * 0.2f - 0.1f
			)
		}
	}

	var timeNanos by remember { mutableLongStateOf(0L) }

	LaunchedEffect(Unit) {
		while (true) {
			withFrameNanos { timeNanos = it }
		}
	}

	Canvas(
		modifier = modifier
			.fillMaxSize()
			.drawWithCache {
				val snowColor = Color.White.copy(alpha = 0.8f)

				onDrawBehind {
					val timeSeconds = timeNanos / 1_000_000_000f

					snowflakes.forEach { snowflake ->
						val adjustedTime = timeSeconds * snowflake.speed

						val yPos =
							((snowflake.y + adjustedTime) % 1.3f) * size.height

						val xPos =
							(snowflake.x +
								kotlin.math.sin(adjustedTime * 2f) *
								snowflake.drift) * size.width

						drawCircle(
							color = snowColor,
							radius = snowflake.size,
							center = Offset(xPos, yPos)
						)
					}
				}
			}
	) {}
}

private data class SnowflakeState(
    val x: Float,
    val y: Float,
    val size: Float,
    val speed: Float,
    val drift: Float
)

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun FeaturedCarousel(
	items: List<CarouselItem>,
	onItemSelected: (CarouselItem) -> Unit,
	modifier: Modifier = Modifier,
	isPaused: Boolean = false,
	activeIndex: Int = 0,
	onActiveIndexChanged: (Int) -> Unit = {}
) {
	if (items.isEmpty()) {
		timber.log.Timber.d("FeaturedCarousel: Showing no items message")
		Box(
			modifier = modifier
				.fillMaxSize()
				.focusable()
				.onKeyEvent { keyEvent ->
					if (keyEvent.type == KeyEventType.KeyDown) {
						when (keyEvent.key) {
							Key.DirectionDown -> {
								// Let focus move to the next focusable element (toolbar)
								false
							}
							else -> false
						}
					} else false
				}
				.background(MaterialTheme.colorScheme.surface)
		) {
			Box(
				modifier = Modifier
					.align(Alignment.Center)
					.background(Color.Magenta)
					.padding(16.dp)
			) {
				Text(
					text = "NO FEATURED ITEMS - DEBUG TEST",
					style = MaterialTheme.typography.bodyLarge.copy(
						fontSize = 24.sp,
						fontWeight = FontWeight.Bold
					),
					color = Color.Cyan,
					textAlign = TextAlign.Center
				)
			}
		}
		return
	}

	var isCarouselFocused by remember { mutableStateOf(false) }
	val borderAlpha = if (isCarouselFocused) 0.6f else 0.08f
	var actualCarouselIndex by remember { mutableIntStateOf(activeIndex) }
	val carouselState = remember { CarouselState() }

	var currentIndex by remember { mutableIntStateOf(activeIndex) }
	val context = LocalContext.current
	var isManualNavigation by remember { mutableStateOf(false) } // Flag to prevent auto-scroll during manual navigation
	var lastManualNavigationTime by remember { mutableLongStateOf(0L) } // Track last manual navigation time
	val isAndroid12OrLower = remember { Build.VERSION.SDK_INT <= Build.VERSION_CODES.S }
	var autoScrollEnabled by remember { mutableStateOf(true) }

	LaunchedEffect(activeIndex) {
		if (activeIndex != currentIndex) {
			currentIndex = activeIndex
			actualCarouselIndex = activeIndex
		}
	}

	LaunchedEffect(isPaused, autoScrollEnabled, isCarouselFocused) {
		if (items.size > 1 && autoScrollEnabled && !isCarouselFocused && !isPaused) {
			while (true) {
				kotlinx.coroutines.delay(8000L) // 8 seconds delay

				if (autoScrollEnabled && !isCarouselFocused && !isPaused) {
					val newIndex = (currentIndex + 1) % items.size
					currentIndex = newIndex
					actualCarouselIndex = newIndex
					onActiveIndexChanged(newIndex)

					if (isAndroid12OrLower) {
					}
				} else {
					break
				}
			}
		} else {
		}
	}

	val disableAutoScrollTemporarily = {
		autoScrollEnabled = false

		kotlinx.coroutines.GlobalScope.launch {
			kotlinx.coroutines.delay(5000L) // 5 second delay
			autoScrollEnabled = true
		}
	}
	// Don't steal focus pls
	Android12CompatibleCarousel(
		items = items,
		currentIndex = currentIndex,
		onItemSelected = onItemSelected,
		onNavigate = { newIndex ->
			currentIndex = newIndex
			actualCarouselIndex = newIndex
			onActiveIndexChanged(newIndex)
		},
		onManualNavigation = { isManual ->
			isManualNavigation = isManual
			if (isManual) {
				lastManualNavigationTime = System.currentTimeMillis()
				disableAutoScrollTemporarily()
			} else {
			}
		},
		isCarouselFocused = isCarouselFocused,
		borderAlpha = borderAlpha,
		modifier = modifier
			.onFocusChanged { focusState ->
				isCarouselFocused = focusState.isFocused
			}
	)
}

@Composable
private fun Android12CompatibleCarousel(
	items: List<CarouselItem>,
	currentIndex: Int,
	onItemSelected: (CarouselItem) -> Unit,
	onNavigate: (Int) -> Unit,
	onManualNavigation: (Boolean) -> Unit,
	isCarouselFocused: Boolean,
	borderAlpha: Float,
	modifier: Modifier = Modifier
) {
	val carouselFocusRequester = remember { FocusRequester() }
	var carouselHasFocus by remember { mutableStateOf(false) }

	Box(
		modifier = modifier
			.fillMaxSize()
			.focusRequester(carouselFocusRequester)
			.focusable()
			.onFocusChanged { focusState ->
				carouselHasFocus = focusState.isFocused
				timber.log.Timber.d("Android12CompatibleCarousel focus changed: ${focusState.isFocused}")
			}
			.onKeyEvent { keyEvent ->
				if (keyEvent.type != KeyEventType.KeyDown) {
					return@onKeyEvent false
				}

				when (keyEvent.key) {
					Key.DirectionCenter, Key.Enter -> {
						onItemSelected(items[currentIndex])
						true
					}
					Key.DirectionLeft -> {
						if (items.isNotEmpty()) {
							onManualNavigation(true)
							val newIndex = if (currentIndex == 0) items.size - 1 else currentIndex - 1
							timber.log.Timber.d("Manual navigation: previous item $newIndex")
							onNavigate(newIndex)
						}
						true
					}
					Key.DirectionRight -> {
						if (items.isNotEmpty()) {
							onManualNavigation(true)
							val newIndex = (currentIndex + 1) % items.size
							timber.log.Timber.d("Manual navigation: next item $newIndex")
							onNavigate(newIndex)
						}
						true
					}
					else -> false
				}
			}
			.border(
				width = 2.dp,
				color = Color.White.copy(alpha = borderAlpha),
				shape = RoundedCornerShape(12.dp),
			)
			.clip(RoundedCornerShape(12.dp))
			.semantics {
				contentDescription = "Featured items carousel - Android 12 compatibility mode"
			}
	) {
		if (items.isNotEmpty()) {
			val currentItem = items[currentIndex]

			CarouselItemBackground(item = currentItem, modifier = Modifier.fillMaxSize())
			CarouselItemForeground(
				item = currentItem,
				isCarouselFocused = carouselHasFocus,
				onItemSelected = { onItemSelected(currentItem) },
				modifier = Modifier.fillMaxSize()
			)

			val userPreferences = koinInject<UserPreferences>()
			if (userPreferences[UserPreferences.snowfallEnabled]) {
				SnowfallEffect(modifier = Modifier.fillMaxSize())
			}

			// Indicator
			CarouselIndicator(
				itemCount = items.size,
				activeItemIndex = currentIndex,
				modifier = Modifier.align(Alignment.BottomEnd)
			)
		}
	}
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun BoxScope.CarouselIndicator(
	itemCount: Int,
	activeItemIndex: Int,
	modifier: Modifier = Modifier
) {
	GlassSurface(
		modifier = modifier
			.padding(16.dp)
			.align(Alignment.BottomEnd),
		cornerRadius = 8.dp,
		borderWidth = 0.dp,
		borderColor = Color.Transparent,
	) {
		CarouselDefaults.IndicatorRow(
			modifier = Modifier
				.align(Alignment.Center)
				.padding(horizontal = 12.dp, vertical = 6.dp),
			itemCount = itemCount,
			activeItemIndex = activeItemIndex,
		)
	}
}

@Composable
private fun CarouselItemForeground(
	item: CarouselItem,
	isCarouselFocused: Boolean = false,
	onItemSelected: () -> Unit,
	modifier: Modifier = Modifier
) {
	val colors = JellyfinTheme.colorScheme
	val typography = JellyfinTheme.typography
	val metadataStyle = typography.metadata.copy(
		color = colors.textOnMedia,
		shadow = Shadow(
			color = Color.Black.copy(alpha = 0.5f),
			offset = Offset(x = 1f, y = 2f),
			blurRadius = 4f
		)
	)

	Box(
		modifier = modifier,
		contentAlignment = Alignment.BottomStart
	) {
		// Logo or title at top
		item.logoUrl?.let { logoUrl ->
			Box(
				modifier = Modifier
					.align(Alignment.TopStart)
					.padding(start = 48.dp, top = 24.dp)
			) {
				AsyncImage(
					modifier = Modifier
						.height(110.dp)
						.width(240.dp),
					url = logoUrl
				)
			}
		} ?: run {
			Box(
				modifier = Modifier
					.align(Alignment.TopStart)
					.padding(start = 48.dp, top = 48.dp)
			) {
				Text(
					text = item.title,
					style = typography.hero.copy(
						fontSize = 36.sp,
						lineHeight = 40.sp,
						shadow = Shadow(
							color = Color.Black.copy(alpha = 0.7f),
							offset = Offset(x = 2f, y = 4f),
							blurRadius = 6f
						)
					),
					maxLines = 2,
					overflow = TextOverflow.Ellipsis,
					color = colors.textPrimary,
					modifier = Modifier.fillMaxWidth(0.5f)
				)
			}
		}

		// Metadata + description column
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(start = 48.dp, top = 130.dp, bottom = 56.dp, end = 0.dp),
			verticalArrangement = Arrangement.Top,
			horizontalAlignment = Alignment.Start
		) {
			val yearAndRuntime = listOfNotNull(
				item.getYear().takeIf { it.isNotEmpty() },
				item.getRuntime().takeIf { it.isNotEmpty() }
			).joinToString("  •  ")

			// Metadata row
			Row(
				modifier = Modifier.padding(top = 8.dp),
				horizontalArrangement = Arrangement.spacedBy(1.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				if (yearAndRuntime.isNotEmpty()) {
					Text(text = yearAndRuntime, style = metadataStyle)
				}

				item.communityRating?.let { communityRating ->
					if (communityRating > 0) {
						if (yearAndRuntime.isNotEmpty()) {
							Text(text = " • ", style = metadataStyle)
						}
						Row(
							horizontalArrangement = Arrangement.spacedBy(4.dp),
							verticalAlignment = Alignment.CenterVertically
						) {
							Icon(
								painter = painterResource(id = org.jellyfin.androidtv.R.drawable.ic_star),
								contentDescription = null,
								tint = Color.Unspecified,
								modifier = Modifier.size(18.dp)
							)
							Text(text = String.format("%.1f", communityRating), style = metadataStyle)
						}
					}
				}

				item.criticRating?.let { criticRating ->
					if (criticRating > 0) {
						if (yearAndRuntime.isNotEmpty() || (item.communityRating ?: 0f) > 0) {
							Text(text = " • ", style = metadataStyle)
						}
						Row(
							horizontalArrangement = Arrangement.spacedBy(4.dp),
							verticalAlignment = Alignment.CenterVertically
						) {
							val tomatoDrawable = if (criticRating >= 60f) {
								org.jellyfin.androidtv.R.drawable.ic_rt_fresh
							} else {
								org.jellyfin.androidtv.R.drawable.ic_rt_rotten
							}
							Icon(
								painter = painterResource(id = tomatoDrawable),
								contentDescription = null,
								tint = Color.Unspecified,
								modifier = Modifier.size(16.dp)
							)
							Text(
								text = "${String.format("%.0f", criticRating)}%",
								style = metadataStyle
							)
						}
					}
				}

				item.parentalRating?.let { parentalRating ->
					if (parentalRating.isNotBlank()) {
						if (yearAndRuntime.isNotEmpty() || (item.communityRating ?: 0f) > 0 || (item.criticRating ?: 0f) > 0) {
							Text(text = " • ", style = metadataStyle)
						}
						Text(text = parentalRating, style = metadataStyle)
					}
				}
			}

			// Description
			if (item.description.isNotBlank()) {
				Text(
					text = item.description,
					style = typography.bodyLarge.copy(
						color = colors.textOnMedia,
						shadow = Shadow(
							color = Color.Black.copy(alpha = 0.5f),
							offset = Offset(x = 1f, y = 2f),
							blurRadius = 4f
						)
					),
					maxLines = 3,
					overflow = TextOverflow.Ellipsis,
					modifier = Modifier
						.padding(top = 10.dp)
						.fillMaxWidth(0.5f)
				)
			}
		}

		// Watch Now button
		Box(
			modifier = Modifier
				.align(Alignment.BottomStart)
				.padding(start = 48.dp, bottom = 16.dp)
		) {
			WatchNowButton(onItemSelected = onItemSelected)
		}
	}
}

@Composable
private fun CarouselItemBackground(item: CarouselItem, modifier: Modifier = Modifier) {
	val imageUrl = item.backdropUrl ?: item.imageUrl
	val colors = JellyfinTheme.colorScheme

	Box(modifier = modifier.fillMaxSize()) {
		// Full-bleed backdrop image
		AsyncImage(
			modifier = Modifier.fillMaxSize(),
			url = imageUrl,
			scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
		)

		// Left gradient for text readability (black 40% → transparent)
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(
					brush = Brush.horizontalGradient(
						0f to colors.background.copy(alpha = 0.95f),
						0.35f to colors.background.copy(alpha = 0.7f),
						0.55f to Color.Transparent,
						1f to Color.Transparent
					)
				)
		)

		// Bottom scrim for indicator and button area
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(
					brush = Brush.verticalGradient(
						0f to Color.Transparent,
						0.6f to Color.Transparent,
						0.85f to colors.background.copy(alpha = 0.5f),
						1f to colors.background.copy(alpha = 0.85f)
					)
				)
		)
	}
}

@Composable
private fun WatchNowButton(onItemSelected: () -> Unit) {
	val buttonFocusRequester = remember { FocusRequester() }
	val typography = JellyfinTheme.typography

	Button(
		onClick = onItemSelected,
		modifier = Modifier
			.focusRequester(buttonFocusRequester),
		contentPadding = androidx.compose.foundation.layout.PaddingValues(
			start = 16.dp,
			end = 24.dp,
			top = 10.dp,
			bottom = 10.dp
		),
		shape = ButtonDefaults.shape(shape = RoundedCornerShape(8.dp)),
		colors = ButtonDefaults.colors(
			containerColor = Color(0xFFAA5CC3),
			contentColor = Color.White,
			focusedContainerColor = Color(0xFFCC5CC3),
			focusedContentColor = Color.White,
		),
		scale = ButtonDefaults.scale(scale = 1.0f, focusedScale = 1.03f),
		glow = ButtonDefaults.glow(
			glow = androidx.tv.material3.Glow(
				elevationColor = Color(0x4DAA5CC3),
				elevation = 8.dp
			),
			focusedGlow = androidx.tv.material3.Glow(
				elevationColor = Color(0x80AA5CC3),
				elevation = 16.dp
			)
		)
	) {
		Icon(
			imageVector = Icons.Outlined.PlayArrow,
			contentDescription = null,
			modifier = Modifier.size(24.dp)
		)
		Spacer(Modifier.width(8.dp))
		Text(
			text = "Watch Now",
			style = typography.button.copy(fontSize = 16.sp)
		)
	}
}
