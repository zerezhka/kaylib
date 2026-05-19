@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package org.bljw.kaylib

import kotlinx.cinterop.CValue
import org.bljw.kaylib.input.InputActionId
import org.bljw.kaylib.input.InputActionState
import org.bljw.kaylib.input.InputSystem
import rl.Color

fun InputActionState.statusText(): String =
    when {
        wasPressed -> "pressed"
        wasReleased -> "released"
        isDown -> "down"
        else -> "up"
    }

fun InputSystem.statusColor(
    id: InputActionId,
    active: () -> CValue<Color> = { UiTheme.green() },
    inactive: () -> CValue<Color> = { UiTheme.hintColor() },
): CValue<Color> =
    if (isDown(id)) active() else inactive()
