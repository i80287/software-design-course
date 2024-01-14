package task1

fun fillRegistry(registry: StoreRegistry) {
    val factory = ItemFactory()
    registry.addItem(factory.getMotherboard(number = 0, name = "GIGABYTE B760M", price = 108.89, cpuSocket = "LGA 1700", cpusNumber = 1u, ramType = "DDR4"))
    registry.addItem(factory.getMotherboard(number = 1, name = "MSI MAG Z790 Tomahawk", price = 279.89, cpuSocket = "LGA 1700", cpusNumber = 1u, ramType = "DDR5"))
    registry.addItem(factory.getMotherboard(number = 2, name = "Gigabyte B550 Gaming X V2", price = 99.99, cpuSocket = "AM4", cpusNumber = 1u, ramType = "DDR4"))
    registry.addItem(factory.getCpu(number = 3, name = "AMD Ryzen™ 5 4500", price = 79.00, cpuSocket = "AM4", coresNumber = 6u, clockRateMhz = 4100u))
    registry.addItem(factory.getCpu(number = 4, name = "Intel Core i7-12700K", price = 256.80, cpuSocket = "LGA 1700", coresNumber = 12u, clockRateMhz = 3600u))
    registry.addItem(factory.getCpu(number = 5, name = "Intel Core i3-12100", price = 109.39, cpuSocket = "LGA 1700", coresNumber = 4u, clockRateMhz = 3300u))
    registry.addItem(factory.getHdd(number = 6, name = "Seagate BarraCuda ST2000LM015", price = 64.99, capacity = 2000u, spinRate = 5400u))
    registry.addItem(factory.getHdd(number = 7, name = "Western Digital HUS722T2TALA604", price = 79.00, capacity = 2000u, spinRate = 7200u))
    registry.addItem(factory.getHdd(number = 8, name = "Western Digital WD201KFGX", price = 412.69, capacity = 20000u, spinRate = 7200u))
}

fun parseInt(input: String?): Int? {
    if (input == null) {
        return null
    }
    return try {
        input.toInt()
    } catch (e: Exception) {
        return null
    }
}

fun main() {
    val registry = StoreRegistry()
    fillRegistry(registry)
    println(registry.toString())
    while (true) {
        print("Enter item number to get info or press Enter to exit\n> ")
        val itemNumber: Int = parseInt(readlnOrNull()) ?: break
        println(registry.getItem(itemNumber)?.toString() ?: "item not found")
    }

    println("Exiting...")
}
