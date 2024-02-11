package task_visitor

import java.util.ArrayList;

class Node {
    constructor(chr: Char) {
        this.type = charToType(chr) 
    }

    constructor(type: NodeType) {
        this.type = type
    }

    var children = ArrayList<Node>()
    var type: NodeType

    fun accept(visitor: Visitor) {
        visitor.visit(this)
    }
    
    fun openingChar(): Char = when(type) {
        NodeType.RB -> '('
        NodeType.SB -> '['
        NodeType.FB -> '{'
        else -> 0.toChar()
    }

    fun closingChar(): Char = when(type) {
        NodeType.RB -> ')'
        NodeType.SB -> ']'
        NodeType.FB -> '}'
        else -> 0.toChar()
    }

    companion object {
        private fun charToType(chr: Char): NodeType {
            return when (chr) {
                '(', ')' -> NodeType.RB
                '[', ']' -> NodeType.SB
                '{', '}' -> NodeType.FB
                else -> NodeType.NULL
            }
        }
    }
}
