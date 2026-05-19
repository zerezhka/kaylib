@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package org.bljw.kaylib

import kotlinx.cinterop.cValue
import org.bljw.kaylib.input.InputActionId
import org.bljw.kaylib.input.InputBinding
import org.bljw.kaylib.input.InputControl
import org.bljw.kaylib.input.InputSystem
import org.bljw.kaylib.input.RaylibInputSource
import rl.BeginDrawing
import rl.ClearBackground
import rl.CloseWindow
import rl.Color
import rl.DrawText
import rl.EndDrawing
import rl.GetScreenHeight
import rl.GetScreenWidth
import rl.InitWindow
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
import rl.MeasureText
import rl.MOUSE_BUTTON_LEFT
import rl.MOUSE_BUTTON_MIDDLE
import rl.MOUSE_BUTTON_RIGHT
import rl.RAYLIB_VERSION
import rl.SetTargetFPS
import rl.WindowShouldClose

private const val WindowWidth = 800
private const val WindowHeight = 600
private const val TargetFps = 60
private const val TitleFontSize = 32
private const val TextFontSize = 20
private const val TextLineHeight = 28
private const val TextX = 48
private const val MovementColorIntensity = 200
private const val RebindingText = "Press any key or mouse button to bind Primary"

private val MoveUpAction = InputActionId("moveUp")
private val MoveDownAction = InputActionId("moveDown")
private val MoveLeftAction = InputActionId("moveLeft")
private val MoveRightAction = InputActionId("moveRight")
private val PrimaryAction = InputActionId("primary")
private val SecondaryAction = InputActionId("secondary")
private val CancelAction = InputActionId("cancel")
private val RebindPrimaryAction = InputActionId("rebindPrimary")

fun runRaylibApp() {
    val white =
        cValue<Color> {
            r = 255.toUByte()
            g = 255.toUByte()
            b = 255.toUByte()
            a = 255.toUByte()
        }

    val black =
        cValue<Color> {
            r = 0u
            g = 0u
            b = 0u
            a = 255.toUByte()
        }

    val hintColor =
        cValue<Color> {
            r = 180.toUByte()
            g = 180.toUByte()
            b = 180.toUByte()
            a = 255.toUByte()
        }

    val green =
        cValue<Color> {
            r = 120.toUByte()
            g = 220.toUByte()
            b = 140.toUByte()
            a = 255.toUByte()
        }

    val red =
        cValue<Color> {
            r = 240.toUByte()
            g = 100.toUByte()
            b = 100.toUByte()
            a = 255.toUByte()
        }

    InitWindow(WindowWidth, WindowHeight, "Raylib $RAYLIB_VERSION")
    SetTargetFPS(TargetFps)

    val inputSource = RaylibInputSource()
    val input = createInputSystem(inputSource)
    var isRebindingPrimary = false
    var canCapturePrimary = false

    try {
        while (!WindowShouldClose()) {
            input.update()

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

            BeginDrawing()
            ClearBackground(movementBackground(input))
            val title = "Input System Foundation"
            val titleWidth = MeasureText(title, TitleFontSize)
            DrawText(title, (GetScreenWidth() - titleWidth) / 2, 40, TitleFontSize, white)

            var y = 112
            DrawText("WASD changes background color", TextX, y, TextFontSize, hintColor)
            y += TextLineHeight
            DrawText("Move: WASD or arrows", TextX, y, TextFontSize, hintColor)
            y += TextLineHeight
            DrawText("Primary: ${input.bindings(PrimaryAction).joinToString { it.control.displayName() }}", TextX, y, TextFontSize, white)
            y += TextLineHeight
            DrawText("Secondary: ${input.bindings(SecondaryAction).joinToString { it.control.displayName() }}", TextX, y, TextFontSize, white)
            y += TextLineHeight
            DrawText("Rebind Primary: R", TextX, y, TextFontSize, hintColor)
            y += TextLineHeight * 2

            if (isRebindingPrimary) {
                DrawText(RebindingText, TextX, y, TextFontSize, green)
            } else {
                DrawText("Press R to remap Primary at runtime", TextX, y, TextFontSize, hintColor)
            }
            y += TextLineHeight * 2

            DrawText("moveUp: ${input.state(MoveUpAction).statusText()}", TextX, y, TextFontSize, input.statusColor(MoveUpAction, green, hintColor))
            y += TextLineHeight
            DrawText("moveDown: ${input.state(MoveDownAction).statusText()}", TextX, y, TextFontSize, input.statusColor(MoveDownAction, green, hintColor))
            y += TextLineHeight
            DrawText("moveLeft: ${input.state(MoveLeftAction).statusText()}", TextX, y, TextFontSize, input.statusColor(MoveLeftAction, green, hintColor))
            y += TextLineHeight
            DrawText("moveRight: ${input.state(MoveRightAction).statusText()}", TextX, y, TextFontSize, input.statusColor(MoveRightAction, green, hintColor))
            y += TextLineHeight
            DrawText("primary: ${input.state(PrimaryAction).statusText()}", TextX, y, TextFontSize, input.statusColor(PrimaryAction, green, hintColor))
            y += TextLineHeight
            DrawText("secondary: ${input.state(SecondaryAction).statusText()}", TextX, y, TextFontSize, input.statusColor(SecondaryAction, green, hintColor))
            y += TextLineHeight
            DrawText("cancel: ${input.state(CancelAction).statusText()}", TextX, y, TextFontSize, input.statusColor(CancelAction, red, hintColor))

            val hint = "Esc or close to quit."
            val hintWidth = MeasureText(hint, TextFontSize)
            DrawText(hint, (GetScreenWidth() - hintWidth) / 2, GetScreenHeight() - TextFontSize - 28, TextFontSize, hintColor)
            EndDrawing()
        }
    } finally {
        CloseWindow()
    }
}

private fun createInputSystem(inputSource: RaylibInputSource): InputSystem =
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

private fun movementBackground(input: InputSystem): kotlinx.cinterop.CValue<Color> {
    var r = 0
    var g = 0
    var b = 0

    if (input.isDown(MoveUpAction)) {
        b += MovementColorIntensity
    }
    if (input.isDown(MoveDownAction)) {
        r += MovementColorIntensity / 2
        g += MovementColorIntensity / 4
    }
    if (input.isDown(MoveLeftAction)) {
        r += MovementColorIntensity
    }
    if (input.isDown(MoveRightAction)) {
        g += MovementColorIntensity
    }

    return cValue {
        this.r = r.coerceIn(0, 255).toUByte()
        this.g = g.coerceIn(0, 255).toUByte()
        this.b = b.coerceIn(0, 255).toUByte()
        a = 255.toUByte()
    }
}

private fun key(code: UInt): InputBinding =
    InputBinding(InputControl.KeyboardKey(code.toInt()))

private fun mouseButton(code: UInt): InputBinding =
    InputBinding(InputControl.MouseButton(code.toInt()))

private fun org.bljw.kaylib.input.InputActionState.statusText(): String =
    when {
        wasPressed -> "pressed"
        wasReleased -> "released"
        isDown -> "down"
        else -> "up"
    }

private fun InputSystem.statusColor(
    id: InputActionId,
    activeColor: kotlinx.cinterop.CValue<Color>,
    inactiveColor: kotlinx.cinterop.CValue<Color>,
): kotlinx.cinterop.CValue<Color> =
    if (isDown(id) || wasPressed(id) || wasReleased(id)) activeColor else inactiveColor

private fun InputControl.displayName(): String =
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
