@file:OptIn(
    kotlinx.cinterop.ExperimentalForeignApi::class,
    kotlin.experimental.ExperimentalNativeApi::class,
)

package org.bljw.kaylib.ui

import kotlin.native.Platform
import org.bljw.kaylib.UiTheme
import rl.DrawText
import rl.GetFPS
import rl.GetFrameTime

private const val OverlayX = 8
private const val OverlayY = 8

object DebugOverlay {
    fun drawIfEnabled() {
        if (!Platform.isDebugBinary) {
            return
        }

        val fps = GetFPS()
        val frameTimeMs = GetFrameTime() * 1000f
        val frameTimeRounded = (frameTimeMs * 10f).toInt() / 10f

        DrawText("FPS: $fps", OverlayX, OverlayY, UiTheme.TextFontSize, UiTheme.hintColor())
        DrawText(
            "${frameTimeRounded} ms",
            OverlayX,
            OverlayY + UiTheme.TextLineHeight,
            UiTheme.TextFontSize,
            UiTheme.hintColor(),
        )
    }
}
