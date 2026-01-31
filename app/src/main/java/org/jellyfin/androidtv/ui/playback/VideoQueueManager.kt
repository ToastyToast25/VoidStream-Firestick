package org.jellyfin.androidtv.ui.playback

import org.jellyfin.sdk.model.api.BaseItemDto

class VideoQueueManager {
	private var _currentVideoQueue: List<BaseItemDto> = emptyList()
	private var _currentMediaPosition = -1
	private var _lastPlayedAudioLanguageIsoCode: String? = null
	private var _selectedMediaSourceIndex: Int = 0
	private var _isTransitioningFromNextUp: Boolean = false
	private var _consecutiveEpisodesPlayed: Int = 0

	fun setCurrentVideoQueue(items: List<BaseItemDto>?) {
		if (items.isNullOrEmpty()) return clearVideoQueue()

		_currentVideoQueue = items.toMutableList()
		_currentMediaPosition = 0
	}

	fun getCurrentVideoQueue(): List<BaseItemDto> = _currentVideoQueue

	fun setCurrentMediaPosition(currentMediaPosition: Int) {
		if (currentMediaPosition !in 0.._currentVideoQueue.size) return

		_currentMediaPosition = currentMediaPosition
	}

	fun getCurrentMediaPosition() = _currentMediaPosition

	fun getLastPlayedAudioLanguageIsoCode(): String? {
		return _lastPlayedAudioLanguageIsoCode
	}

	fun setLastPlayedAudioLanguageIsoCode(isoCode: String?) {
		_lastPlayedAudioLanguageIsoCode = isoCode
	}

	fun getSelectedMediaSourceIndex() = _selectedMediaSourceIndex

	fun setSelectedMediaSourceIndex(index: Int) {
		_selectedMediaSourceIndex = index
	}

	var isTransitioningFromNextUp: Boolean
		get() = _isTransitioningFromNextUp
		set(value) { _isTransitioningFromNextUp = value }

	var consecutiveEpisodesPlayed: Int
		get() = _consecutiveEpisodesPlayed
		set(value) { _consecutiveEpisodesPlayed = value }

	/** When true, the next-up screen should disable auto-timer to require user confirmation */
	var isStillWatchingPause: Boolean = false

	fun resetConsecutiveCount() {
		_consecutiveEpisodesPlayed = 0
		isStillWatchingPause = false
	}

	fun clearVideoQueue() {
		_currentVideoQueue = emptyList()
		_currentMediaPosition = -1
		_lastPlayedAudioLanguageIsoCode = null
		_selectedMediaSourceIndex = 0
		_isTransitioningFromNextUp = false
		_consecutiveEpisodesPlayed = 0
	}
}
