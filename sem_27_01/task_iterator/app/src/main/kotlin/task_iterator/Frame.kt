package task_iterator

import kotlin.collections.ArrayList
import kotlin.collections.Iterator

class Frame private constructor(val photos: ArrayList<Photo>) {
    operator fun iterator(): Iterator<Photo> = FrameIterator(photos)

    companion object {
        fun fromString(str: String): Frame {
            val exprs = str.split('\"')
            val photos = ArrayList<Photo>(exprs.size / 2)
            for (i in 1..<exprs.size step 2) {
                photos.add(Photo(exprs[i]))
            }
            return Frame(photos)
        }
    }
}
