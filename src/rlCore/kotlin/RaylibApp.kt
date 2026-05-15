@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package org.bljw.kaylib

import kotlinx.cinterop.cValue
import rl.BeginDrawing
import rl.ClearBackground
import rl.CloseWindow
import rl.Color
import rl.DrawText
import rl.EndDrawing
import rl.GetScreenHeight
import rl.GetScreenWidth
import rl.InitWindow
import rl.MeasureText
import rl.RAYLIB_VERSION
import rl.SetTargetFPS
import rl.WindowShouldClose

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

    InitWindow(800, 600, "Raylib $RAYLIB_VERSION")
    SetTargetFPS(60)

    val fontSize = 32
    val message = "Hello from Kotlin"
    val hintFontSize = 16
    val hint = "Esc or close to quit."

    try {
        while (!WindowShouldClose()) {
            BeginDrawing()
            ClearBackground(black)
            val textWidth = MeasureText(message, fontSize)
            val x = (GetScreenWidth() - textWidth) / 2
            val y = (GetScreenHeight() - fontSize) / 2
            DrawText(message, x, y, fontSize, white)
            val hintWidth = MeasureText(hint, hintFontSize)
            val hintX = (GetScreenWidth() - hintWidth) / 2
            val hintY = GetScreenHeight() - hintFontSize - 28
            DrawText(hint, hintX, hintY, hintFontSize, hintColor)
            EndDrawing()
        }
    } finally {
        CloseWindow()
    }
}
