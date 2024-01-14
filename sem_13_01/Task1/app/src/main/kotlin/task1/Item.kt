package task1

open class Item(var number: Int, var name: String, var price: Double) {
    override fun toString(): String {
        return "| number: $number | name: $name | price: $price |"
    }
}
