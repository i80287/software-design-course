package task_iterator

import kotlin.collections.ArrayList
import kotlin.collections.Iterator

class FrameIterator(val photos: ArrayList<Photo>): Iterator<Photo> {
    private var i: Int = 0

    override fun hasNext(): Boolean = photos.isNotEmpty()

    override fun next(): Photo {
        val photo: Photo = photos[i]
        i = (i + 1) % photos.size
        Thread.sleep(1_000)
        return photo
    }
}
