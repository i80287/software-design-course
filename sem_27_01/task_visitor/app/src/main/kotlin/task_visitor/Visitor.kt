package task_visitor

class Visitor {
    fun visit(node: Node) {
        when (node.type) {
            NodeType.SB -> {
                node.children.clear()
                return
            }
            NodeType.FB -> {
                val newNode = Node(NodeType.RB)
                newNode.children = node.children
                node.children = ArrayList()
                node.children.add(newNode)
            }
            NodeType.RB -> {}
            NodeType.NULL -> return
        }

        for (child in node.children) {
            child.accept(this)
        }
    }
}
