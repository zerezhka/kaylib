package org.bljw.kaylib.screens

import org.bljw.kaylib.AppScreen
import org.bljw.kaylib.input.InputSystem

interface Screen {
    fun update(input: InputSystem): AppScreen?
    fun draw()
}
