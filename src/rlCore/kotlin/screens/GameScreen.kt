@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package org.bljw.kaylib.screens

import kotlinx.cinterop.CValue
import org.bljw.kaylib.AppScreen
import org.bljw.kaylib.CancelAction
import org.bljw.kaylib.MoveDownAction
import org.bljw.kaylib.MoveLeftAction
import org.bljw.kaylib.MoveRightAction
import org.bljw.kaylib.MoveUpAction
import org.bljw.kaylib.PrimaryAction
import org.bljw.kaylib.SecondaryAction
import org.bljw.kaylib.UiTheme
import org.bljw.kaylib.input.InputSystem
import org.bljw.kaylib.statusColor
import org.bljw.kaylib.statusText
import rl.Color
import rl.DrawText
import rl.GetScreenHeight
import rl.GetScreenWidth
import rl.MeasureText

class GameScreen {
    fun update(input: InputSystem): AppScreen? =
        if (input.wasPressed(CancelAction)) {
            AppScreen.MainMenu
        } else {
            null
        }

    fun drawBackground(input: InputSystem): CValue<Color> {
        var r = 0
        var g = 0
        var b = 0

        if (input.isDown(MoveUpAction)) {
            b += UiTheme.MovementColorIntensity
        }
        if (input.isDown(MoveDownAction)) {
            r += UiTheme.MovementColorIntensity / 2
            g += UiTheme.MovementColorIntensity / 4
        }
        if (input.isDown(MoveLeftAction)) {
            r += UiTheme.MovementColorIntensity
        }
        if (input.isDown(MoveRightAction)) {
            g += UiTheme.MovementColorIntensity
        }

        return UiTheme.movementBackground(r, g, b)
    }

    fun draw(input: InputSystem) {
        val title = "New Game"
        val titleWidth = MeasureText(title, UiTheme.TitleFontSize)
        DrawText(title, (GetScreenWidth() - titleWidth) / 2, 40, UiTheme.TitleFontSize, UiTheme.white())

        var y = 112
        DrawText("WASD changes background color", UiTheme.TextX, y, UiTheme.TextFontSize, UiTheme.hintColor())
        y += UiTheme.TextLineHeight
        DrawText("Move: WASD or arrows", UiTheme.TextX, y, UiTheme.TextFontSize, UiTheme.hintColor())
        y += UiTheme.TextLineHeight * 2

        DrawText("moveUp: ${input.state(MoveUpAction).statusText()}", UiTheme.TextX, y, UiTheme.TextFontSize, input.statusColor(MoveUpAction))
        y += UiTheme.TextLineHeight
        DrawText("moveDown: ${input.state(MoveDownAction).statusText()}", UiTheme.TextX, y, UiTheme.TextFontSize, input.statusColor(MoveDownAction))
        y += UiTheme.TextLineHeight
        DrawText("moveLeft: ${input.state(MoveLeftAction).statusText()}", UiTheme.TextX, y, UiTheme.TextFontSize, input.statusColor(MoveLeftAction))
        y += UiTheme.TextLineHeight
        DrawText("moveRight: ${input.state(MoveRightAction).statusText()}", UiTheme.TextX, y, UiTheme.TextFontSize, input.statusColor(MoveRightAction))
        y += UiTheme.TextLineHeight
        DrawText("primary: ${input.state(PrimaryAction).statusText()}", UiTheme.TextX, y, UiTheme.TextFontSize, input.statusColor(PrimaryAction))
        y += UiTheme.TextLineHeight
        DrawText("secondary: ${input.state(SecondaryAction).statusText()}", UiTheme.TextX, y, UiTheme.TextFontSize, input.statusColor(SecondaryAction))

        val hint = "Esc: Main Menu"
        val hintWidth = MeasureText(hint, UiTheme.TextFontSize)
        DrawText(hint, (GetScreenWidth() - hintWidth) / 2, GetScreenHeight() - UiTheme.TextFontSize - 28, UiTheme.TextFontSize, UiTheme.hintColor())
    }
}
