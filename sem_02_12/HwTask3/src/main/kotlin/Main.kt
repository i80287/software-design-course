fun main() {
    val args: List<String> = readLine()!!.split(' ');
    val n: Int = args.size;
    if (n == 0) {
        println('0');
        return;
    }

    var count: Int = 1;
    var a_i_prev: Int = args[0].toInt();
    for (i in 1..<n) {
        val a_i: Int = args[i].toInt();
        if (a_i_prev != a_i) {
            count++;
            a_i_prev = a_i;
        }
    }

    println(count);
}
