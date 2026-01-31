package org.jellyfin.androidtv.ui.playback

import androidx.annotation.OptIn
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.C
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.common.util.UnstableApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jellyfin.androidtv.ui.playback.segment.MediaSegmentAction
import org.jellyfin.androidtv.ui.playback.segment.MediaSegmentRepository
import org.jellyfin.androidtv.util.sdk.end
import org.jellyfin.androidtv.util.sdk.start
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.api.client.extensions.liveTvApi
import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.MediaSegmentDto
import org.jellyfin.sdk.model.api.MediaSegmentType
import org.jellyfin.sdk.model.api.MediaStreamType
import org.jellyfin.sdk.model.api.SubtitleDeliveryMethod
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.UUID

fun PlaybackController.getLiveTvChannel(
	id: UUID,
	callback: (channel: BaseItemDto) -> Unit,
) {
	val api by fragment.inject<ApiClient>()

	fragment.lifecycleScope.launch {
		runCatching {
			withContext(Dispatchers.IO) {
				api.liveTvApi.getChannel(id).content
			}
		}.onSuccess { channel ->
			callback(channel)
		}
	}
}

@OptIn(UnstableApi::class)
@JvmOverloads
fun PlaybackController.setSubtitleIndex(index: Int, force: Boolean = false) {
	// Get current index safely
	val currentSubIndex = mCurrentOptions?.subtitleStreamIndex ?: -1

	if (!force) {
		if (currentSubIndex == index) {
			mVideoManager.mExoPlayer.trackSelector?.let { selector ->
				val params = selector.parameters
				val isTextDisabled = params.disabledTrackTypes.contains(C.TRACK_TYPE_TEXT)
				if (isTextDisabled) {
					val newParams = params.buildUpon()
						.setTrackTypeDisabled(C.TRACK_TYPE_TEXT, false)
						.build()
					selector.parameters = newParams
					// Force a player refresh to ensure subtitles are rendered
					val currentPos = mVideoManager.mExoPlayer.currentPosition
					mVideoManager.mExoPlayer.seekTo(currentPos + 1)
					mVideoManager.mExoPlayer.seekTo(currentPos)
				}
			}
			return
		}

		// If we're trying to disable subtitles that are already disabled
		if (index == -1 && currentSubIndex == -1) {
			return
		}
	}

	// Store the current position before making any changes
	val currentPosition = mCurrentPosition
	if (index == -1) {
		// If we're already disabled and not burning subs, we can return early
		if (currentSubIndex == -1 && !burningSubs) {
			return
		}

		// Update the options first
		mCurrentOptions?.subtitleStreamIndex = -1

		// Handle burning subs case
		if (burningSubs) {
			stop()
			burningSubs = false
			play(currentPosition, -1)
		} else {
			// Clear any subtitle track selection
			mVideoManager.mExoPlayer.trackSelector?.let { selector ->
				val params = selector.parameters
				val newParams = params.buildUpon()
					.clearOverridesOfType(C.TRACK_TYPE_TEXT)
					.setTrackTypeDisabled(C.TRACK_TYPE_TEXT, true)
					.build()

				// Only update if parameters actually changed
				if (params != newParams) {
					selector.parameters = newParams
				}
			}
		}
	} else {
		val mediaSource = currentMediaSource ?: return

		// Find the subtitle stream
		val stream = mediaSource.mediaStreams?.find { it.type == MediaStreamType.SUBTITLE && it.index == index } ?: return

		// If we're burning subs or the stream requires it, restart playback
		if (burningSubs || stream.deliveryMethod == SubtitleDeliveryMethod.ENCODE) {
			// Only restart if the index is actually changing or we're forcing an update
			if (mCurrentOptions.subtitleStreamIndex != index || force) {
				stop()
				burningSubs = stream.deliveryMethod == SubtitleDeliveryMethod.ENCODE
				mCurrentOptions.subtitleStreamIndex = index
				play(currentPosition, index)
			}
			return
		}

		when (stream.deliveryMethod) {
			SubtitleDeliveryMethod.EXTERNAL,
			SubtitleDeliveryMethod.EMBED,
			SubtitleDeliveryMethod.HLS -> {
				// First check if we're already on this track to avoid unnecessary processing
				val currentSubIndex = mCurrentOptions.subtitleStreamIndex ?: -1
				if (currentSubIndex == index && !force) {
					mVideoManager.mExoPlayer.trackSelector?.let { selector ->
						val params = selector.parameters
						val isTextDisabled = params.disabledTrackTypes.contains(C.TRACK_TYPE_TEXT)
						if (isTextDisabled) {
							val newParams = params.buildUpon()
								.setTrackTypeDisabled(C.TRACK_TYPE_TEXT, false)
								.build()
							selector.parameters = newParams
							// Force a player refresh to ensure subtitles are rendered
							val currentPos = mVideoManager.mExoPlayer.currentPosition
							mVideoManager.mExoPlayer.seekTo(currentPos + 1)
							mVideoManager.mExoPlayer.seekTo(currentPos)
						}
					}
					return
				}

				// External subtitles need to be resolved differently
				val group = when (stream.deliveryMethod) {
					SubtitleDeliveryMethod.EXTERNAL -> {
						mVideoManager.mExoPlayer.currentTracks.groups.firstOrNull { group ->
							// Verify this is a group with a single format (the subtitles) that is added by us.
							// Each external subtitle format id is prefixed with its source index (normally starting at 1)
							group.length == 1 && group.getTrackFormat(0).id?.endsWith(":JF_EXTERNAL:$index") == true
						}?.also {
						}
					}
					else -> {
						// The server does not send a reliable index in all cases, so calculate it manually
						val subtitleStreams = mediaSource.mediaStreams.orEmpty()
							.filter { it.type == MediaStreamType.SUBTITLE }
							.filter { it.deliveryMethod == stream.deliveryMethod } // Only match the same delivery method
							.toList()

						val localIndex = subtitleStreams.indexOfFirst { it.index == index }

						if (localIndex == -1) {
							return setSubtitleIndex(-1)
						}

						// Find the corresponding track group
						val groups = mVideoManager.mExoPlayer.currentTracks.groups
							.filter { it.type == C.TRACK_TYPE_TEXT }
							.filterNot { it.length == 1 && it.getTrackFormat(0).id?.endsWith(":JF_EXTERNAL:") == true }
							.toList()

						// Return the group at the found index or null if not found
						groups.getOrNull(localIndex)?.also {
						}
					}
				}?.mediaTrackGroup

				if (group == null) {
					Timber.w("Failed to find correct subtitle group for method ${stream.deliveryMethod}")
					return setSubtitleIndex(-1)
				}

				mCurrentOptions?.subtitleStreamIndex = index

				try {
					with(mVideoManager.mExoPlayer.trackSelector!!) {
						val currentParams = parameters
						val newParams = currentParams.buildUpon()
							.clearOverridesOfType(C.TRACK_TYPE_TEXT)
							.addOverride(TrackSelectionOverride(group, 0))
							.setTrackTypeDisabled(C.TRACK_TYPE_TEXT, false)
							.build()

						if (currentParams != newParams) {
							parameters = newParams
							// Force a player refresh to ensure subtitles are rendered
							val currentPos = mVideoManager.mExoPlayer.currentPosition
							mVideoManager.mExoPlayer.seekTo(currentPos + 1)
							mVideoManager.mExoPlayer.seekTo(currentPos)
						}
					}
				} catch (e: Exception) {
					Timber.e(e, "Error setting subtitle track")
					setSubtitleIndex(-1)
				}
			}

			SubtitleDeliveryMethod.DROP -> {
				setSubtitleIndex(-1)
			}
			null -> {
				// Handle null delivery method by trying to find the best matching track

				// Find the subtitle stream in the media source
				val subtitleStreams = mediaSource.mediaStreams.orEmpty()
					.filter { it.type == MediaStreamType.SUBTITLE }
					.toList()

				val streamIndex = subtitleStreams.indexOfFirst { it.index == index }
				if (streamIndex == -1) {
					return setSubtitleIndex(-1)
				}

				// Get all available text track groups
				val trackGroups = mVideoManager.mExoPlayer.currentTracks.groups
				val textTrackGroups = trackGroups.filter { it.type == C.TRACK_TYPE_TEXT }

				// Try to find the best matching track group
				val groupToUse = textTrackGroups.firstOrNull { group ->
					// Match by index in the track ID if possible
					group.mediaTrackGroup.getFormat(0).id?.contains("$index") == true
				}?.mediaTrackGroup ?: textTrackGroups.getOrNull(streamIndex)?.mediaTrackGroup

				if (groupToUse != null) {

					// Update current options
					mCurrentOptions?.subtitleStreamIndex = index

					// Update track selection
					val trackSelector = mVideoManager.mExoPlayer.trackSelector
					if (trackSelector != null) {
						val parameters = trackSelector.parameters
						val newParameters = parameters.buildUpon()
							.clearOverridesOfType(C.TRACK_TYPE_TEXT)
							.addOverride(TrackSelectionOverride(groupToUse, 0))
							.setTrackTypeDisabled(C.TRACK_TYPE_TEXT, false)
							.build()

						if (parameters != newParameters) {
							trackSelector.parameters = newParameters
							// Force refresh of the player
							val currentPosition = mVideoManager.mExoPlayer.currentPosition
							mVideoManager.mExoPlayer.seekTo(currentPosition + 1)
							mVideoManager.mExoPlayer.seekTo(currentPosition)
						}
					}
				} else {
					setSubtitleIndex(-1)
				}
			}

			SubtitleDeliveryMethod.ENCODE -> {
				stop()
				burningSubs = true
				mCurrentOptions.subtitleStreamIndex = index
				play(currentPosition, index)
			}
		}
	}
}

