@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package org.bljw.kaylib

import org.bljw.kaylib.input.RaylibInputSource
import org.bljw.kaylib.ui.DebugOverlay
import org.bljw.kaylib.screens.ExitConfirmScreen
import org.bljw.kaylib.screens.GameScreen
import org.bljw.kaylib.screens.MainMenuScreen
import org.bljw.kaylib.screens.SettingsScreen
import rl.BeginDrawing
import rl.ClearBackground
import rl.CloseWindow
import rl.EndDrawing
import rl.InitWindow
import rl.KEY_NULL
import rl.RAYLIB_VERSION
import rl.SetExitKey
import rl.SetTargetFPS
import rl.WindowShouldClose

fun runRaylibApp() {
    InitWindow(UiTheme.WindowWidth, UiTheme.WindowHeight, "Raylib $RAYLIB_VERSION")
    SetExitKey(KEY_NULL.toInt())
    SetTargetFPS(UiTheme.TargetFps)

    val inputSource = RaylibInputSource()
    val input = createInputSystem(inputSource)

    val mainMenuScreen = MainMenuScreen()
    val gameScreen = GameScreen()
    val settingsScreen = SettingsScreen()
    val exitConfirmScreen = ExitConfirmScreen()

    var screen = AppScreen.MainMenu
    var shouldQuit = false

    try {
        while (!WindowShouldClose() && !shouldQuit) {
            input.update()

            screen =
                when (screen) {
                    AppScreen.MainMenu -> mainMenuScreen.update(input) ?: screen
                    AppScreen.Game -> gameScreen.update(input) ?: screen
                    AppScreen.Settings -> settingsScreen.update(input, inputSource) ?: screen
                    AppScreen.ExitConfirm ->
                        exitConfirmScreen.update(input) { shouldQuit = true } ?: screen
                }

            BeginDrawing()
            when (screen) {
                AppScreen.MainMenu -> {
                    ClearBackground(UiTheme.black())
                    mainMenuScreen.draw()
                }
                AppScreen.Game -> {
                    ClearBackground(gameScreen.drawBackground(input))
                    gameScreen.draw(input)
                }
                AppScreen.Settings -> {
                    ClearBackground(UiTheme.black())
                    settingsScreen.draw(input)
                }
                AppScreen.ExitConfirm -> {
                    ClearBackground(UiTheme.black())
                    mainMenuScreen.draw()
                    exitConfirmScreen.draw()
                }
            }
            DebugOverlay.drawIfEnabled()
            EndDrawing()
        }
    } finally {
        CloseWindow()
    }
}
