package task1

class Motherboard(number: Int, name: String, price: Double, var cpuSocket: String, var cpusNumber: UInt, var ramType: String) : Item(number, name, price) {
    override fun toString(): String {
        return super.toString() + " type: motherboard | cpu socket: $cpuSocket | number of cpus: $cpusNumber | ram type: $ramType gb |"
    }
}
