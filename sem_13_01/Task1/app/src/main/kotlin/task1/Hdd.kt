package task1

class Hdd(number: Int, name: String, price: Double, var capacity: UInt, var spinRate: UInt) : Item(number, name, price) {
    override fun toString(): String {
        return super.toString() + " type: hdd | capacity: $capacity gb | spinRate: $spinRate rpm |"
    }
}
