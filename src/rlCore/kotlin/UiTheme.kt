@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package org.bljw.kaylib

import kotlinx.cinterop.CValue
import kotlinx.cinterop.cValue
import rl.Color

object UiTheme {
    const val WindowWidth = 800
    const val WindowHeight = 600
    const val TargetFps = 60
    const val TitleFontSize = 32
    const val TextFontSize = 20
    const val TextLineHeight = 28
    const val TextX = 48
    const val MovementColorIntensity = 200
    const val MenuItemHeight = 48
    const val MenuItemPaddingX = 24
    const val RebindingText = "Press any key or mouse button to bind Primary"

    fun white(): CValue<Color> = rgba(255, 255, 255)

    fun black(): CValue<Color> = rgba(0, 0, 0)

    fun hintColor(): CValue<Color> = rgba(180, 180, 180)

    fun green(): CValue<Color> = rgba(120, 220, 140)

    fun red(): CValue<Color> = rgba(240, 100, 100)

    fun overlay(): CValue<Color> = rgba(0, 0, 0, 180)

    fun menuHighlight(): CValue<Color> = rgba(50, 50, 70)

    fun movementBackground(
        r: Int,
        g: Int,
        b: Int,
    ): CValue<Color> =
        rgba(
            r.coerceIn(0, 255),
            g.coerceIn(0, 255),
            b.coerceIn(0, 255),
        )

    private fun rgba(
        r: Int,
        g: Int,
        b: Int,
        a: Int = 255,
    ): CValue<Color> =
        cValue {
            this.r = r.toUByte()
            this.g = g.toUByte()
            this.b = b.toUByte()
            this.a = a.toUByte()
        }
}
