@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package org.bljw.kaylib

import org.bljw.kaylib.input.RaylibInputSource
import org.bljw.kaylib.ui.DebugOverlay
import org.bljw.kaylib.screens.ExitConfirmScreen
import org.bljw.kaylib.screens.GameScreen
import org.bljw.kaylib.screens.MainMenuScreen
import org.bljw.kaylib.screens.Screen
import org.bljw.kaylib.screens.SettingsScreen
import rl.KEY_NULL
import rl.RAYLIB_VERSION

fun runRaylibApp() {
    initWindow(UiTheme.WindowWidth, UiTheme.WindowHeight, "Raylib $RAYLIB_VERSION")
    setExitKey(KEY_NULL.toInt())
    setTargetFps(UiTheme.TargetFps)

    val inputSource = RaylibInputSource()
    val input = createInputSystem(inputSource)

    var shouldQuit = false

    val mainMenuScreen = MainMenuScreen()
    val gameScreen = GameScreen(input)
    val settingsScreen = SettingsScreen(input, inputSource)
    val exitConfirmScreen = ExitConfirmScreen(onQuit = { shouldQuit = true })

    val screens: Map<AppScreen, Screen> = mapOf(
        AppScreen.MainMenu to mainMenuScreen,
        AppScreen.Game to gameScreen,
        AppScreen.Settings to settingsScreen,
        AppScreen.ExitConfirm to exitConfirmScreen,
    )

    var screen = AppScreen.MainMenu

    try {
        while (!windowShouldClose() && !shouldQuit) {
            input.update()

            screen = screens.getValue(screen).update(input) ?: screen

            beginDrawing()
            clearBackground(if (screen == AppScreen.Game) gameScreen.background() else UiTheme.black())
            if (screen == AppScreen.ExitConfirm) mainMenuScreen.draw()
            screens.getValue(screen).draw()
            DebugOverlay.drawIfEnabled()
            endDrawing()
        }
    } finally {
        closeWindow()
    }
}
