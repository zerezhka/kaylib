@file:OptIn(
    kotlinx.cinterop.ExperimentalForeignApi::class,
    kotlin.experimental.ExperimentalNativeApi::class,
)

package org.bljw.kaylib.ui

import kotlin.native.Platform
import org.bljw.kaylib.UiTheme
import org.bljw.kaylib.drawText
import org.bljw.kaylib.fps
import org.bljw.kaylib.frameTime

private const val OverlayX = 8
private const val OverlayY = 8

object DebugOverlay {
    fun drawIfEnabled() {
        if (!Platform.isDebugBinary) {
            return
        }

        val fps = fps()
        val frameTimeMs = frameTime() * 1000f
        val frameTimeRounded = (frameTimeMs * 10f).toInt() / 10f

        drawText("FPS: $fps", OverlayX, OverlayY, UiTheme.TextFontSize, UiTheme.hintColor())
        drawText("${frameTimeRounded} ms", OverlayX, OverlayY + UiTheme.TextLineHeight, UiTheme.TextFontSize, UiTheme.hintColor())
    }
}
