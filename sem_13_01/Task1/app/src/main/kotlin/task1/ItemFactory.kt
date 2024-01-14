package task1

class ItemFactory {
    fun getMotherboard(number: Int, name: String, price: Double, cpuSocket: String, cpusNumber: UInt, ramType: String): Motherboard {
        return Motherboard(number, name, price, cpuSocket, cpusNumber, ramType)
    }

    fun getCpu(number: Int, name: String, price: Double, cpuSocket: String, coresNumber: UInt, clockRateMhz: UInt): Cpu {
        return Cpu(number, name, price, cpuSocket, coresNumber, clockRateMhz)
    }

    fun getHdd(number: Int, name: String, price: Double, capacity: UInt, spinRate: UInt): Hdd {
        return Hdd(number, name, price, capacity, spinRate)
    }
}
