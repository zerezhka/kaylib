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
import rl.KEY_R
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
val RebindPrimaryAction = InputActionId("rebindPrimary")

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
        addAction(
            id = RebindPrimaryAction,
            bindings =
                listOf(
                    key(KEY_R),
                ),
        )
    }

private fun key(code: UInt): InputBinding =
    InputBinding(InputControl.KeyboardKey(code.toInt()))

private fun mouseButton(code: UInt): InputBinding =
    InputBinding(InputControl.MouseButton(code.toInt()))

fun InputControl.displayName(): String =
    when (this) {
        is InputControl.KeyboardKey ->
            when (code) {
                KEY_A.toInt() -> "A"
                KEY_D.toInt() -> "D"
                KEY_DOWN.toInt() -> "Down"
                KEY_ESCAPE.toInt() -> "Escape"
                KEY_LEFT.toInt() -> "Left"
                KEY_R.toInt() -> "R"
                KEY_RIGHT.toInt() -> "Right"
                KEY_S.toInt() -> "S"
                KEY_SPACE.toInt() -> "Space"
                KEY_UP.toInt() -> "Up"
                KEY_W.toInt() -> "W"
                else -> "Key $code"
            }

        is InputControl.MouseButton ->
            when (code) {
                MOUSE_BUTTON_LEFT.toInt() -> "Left Mouse"
                MOUSE_BUTTON_RIGHT.toInt() -> "Right Mouse"
                MOUSE_BUTTON_MIDDLE.toInt() -> "Middle Mouse"
                else -> "Mouse $code"
            }

        is InputControl.GamepadButton -> "Gamepad $gamepad Button $code"
        is InputControl.GamepadAxis -> "Gamepad $gamepad Axis $code"
        is InputControl.VirtualButton -> "Virtual Button $code"
    }
