@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package org.bljw.kaylib.screens

import org.bljw.kaylib.AppScreen
import org.bljw.kaylib.UiTheme
import org.bljw.kaylib.input.InputSystem
import org.bljw.kaylib.ui.VerticalMenu
import rl.DrawText
import rl.GetScreenWidth
import rl.MeasureText

class MainMenuScreen {
    private val menu =
        VerticalMenu(
            items =
                listOf(
                    "New Game",
                    "Settings",
                    "Exit",
                ),
            centerX = UiTheme.WindowWidth / 2,
            startY = 280,
        )

    fun update(input: InputSystem): AppScreen? =
        when (menu.update(input)) {
            0 -> AppScreen.Game
            1 -> AppScreen.Settings
            2 -> AppScreen.ExitConfirm
            else -> null
        }

    fun draw() {
        val title = "Kaylib"
        val titleWidth = MeasureText(title, UiTheme.TitleFontSize)
        DrawText(title, (GetScreenWidth() - titleWidth) / 2, 120, UiTheme.TitleFontSize, UiTheme.white())
        menu.draw()
    }
}
