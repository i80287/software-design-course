fun main() {
    val n: Int = readLine()!!.toInt();
    val matrix: Array<Array<Int>> = Array<Array<Int>>(n) { Array<Int>(n) { (0..10).random() } };
    var sum: Int = 0;
    for (i in 1..<n) {
        var j: Int = n - 1;
        while (n - 1 - i < j) {
            sum += matrix[i][j];
            --j;
        }
    }
    println(sum);
}
