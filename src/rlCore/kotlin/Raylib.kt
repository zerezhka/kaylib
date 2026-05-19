@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package org.bljw.kaylib

import kotlinx.cinterop.CValue
import rl.BeginDrawing
import rl.CheckCollisionPointRec
import rl.ClearBackground
import rl.CloseWindow
import rl.Color
import rl.DrawRectangle
import rl.DrawRectangleRec
import rl.DrawText
import rl.EndDrawing
import rl.GetFPS
import rl.GetFrameTime
import rl.GetMousePosition
import rl.GetScreenHeight
import rl.GetScreenWidth
import rl.InitWindow
import rl.IsMouseButtonPressed
import rl.MeasureText
import rl.MOUSE_BUTTON_LEFT
import rl.Rectangle
import rl.SetExitKey
import rl.SetTargetFPS
import rl.Vector2
import rl.WindowShouldClose

fun initWindow(width: Int, height: Int, title: String) = InitWindow(width, height, title)
fun closeWindow() = CloseWindow()
fun windowShouldClose(): Boolean = WindowShouldClose()
fun setTargetFps(fps: Int) = SetTargetFPS(fps)
fun setExitKey(key: Int) = SetExitKey(key)

fun beginDrawing() = BeginDrawing()
fun endDrawing() = EndDrawing()
fun clearBackground(color: CValue<Color>) = ClearBackground(color)

fun drawText(text: String, x: Int, y: Int, fontSize: Int, color: CValue<Color>) =
    DrawText(text, x, y, fontSize, color)

fun measureText(text: String, fontSize: Int): Int = MeasureText(text, fontSize)

fun drawRect(x: Int, y: Int, width: Int, height: Int, color: CValue<Color>) =
    DrawRectangle(x, y, width, height, color)

fun drawRect(rec: CValue<Rectangle>, color: CValue<Color>) = DrawRectangleRec(rec, color)

fun screenWidth(): Int = GetScreenWidth()
fun screenHeight(): Int = GetScreenHeight()

fun mousePos(): CValue<Vector2> = GetMousePosition()
fun isMouseClicked(button: Int = MOUSE_BUTTON_LEFT.toInt()): Boolean = IsMouseButtonPressed(button)
fun checkCollision(point: CValue<Vector2>, rec: CValue<Rectangle>): Boolean =
    CheckCollisionPointRec(point, rec)

fun fps(): Int = GetFPS()
fun frameTime(): Float = GetFrameTime()
