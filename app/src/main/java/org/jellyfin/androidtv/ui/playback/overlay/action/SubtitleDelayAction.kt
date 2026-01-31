package org.jellyfin.androidtv.ui.playback.overlay.action

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import org.jellyfin.androidtv.R
import org.jellyfin.androidtv.ui.playback.PlaybackController
import org.jellyfin.androidtv.ui.playback.overlay.CustomPlaybackTransportControlGlue
import org.jellyfin.androidtv.ui.playback.overlay.VideoPlayerAdapter
import java.util.Locale

class SubtitleDelayAction(
	context: Context,
	customPlaybackTransportControlGlue: CustomPlaybackTransportControlGlue,
) : CustomAction(context, customPlaybackTransportControlGlue) {
	private var popup: PopupMenu? = null

	companion object {
		// Delay options in milliseconds
		private val DELAY_OPTIONS = longArrayOf(
			-5000, -2000, -1000, -500, -250,
			0,
			250, 500, 1000, 2000, 5000
		)
	}

	init {
		initializeWithIcon(R.drawable.ic_time)
	}

	override fun handleClickAction(
		playbackController: PlaybackController,
		videoPlayerAdapter: VideoPlayerAdapter,
		context: Context,
		view: View,
	) {
		videoPlayerAdapter.leanbackOverlayFragment.setFading(false)
		val currentDelay = playbackController.subtitleDelayMs

		dismissPopup()
		val themedContext = ContextThemeWrapper(context, R.style.PopupMenuCompact)
		popup = PopupMenu(themedContext, view, Gravity.END).apply {
			for ((index, delayMs) in DELAY_OPTIONS.withIndex()) {
				val label = when {
					delayMs == 0L -> context.getString(R.string.lbl_none)
					delayMs > 0 -> String.format(Locale.US, "+%.1fs", delayMs / 1000.0)
					else -> String.format(Locale.US, "%.1fs", delayMs / 1000.0)
				}
				menu.add(0, index, index, label).apply {
					isChecked = currentDelay == delayMs
				}
			}
			menu.setGroupCheckable(0, true, true)

			setOnDismissListener {
				videoPlayerAdapter.leanbackOverlayFragment.setFading(true)
				popup = null
			}
			setOnMenuItemClickListener { item ->
				val selectedDelay = DELAY_OPTIONS[item.itemId]
				playbackController.subtitleDelayMs = selectedDelay
				true
			}
		}
		popup?.show()
	}

	fun dismissPopup() {
		popup?.dismiss()
	}
}
