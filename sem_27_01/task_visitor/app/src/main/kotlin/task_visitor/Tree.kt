package task_visitor

import java.lang.StringBuilder
import java.util.ArrayList

class Tree(s: String) {
    private var root = Node(if (s.isNotEmpty()) s[0] else 0.toChar())

    init {
        val nodesPtrsStack = ArrayList<Node>()
        nodesPtrsStack.ensureCapacity(s.length / 2)
        nodesPtrsStack.add(root)

        for (i in 1..<s.length) {
            when (val chr: Char = s[i]) {
                '(', '[', '{' -> {
                    val lastNode: Node = nodesPtrsStack.last()
                    val newNode = Node(chr)
                    lastNode.children.add(newNode)
                    nodesPtrsStack.add(newNode)
                }
                ')', ']', '}' -> {
                    nodesPtrsStack.removeAt(nodesPtrsStack.size - 1)
                }
            }
        }
    }

    fun accept(visitor: Visitor) {
        root.accept(visitor)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.ensureCapacity(root.children.size)
        fillString(sb, root)
        return sb.toString()
    }

    companion object {
        private fun fillString(sb: StringBuilder, node: Node) {
            sb.append(node.openingChar())
            for (child in node.children) {
                fillString(sb, child)
            }
            sb.append(node.closingChar())
        }
    }
}
