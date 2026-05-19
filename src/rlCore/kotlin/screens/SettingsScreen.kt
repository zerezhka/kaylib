@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package org.bljw.kaylib.screens

import org.bljw.kaylib.AppScreen
import org.bljw.kaylib.CancelAction
import org.bljw.kaylib.MoveDownAction
import org.bljw.kaylib.MoveLeftAction
import org.bljw.kaylib.MoveRightAction
import org.bljw.kaylib.MoveUpAction
import org.bljw.kaylib.PrimaryAction
import org.bljw.kaylib.RebindPrimaryAction
import org.bljw.kaylib.SecondaryAction
import org.bljw.kaylib.UiTheme
import org.bljw.kaylib.displayName
import org.bljw.kaylib.input.InputBinding
import org.bljw.kaylib.input.InputSystem
import org.bljw.kaylib.input.RaylibInputSource
import org.bljw.kaylib.statusColor
import org.bljw.kaylib.statusText
import rl.DrawText
import rl.GetScreenHeight
import rl.GetScreenWidth
import rl.MeasureText

class SettingsScreen {
    private var isRebindingPrimary = false
    private var canCapturePrimary = false

    fun update(
        input: InputSystem,
        inputSource: RaylibInputSource,
    ): AppScreen? {
        if (input.wasPressed(CancelAction)) {
            isRebindingPrimary = false
            canCapturePrimary = false
            return AppScreen.MainMenu
        }

        if (isRebindingPrimary) {
            if (canCapturePrimary) {
                inputSource.captureNextControl()?.let { control ->
                    input.replaceBinding(
                        id = PrimaryAction,
                        binding = InputBinding(control),
                    )
                    isRebindingPrimary = false
                    canCapturePrimary = false
                }
            } else if (!input.isDown(RebindPrimaryAction)) {
                canCapturePrimary = true
            }
        } else if (input.wasPressed(RebindPrimaryAction)) {
            isRebindingPrimary = true
            canCapturePrimary = false
            inputSource.clearKeyboardCaptureQueue()
        }

        return null
    }

    fun draw(input: InputSystem) {
        val title = "Settings"
        val titleWidth = MeasureText(title, UiTheme.TitleFontSize)
        DrawText(title, (GetScreenWidth() - titleWidth) / 2, 40, UiTheme.TitleFontSize, UiTheme.white())

        var y = 112
        DrawText("Controls", UiTheme.TextX, y, UiTheme.TextFontSize, UiTheme.hintColor())
        y += UiTheme.TextLineHeight
        DrawText("Primary: ${input.bindings(PrimaryAction).joinToString { it.control.displayName() }}", UiTheme.TextX, y, UiTheme.TextFontSize, UiTheme.white())
        y += UiTheme.TextLineHeight
        DrawText("Secondary: ${input.bindings(SecondaryAction).joinToString { it.control.displayName() }}", UiTheme.TextX, y, UiTheme.TextFontSize, UiTheme.white())
        y += UiTheme.TextLineHeight
        DrawText("Rebind Primary: R", UiTheme.TextX, y, UiTheme.TextFontSize, UiTheme.hintColor())
        y += UiTheme.TextLineHeight * 2

        if (isRebindingPrimary) {
            DrawText(UiTheme.RebindingText, UiTheme.TextX, y, UiTheme.TextFontSize, UiTheme.green())
        } else {
            DrawText("Press R to remap Primary at runtime", UiTheme.TextX, y, UiTheme.TextFontSize, UiTheme.hintColor())
        }
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
        y += UiTheme.TextLineHeight
        DrawText("cancel: ${input.state(CancelAction).statusText()}", UiTheme.TextX, y, UiTheme.TextFontSize, input.statusColor(CancelAction))

        val hint = "Esc: Back"
        val hintWidth = MeasureText(hint, UiTheme.TextFontSize)
        DrawText(hint, (GetScreenWidth() - hintWidth) / 2, GetScreenHeight() - UiTheme.TextFontSize - 28, UiTheme.TextFontSize, UiTheme.hintColor())
    }
}
