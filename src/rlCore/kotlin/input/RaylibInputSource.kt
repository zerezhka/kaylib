@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package org.bljw.kaylib.input

import rl.GetKeyPressed
import rl.IsKeyDown
import rl.IsMouseButtonDown
import rl.IsMouseButtonPressed
import rl.KEY_NULL
import rl.MOUSE_BUTTON_BACK
import rl.MOUSE_BUTTON_EXTRA
import rl.MOUSE_BUTTON_FORWARD
import rl.MOUSE_BUTTON_LEFT
import rl.MOUSE_BUTTON_MIDDLE
import rl.MOUSE_BUTTON_RIGHT
import rl.MOUSE_BUTTON_SIDE

private val captureMouseButtons =
    listOf(
        MOUSE_BUTTON_LEFT.toInt(),
        MOUSE_BUTTON_RIGHT.toInt(),
        MOUSE_BUTTON_MIDDLE.toInt(),
        MOUSE_BUTTON_SIDE.toInt(),
        MOUSE_BUTTON_EXTRA.toInt(),
        MOUSE_BUTTON_FORWARD.toInt(),
        MOUSE_BUTTON_BACK.toInt(),
    )

class RaylibInputSource : InputSource {
    override fun read(control: InputControl): InputControlState =
        when (control) {
            is InputControl.KeyboardKey ->
                InputControlState(
                    isDown = IsKeyDown(control.code),
                )

            is InputControl.MouseButton ->
                InputControlState(
                    isDown = IsMouseButtonDown(control.code),
                )

            is InputControl.GamepadButton,
            is InputControl.GamepadAxis,
            is InputControl.VirtualButton,
            ->
                InputControlState(
                    isDown = false,
                    value = 0f,
                )
        }

    fun captureNextControl(): InputControl? {
        val key = GetKeyPressed()
        if (key != KEY_NULL.toInt()) {
            return InputControl.KeyboardKey(key)
        }

        val mouseButton = captureMouseButtons.firstOrNull(::IsMouseButtonPressed)

        return mouseButton?.let(InputControl::MouseButton)
    }

    fun clearKeyboardCaptureQueue() {
        while (GetKeyPressed() != KEY_NULL.toInt()) {
        }
    }
}
