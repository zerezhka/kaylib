@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package org.bljw.kaylib.ui

import kotlinx.cinterop.CValue
import kotlinx.cinterop.cValue
import kotlinx.cinterop.useContents
import org.bljw.kaylib.MoveDownAction
import org.bljw.kaylib.MoveUpAction
import org.bljw.kaylib.PrimaryAction
import org.bljw.kaylib.UiTheme
import org.bljw.kaylib.input.InputSystem
import org.bljw.kaylib.checkCollision
import org.bljw.kaylib.drawRect
import org.bljw.kaylib.drawText
import org.bljw.kaylib.measureText
import org.bljw.kaylib.mousePos
import rl.Color
import rl.Rectangle

class VerticalMenu(
    private val items: List<String>,
    private val centerX: Int,
    private val startY: Int,
) {
    var selectedIndex = 0
        private set

    private var hoveredIndex: Int? = null

    fun update(input: InputSystem): Int? {
        if (input.wasPressed(MoveUpAction)) {
            selectedIndex = (selectedIndex - 1).mod(items.size)
        }
        if (input.wasPressed(MoveDownAction)) {
            selectedIndex = (selectedIndex + 1).mod(items.size)
        }

        hoveredIndex = itemIndexAtMouse()

        val activateIndex = when {
            input.wasPressed(PrimaryAction) && hoveredIndex != null -> hoveredIndex
            input.wasPressed(PrimaryAction) -> selectedIndex
            else -> null
        }

        return activateIndex
    }

    fun draw() {
        items.forEachIndexed { index, label ->
            val bounds = itemBounds(index)
            val isSelected = index == selectedIndex
            val isHovered = index == hoveredIndex

            if (isSelected || isHovered) {
                drawRect(bounds, UiTheme.menuHighlight())
            }

            val textWidth = measureText(label, UiTheme.TextFontSize)
            bounds.useContents {
                val textX = centerX - textWidth / 2
                val textY = y.toInt() + (height.toInt() - UiTheme.TextFontSize) / 2
                val textColor = if (isSelected || isHovered) UiTheme.green() else UiTheme.white()
                drawText(label, textX, textY, UiTheme.TextFontSize, textColor)
            }
        }
    }

    private fun itemBounds(index: Int): CValue<Rectangle> {
        val label = items[index]
        val textWidth = measureText(label, UiTheme.TextFontSize)
        val width = textWidth + UiTheme.MenuItemPaddingX * 2
        val height = UiTheme.MenuItemHeight
        val x = centerX - width / 2f
        val y = (startY + index * height).toFloat()

        return cValue {
            this.x = x
            this.y = y
            this.width = width.toFloat()
            this.height = height.toFloat()
        }
    }

    private fun itemIndexAtMouse(): Int? {
        val mouse = mousePos()
        return items.indices.firstOrNull { index ->
            checkCollision(mouse, itemBounds(index))
        }
    }
}
