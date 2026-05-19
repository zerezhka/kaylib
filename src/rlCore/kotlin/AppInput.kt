@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package org.bljw.kaylib

import org.bljw.kaylib.input.InputActionId
import org.bljw.kaylib.input.InputBinding
import org.bljw.kaylib.input.InputControl
import org.bljw.kaylib.input.InputSystem
import org.bljw.kaylib.input.RaylibInputSource
import rl.KEY_A
import rl.KEY_D
import rl.KEY_DOWN
import rl.KEY_ESCAPE
import rl.KEY_LEFT
import rl.KEY_RIGHT
import rl.KEY_S
import rl.KEY_SPACE
import rl.KEY_UP
import rl.KEY_W
import rl.MOUSE_BUTTON_LEFT
import rl.MOUSE_BUTTON_MIDDLE
import rl.MOUSE_BUTTON_RIGHT

val MoveUpAction = InputActionId("moveUp")
val MoveDownAction = InputActionId("moveDown")
val MoveLeftAction = InputActionId("moveLeft")
val MoveRightAction = InputActionId("moveRight")
val PrimaryAction = InputActionId("primary")
val SecondaryAction = InputActionId("secondary")
val CancelAction = InputActionId("cancel")

fun createInputSystem(inputSource: RaylibInputSource): InputSystem =
    InputSystem(inputSource).apply {
        addAction(
            id = MoveUpAction,
            bindings =
                listOf(
                    key(KEY_W),
                    key(KEY_UP),
                ),
        )
        addAction(
            id = MoveDownAction,
            bindings =
                listOf(
                    key(KEY_S),
                    key(KEY_DOWN),
                ),
        )
        addAction(
            id = MoveLeftAction,
            bindings =
                listOf(
                    key(KEY_A),
                    key(KEY_LEFT),
                ),
        )
        addAction(
            id = MoveRightAction,
            bindings =
                listOf(
                    key(KEY_D),
                    key(KEY_RIGHT),
                ),
        )
        addAction(
            id = PrimaryAction,
            bindings =
                listOf(
                    mouseButton(MOUSE_BUTTON_LEFT),
                    key(KEY_SPACE),
                ),
        )
        addAction(
            id = SecondaryAction,
            bindings =
                listOf(
                    mouseButton(MOUSE_BUTTON_RIGHT),
                ),
        )
        addAction(
            id = CancelAction,
            bindings =
                listOf(
                    key(KEY_ESCAPE),
                ),
        )
    }

private fun key(code: UInt): InputBinding =
    InputBinding(InputControl.KeyboardKey(code.toInt()))

private fun mouseButton(code: UInt): InputBinding =
    InputBinding(InputControl.MouseButton(code.toInt()))

fun InputControl.displayName(): String =
    when (this) {
        is InputControl.KeyboardKey -> keyDisplayName(code)
        is InputControl.MouseButton ->
            when (code) {
                MOUSE_BUTTON_LEFT.toInt() -> "LMB"
                MOUSE_BUTTON_RIGHT.toInt() -> "RMB"
                MOUSE_BUTTON_MIDDLE.toInt() -> "MMB"
                else -> "Mouse $code"
            }
        is InputControl.GamepadButton -> "GP$gamepad B$code"
        is InputControl.GamepadAxis -> "GP$gamepad Axis$code"
        is InputControl.VirtualButton -> "Virt$code"
    }

private fun keyDisplayName(code: Int): String =
    when (code) {
        in 65..90 -> code.toChar().toString()
        in 48..57 -> code.toChar().toString()
        32 -> "Space"
        256 -> "Escape"
        257 -> "Enter"
        258 -> "Tab"
        259 -> "Backspace"
        260 -> "Insert"
        261 -> "Delete"
        262 -> "Right"
        263 -> "Left"
        264 -> "Down"
        265 -> "Up"
        266 -> "PgUp"
        267 -> "PgDn"
        268 -> "Home"
        269 -> "End"
        280 -> "CapsLock"
        in 290..301 -> "F${code - 289}"
        340 -> "LShift"
        341 -> "LCtrl"
        342 -> "LAlt"
        344 -> "RShift"
        345 -> "RCtrl"
        346 -> "RAlt"
        in 320..329 -> "KP${code - 320}"
        330 -> "KP."
        334 -> "KP+"
        333 -> "KP-"
        332 -> "KP*"
        331 -> "KP/"
        335 -> "KPEnter"
        else -> "Key$code"
    }
