abstract class Figure(val height: Int, val side: Int) {
    abstract fun volume() : Double;
    abstract fun upperProjSquare() : Double;
    abstract fun sideProjSquare() : Double;
}

class Cone(height: Int, diam: Int) : Figure(height, diam) {
    override fun volume() : Double {
        return height * upperProjSquare() / 3;
    }

    override fun upperProjSquare() : Double {
        return side * side * Math.PI / 4;
    }

    override fun sideProjSquare() : Double {
        return side.toDouble() * height / 2;
    }
}

class Pyramid(height: Int, side: Int) : Figure(height, side) {
    override fun volume() : Double {
        return height * upperProjSquare() / 3;
    }

    override fun upperProjSquare() : Double {
        return side * side.toDouble();
    }

    override fun sideProjSquare() : Double {
        return side.toDouble() * height / 2;
    }
}

class Cube(height: Int, side: Int) : Figure(height, side) {
    override fun volume() : Double {
        return side * side.toDouble() * side;
    }

    override fun upperProjSquare() : Double {
        return side * side.toDouble();
    }

    override fun sideProjSquare() : Double {
        return side * side.toDouble();
    }
}

fun main() {
    print("Введите тип фигуры\n>");
    val ftype: String = readln();
    print("Введите высоту фигуры\n>");
    val height: Int = readln().toInt();
    print("Введите основание / диаметр фигуры\n>");
    val side: Int = readln().toInt();
    val fg: Figure = when (ftype) {
        "конус" -> Cone(height, side)
        "куб" -> Cube(height, side)
        else -> Pyramid(height, side)
    }
    println("Объём $ftype: " + fg.volume());
    println("Площадь боковой проекции: " + fg.sideProjSquare());
    println("Площадь проекции сверху: " + fg.upperProjSquare());
}
