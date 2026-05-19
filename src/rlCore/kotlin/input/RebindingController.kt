@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package org.bljw.kaylib.input

class RebindingController(private val inputSource: RaylibInputSource) {
    var activeAction: InputActionId? = null
        private set

    private var releaseBeforeCapture: InputActionId? = null
    private var canCapture = false

    fun start(action: InputActionId) {
        activeAction = action
        releaseBeforeCapture = action
        canCapture = false
        inputSource.clearKeyboardCaptureQueue()
    }

    fun cancel() {
        activeAction = null
        releaseBeforeCapture = null
        canCapture = false
    }

    fun update(input: InputSystem): Pair<InputActionId, InputControl>? {
        val action = activeAction ?: return null

        if (canCapture) {
            val control = inputSource.captureNextControl()
            if (control != null) {
                cancel()
                return action to control
            }
        } else if (releaseBeforeCapture != null && !input.isDown(releaseBeforeCapture!!)) {
            canCapture = true
        }

        return null
    }
}
