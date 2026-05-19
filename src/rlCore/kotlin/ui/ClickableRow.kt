@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package org.bljw.kaylib.ui

import kotlinx.cinterop.CValue
import kotlinx.cinterop.cValue
import org.bljw.kaylib.UiTheme
import org.bljw.kaylib.checkCollision
import org.bljw.kaylib.isMouseClicked
import org.bljw.kaylib.measureText
import org.bljw.kaylib.mousePos
import rl.Rectangle

object ClickableRow {
    fun bounds(
        label: String,
        x: Int,
        y: Int,
    ): CValue<Rectangle> {
        val textWidth = measureText(label, UiTheme.TextFontSize)
        val width = textWidth + UiTheme.MenuItemPaddingX * 2
        val height = UiTheme.MenuItemHeight

        return cValue {
            this.x = x.toFloat()
            this.y = y.toFloat()
            this.width = width.toFloat()
            this.height = height.toFloat()
        }
    }

    fun isClicked(
        label: String,
        x: Int,
        y: Int,
    ): Boolean = isMouseClicked() && checkCollision(mousePos(), bounds(label, x, y))
}
