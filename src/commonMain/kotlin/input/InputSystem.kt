package org.bljw.kaylib.input

data class InputActionId(val value: String) {
    override fun toString(): String = value
}

sealed interface InputControl {
    val code: Int

    data class KeyboardKey(override val code: Int) : InputControl

    data class MouseButton(override val code: Int) : InputControl

    data class GamepadButton(
        override val code: Int,
        val gamepad: Int = 0,
    ) : InputControl

    data class GamepadAxis(
        override val code: Int,
        val gamepad: Int = 0,
        val threshold: Float = 0.5f,
        val direction: AxisDirection = AxisDirection.Positive,
    ) : InputControl

    data class VirtualButton(override val code: Int) : InputControl
}

enum class AxisDirection {
    Negative,
    Positive,
}

data class InputBinding(
    val control: InputControl,
)

data class InputAction(
    val id: InputActionId,
    val bindings: List<InputBinding>,
)

data class InputActionState(
    val isDown: Boolean = false,
    val wasPressed: Boolean = false,
    val wasReleased: Boolean = false,
    val value: Float = 0f,
)

data class InputControlState(
    val isDown: Boolean,
    val value: Float = if (isDown) 1f else 0f,
)

interface InputSource {
    fun read(control: InputControl): InputControlState
}

class InputSystem(
    private val source: InputSource,
) {
    private val actions = mutableMapOf<InputActionId, InputAction>()
    private val states = mutableMapOf<InputActionId, InputActionState>()

    fun addAction(
        id: InputActionId,
        bindings: List<InputBinding>,
    ) {
        require(bindings.isNotEmpty()) { "Action $id must have at least one binding." }

        actions[id] =
            InputAction(
                id = id,
                bindings = bindings,
            )
        if (id !in states) {
            states[id] = InputActionState()
        }
    }

    fun setBindings(
        id: InputActionId,
        bindings: List<InputBinding>,
    ) {
        require(bindings.isNotEmpty()) { "Action $id must have at least one binding." }
        val action = actions.getValue(id)
        actions[id] = action.copy(bindings = bindings)
    }

    fun replaceBinding(
        id: InputActionId,
        binding: InputBinding,
    ) {
        setBindings(
            id = id,
            bindings = listOf(binding),
        )
    }

    fun bindings(id: InputActionId): List<InputBinding> =
        actions.getValue(id).bindings

    fun state(id: InputActionId): InputActionState =
        states[id] ?: InputActionState()

    fun isDown(id: InputActionId): Boolean =
        state(id).isDown

    fun wasPressed(id: InputActionId): Boolean =
        state(id).wasPressed

    fun wasReleased(id: InputActionId): Boolean =
        state(id).wasReleased

    fun update() {
        actions.forEach { (id, action) ->
            val previous = state(id)
            val next = action.bindings
                .asSequence()
                .map { source.read(it.control) }
                .fold(InputControlState(isDown = false)) { current, candidate ->
                    InputControlState(
                        isDown = current.isDown || candidate.isDown,
                        value = maxOf(current.value, candidate.value),
                    )
                }

            states[id] =
                InputActionState(
                    isDown = next.isDown,
                    wasPressed = next.isDown && !previous.isDown,
                    wasReleased = !next.isDown && previous.isDown,
                    value = next.value,
                )
        }
    }
}