fun PlaybackController.applyMediaSegments(
	item: BaseItemDto,
	callback: () -> Unit,
) {
	val mediaSegmentRepository by fragment.inject<MediaSegmentRepository>()

	fragment.clearSkipOverlay()

	// When transitioning from next-up (binge watching), auto-skip intro segments
	// to reduce the Skip Credits → Next Episode → Skip Intro prompt chain
	val videoQueueManager by fragment.inject<VideoQueueManager>()
	val isFromNextUp = videoQueueManager.isTransitioningFromNextUp
	if (isFromNextUp) {
		videoQueueManager.isTransitioningFromNextUp = false
	}

	fragment.lifecycleScope.launch {
		val mediaSegments = runCatching {
			mediaSegmentRepository.getSegmentsForItem(item)
		}.getOrNull().orEmpty()

		for (mediaSegment in mediaSegments) {
			var action = mediaSegmentRepository.getMediaSegmentAction(mediaSegment)

			// Auto-skip intro when coming from next-up to avoid excessive prompts
			if (isFromNextUp && action == MediaSegmentAction.ASK_TO_SKIP
				&& mediaSegment.type == MediaSegmentType.INTRO) {
				action = MediaSegmentAction.SKIP
			}

			when (action) {
				MediaSegmentAction.SKIP -> addSkipAction(mediaSegment)
				MediaSegmentAction.ASK_TO_SKIP -> addAskToSkipAction(mediaSegment)
				MediaSegmentAction.NOTHING -> Unit
			}
		}

		callback()
	}
}

