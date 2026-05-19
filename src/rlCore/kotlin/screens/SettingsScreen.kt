@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package org.bljw.kaylib.screens

import org.bljw.kaylib.AppScreen
import org.bljw.kaylib.CancelAction
import org.bljw.kaylib.MoveDownAction
import org.bljw.kaylib.MoveLeftAction
import org.bljw.kaylib.MoveRightAction
import org.bljw.kaylib.MoveUpAction
import org.bljw.kaylib.PrimaryAction
import org.bljw.kaylib.SecondaryAction
import org.bljw.kaylib.UiTheme
import org.bljw.kaylib.displayName
import org.bljw.kaylib.drawRect
import org.bljw.kaylib.drawText
import org.bljw.kaylib.input.InputActionId
import org.bljw.kaylib.input.InputBinding
import org.bljw.kaylib.input.InputSystem
import org.bljw.kaylib.input.RaylibInputSource
import org.bljw.kaylib.measureText
import org.bljw.kaylib.screenHeight
import org.bljw.kaylib.screenWidth
import org.bljw.kaylib.ui.ClickableRow

class SettingsScreen(
    private val input: InputSystem,
    private val inputSource: RaylibInputSource,
) : Screen {
    private var rebindingAction: InputActionId? = null
    private var releaseBeforeCapture: InputActionId? = null
    private var canCapture = false

    private val remappableActions = listOf(
        MoveUpAction,
        MoveDownAction,
        MoveLeftAction,
        MoveRightAction,
        PrimaryAction,
        SecondaryAction,
    )

    override fun update(input: InputSystem): AppScreen? {
        if (input.wasPressed(CancelAction)) {
            if (rebindingAction != null) {
                stopRebinding()
                return null
            }
            return AppScreen.MainMenu
        }

        val layout = layout()

        if (rebindingAction == null) {
            for ((action, y) in layout.actionRows) {
                if (ClickableRow.isClicked(layout.label(action), UiTheme.TextX, y)) {
                    startRebinding(action)
                    break
                }
            }
        } else {
            val action = rebindingAction!!
            if (canCapture) {
                inputSource.captureNextControl()?.let { control ->
                    input.replaceBinding(id = action, binding = InputBinding(control))
                    stopRebinding()
                }
            } else if (releaseBeforeCapture != null && !input.isDown(releaseBeforeCapture!!)) {
                canCapture = true
            }
        }

        return null
    }

    override fun draw() {
        val layout = layout()

        val title = "Settings"
        val titleWidth = measureText(title, UiTheme.TitleFontSize)
        drawText(title, (screenWidth() - titleWidth) / 2, 40, UiTheme.TitleFontSize, UiTheme.white())

        drawText("Controls", UiTheme.TextX, layout.controlsHeaderY, UiTheme.TextFontSize, UiTheme.hintColor())

        for ((action, y) in layout.actionRows) {
            val label = layout.label(action)
            val highlighted = rebindingAction == action
            drawRow(label, y, highlighted)
        }

        val hintY = layout.hintY
        if (rebindingAction != null) {
            drawText(UiTheme.RebindingText, UiTheme.TextX, hintY, UiTheme.TextFontSize, UiTheme.green())
        } else {
            drawText("Click a binding to remap", UiTheme.TextX, hintY, UiTheme.TextFontSize, UiTheme.hintColor())
        }

        val hint = "Esc: Back"
        val hintWidth = measureText(hint, UiTheme.TextFontSize)
        drawText(hint, (screenWidth() - hintWidth) / 2, screenHeight() - UiTheme.TextFontSize - 28, UiTheme.TextFontSize, UiTheme.hintColor())
    }

    private fun drawRow(label: String, y: Int, highlighted: Boolean) {
        if (highlighted) {
            drawRect(ClickableRow.bounds(label, UiTheme.TextX, y), UiTheme.menuHighlight())
        }
        val textY = y + (UiTheme.MenuItemHeight - UiTheme.TextFontSize) / 2
        drawText(label, UiTheme.TextX, textY, UiTheme.TextFontSize, if (highlighted) UiTheme.green() else UiTheme.white())
    }

    private fun startRebinding(action: InputActionId) {
        rebindingAction = action
        releaseBeforeCapture = action
        canCapture = false
        inputSource.clearKeyboardCaptureQueue()
    }

    private fun stopRebinding() {
        rebindingAction = null
        releaseBeforeCapture = null
        canCapture = false
    }

    private fun layout(): SettingsLayout {
        var y = 112
        val controlsHeaderY = y
        y += UiTheme.TextLineHeight

        val actionRows = remappableActions.map { action ->
            val rowY = y
            y += UiTheme.TextLineHeight
            action to rowY
        }

        y += UiTheme.TextLineHeight / 2
        val hintY = y

        return SettingsLayout(controlsHeaderY = controlsHeaderY, actionRows = actionRows, hintY = hintY)
    }

    private inner class SettingsLayout(
        val controlsHeaderY: Int,
        val actionRows: List<Pair<InputActionId, Int>>,
        val hintY: Int,
    ) {
        fun label(action: InputActionId): String {
            val name = when (action) {
                MoveUpAction -> "Move Up"
                MoveDownAction -> "Move Down"
                MoveLeftAction -> "Move Left"
                MoveRightAction -> "Move Right"
                PrimaryAction -> "Primary"
                SecondaryAction -> "Secondary"
                else -> action.value
            }
            val bindings = input.bindings(action).joinToString { it.control.displayName() }
            return "$name: $bindings"
        }
    }
}
