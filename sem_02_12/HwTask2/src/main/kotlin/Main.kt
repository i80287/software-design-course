fun main() {
    val line: String? = readlnOrNull();
    val lastWord: String? = line?.split(' ')?.last();
    println(line);
    repeat(5) {
        println(lastWord);
    }
}
