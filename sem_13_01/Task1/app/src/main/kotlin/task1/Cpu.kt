package task1

class Cpu(number: Int, name: String, price: Double, var cpuSocket: String, var coresNumber: UInt, var clockRateMhz: UInt) : Item(number, name, price) {
    override fun toString(): String {
        return super.toString() + " type: cpu | cpu socket: $cpuSocket | number of cores: $coresNumber | clock rate: ${clockRateMhz / 1000u} ghz |"
    }
}
