@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package org.bljw.kaylib.screens

import org.bljw.kaylib.AppScreen
import org.bljw.kaylib.CancelAction
import org.bljw.kaylib.UiTheme
import org.bljw.kaylib.input.InputSystem
import org.bljw.kaylib.ui.VerticalMenu
import rl.DrawRectangle
import rl.DrawText
import rl.GetScreenHeight
import rl.GetScreenWidth
import rl.MeasureText

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
        DrawRectangle(0, 0, GetScreenWidth(), GetScreenHeight(), UiTheme.overlay())

        val message = "Exit game?"
        val messageWidth = MeasureText(message, UiTheme.TitleFontSize)
        DrawText(
            message,
            (GetScreenWidth() - messageWidth) / 2,
            220,
            UiTheme.TitleFontSize,
            UiTheme.white(),
        )
        menu.draw()
    }
}
