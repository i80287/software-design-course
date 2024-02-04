package task_iterator

fun main() {
    val frame: Frame = Frame.fromString(readln())
    for (photo: Photo in frame) {
        println(photo)
    }
}