@OptIn(UnstableApi::class)
private fun PlaybackController.addSkipAction(mediaSegment: MediaSegmentDto) {
	mVideoManager.mExoPlayer
		.createMessage { _, _ ->
			// We can't seek directly on the ExoPlayer instance as not all media is seekable
			// the seek function in the PlaybackController checks this and optionally starts a transcode
			// at the requested position
			fragment.lifecycleScope.launch(Dispatchers.Main) {
				seek(mediaSegment.end.inWholeMilliseconds, true)
			}
		}
		// Segments at position 0 will never be hit by ExoPlayer so we need to add a minimum value
		.setPosition(mediaSegment.start.inWholeMilliseconds.coerceAtLeast(1))
		.setDeleteAfterDelivery(false)
		.send()
}

@OptIn(UnstableApi::class)
private fun PlaybackController.addAskToSkipAction(mediaSegment: MediaSegmentDto) {
	mVideoManager.mExoPlayer
		.createMessage { _, _ ->
			fragment?.askToSkip(mediaSegment.end)
		}
		// Segments at position 0 will never be hit by ExoPlayer so we need to add a minimum value
		.setPosition(mediaSegment.start.inWholeMilliseconds.coerceAtLeast(1))
		.setDeleteAfterDelivery(false)
		.send()
}
