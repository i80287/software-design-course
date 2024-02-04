package task_memento

import kotlin.collections.ArrayList
import kotlin.text.StringBuilder

class Editor {
    private val textsStack = ArrayList<StringBuilder>()
    private var currentText = StringBuilder()

    fun run() {
        while (true) {
            print("Enter command (help for help menu)\n> ")
            val (cmd, text) = extractCommandAndText(readln())
            when (cmd) {
                "add" -> appendText(text)
                "edit" -> setNewState(text)
                "watch" -> printState()
                "cancel" -> restore()
                "help" ->  printHelperMenu()
                "exit" -> {
                    onExit()
                    break
                }
                else -> onUnknownCommand(cmd)
            }
        }
    }

    private fun extractCommandAndText(input: String): Pair<String, String> {
        for (i: Int in input.indices) {
            if (input[i] == ' ') {
                return Pair(input.substring(0, i), input.substring(i + 1))
            }
        }

        return Pair(input, "")
    }

    private fun save() {
        textsStack.add(currentText)
    }

    private fun setNewState(text: String) {
        save()
        currentText = StringBuilder(text)
        currentText.append('\n')
    }

    private fun printState() = println(getState())

    private fun getState(): StringBuilder = currentText

    private fun appendText(text: String) {
        currentText.append(text)
        currentText.append('\n')
    }

    private fun restore() {
        if (textsStack.isNotEmpty())
            currentText = textsStack.removeAt(textsStack.size - 1)
        else
            currentText.clear()
    }

    private fun printHelperMenu() = println(
        """Supported commands:
        add some_text - adds some_text with new line symbol to the end 

        edit new_text - saves current text and sets current text to new_text (with new line symbol in the end)

        watch - prints out current text
        
        cancel - restores previous text
        
        help - prints this menu
        
        exit - stop editor
        """.trimIndent())

    private fun onExit() = println("Exiting program")

    private fun onUnknownCommand(cmd: String) = println("Unknown command: $cmd")
}
