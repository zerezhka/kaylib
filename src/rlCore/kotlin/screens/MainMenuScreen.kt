@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package org.bljw.kaylib.screens

import org.bljw.kaylib.AppScreen
import org.bljw.kaylib.UiTheme
import org.bljw.kaylib.input.InputSystem
import org.bljw.kaylib.ui.VerticalMenu
import org.bljw.kaylib.drawText
import org.bljw.kaylib.measureText
import org.bljw.kaylib.screenWidth

class MainMenuScreen : Screen {
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

    override fun update(input: InputSystem): AppScreen? =
        when (menu.update(input)) {
            0 -> AppScreen.Game
            1 -> AppScreen.Settings
            2 -> AppScreen.ExitConfirm
            else -> null
        }

    override fun draw() {
        val title = "Kaylib"
        val titleWidth = measureText(title, UiTheme.TitleFontSize)
        drawText(title, (screenWidth() - titleWidth) / 2, 120, UiTheme.TitleFontSize, UiTheme.white())
        menu.draw()
    }
}
