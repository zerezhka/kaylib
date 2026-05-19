@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package org.bljw.kaylib.screens

import org.bljw.kaylib.AppScreen
import org.bljw.kaylib.CancelAction
import org.bljw.kaylib.UiTheme
import org.bljw.kaylib.input.InputSystem
import org.bljw.kaylib.ui.VerticalMenu
import org.bljw.kaylib.drawRect
import org.bljw.kaylib.drawText
import org.bljw.kaylib.measureText
import org.bljw.kaylib.screenHeight
import org.bljw.kaylib.screenWidth

class ExitConfirmScreen {
    private val menu =
        VerticalMenu(
            items =
                listOf(
                    "Yes",
                    "No",
                ),
            centerX = UiTheme.WindowWidth / 2,
            startY = 320,
        )

    fun update(
        input: InputSystem,
        onQuit: () -> Unit,
    ): AppScreen? {
        if (input.wasPressed(CancelAction)) {
            return AppScreen.MainMenu
        }

        return when (menu.update(input)) {
            0 -> {
                onQuit()
                null
            }
            1 -> AppScreen.MainMenu
            else -> null
        }
    }

    fun draw() {
        drawRect(0, 0, screenWidth(), screenHeight(), UiTheme.overlay())

        val message = "Exit game?"
        val messageWidth = measureText(message, UiTheme.TitleFontSize)
        drawText(message, (screenWidth() - messageWidth) / 2, 220, UiTheme.TitleFontSize, UiTheme.white())
        menu.draw()
    }
}
