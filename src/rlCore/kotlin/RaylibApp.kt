@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package org.bljw.kaylib

import org.bljw.kaylib.input.RaylibInputSource
import org.bljw.kaylib.ui.DebugOverlay
import org.bljw.kaylib.screens.ExitConfirmScreen
import org.bljw.kaylib.screens.GameScreen
import org.bljw.kaylib.screens.MainMenuScreen
import org.bljw.kaylib.screens.SettingsScreen
import rl.KEY_NULL
import rl.RAYLIB_VERSION

fun runRaylibApp() {
    initWindow(UiTheme.WindowWidth, UiTheme.WindowHeight, "Raylib $RAYLIB_VERSION")
    setExitKey(KEY_NULL.toInt())
    setTargetFps(UiTheme.TargetFps)

    val inputSource = RaylibInputSource()
    val input = createInputSystem(inputSource)

    val mainMenuScreen = MainMenuScreen()
    val gameScreen = GameScreen()
    val settingsScreen = SettingsScreen()
    val exitConfirmScreen = ExitConfirmScreen()

    var screen = AppScreen.MainMenu
    var shouldQuit = false

    try {
        while (!windowShouldClose() && !shouldQuit) {
            input.update()

            screen =
                when (screen) {
                    AppScreen.MainMenu -> mainMenuScreen.update(input) ?: screen
                    AppScreen.Game -> gameScreen.update(input) ?: screen
                    AppScreen.Settings -> settingsScreen.update(input, inputSource) ?: screen
                    AppScreen.ExitConfirm ->
                        exitConfirmScreen.update(input) { shouldQuit = true } ?: screen
                }

            beginDrawing()
            when (screen) {
                AppScreen.MainMenu -> {
                    clearBackground(UiTheme.black())
                    mainMenuScreen.draw()
                }
                AppScreen.Game -> {
                    clearBackground(gameScreen.drawBackground(input))
                    gameScreen.draw(input)
                }
                AppScreen.Settings -> {
                    clearBackground(UiTheme.black())
                    settingsScreen.draw(input)
                }
                AppScreen.ExitConfirm -> {
                    clearBackground(UiTheme.black())
                    mainMenuScreen.draw()
                    exitConfirmScreen.draw()
                }
            }
            DebugOverlay.drawIfEnabled()
            endDrawing()
        }
    } finally {
        closeWindow()
    }
}
