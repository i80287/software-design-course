package task1

import java.lang.StringBuilder
import java.util.TreeMap

class StoreRegistry {
    val items = TreeMap<Int, Item>()

    fun addItem(item: Item) {
        items[item.number] = item
    }

    fun getItem(itemNumber: Int): Item? = items[itemNumber]

    override fun toString(): String {
        val sb = StringBuilder(items.size * "|  |  |  |".length) 
        for (item: Item in items.values) {
            sb.appendLine(item.toString())
        }
        return sb.toString()
    }
}
