// Introduction: Segment tree for range query in O(log(n)) time, point update in O(log(n)) time
// Construct: Use original array and computation mode
// Learning video: https://www.youtube.com/watch?v=ZBHKZF5w4YU
// Learning text: https://cp-algorithms.com/data_structures/segment_tree.html
class SegmentTree(private var originalArray: IntArray, private val mode: Mode = Mode.Min) {

    // Original array's size, may be filled to next power of two for simple implement
    private var arraySize = originalArray.size

    // The array to store segment tree
    private var segmentTree: IntArray

    // Three mode to use segment tree
    enum class Mode { Sum, Min, Max }

    // Default value to fill original array based on different mode
    private val defaultValue = when (mode) {
        Mode.Sum -> 0
        Mode.Min -> Int.MAX_VALUE
        Mode.Max -> Int.MIN_VALUE
    }

    // Return segment tree's size, and fill up original array until it's size is power of two
    private fun getSegmentTreeSize(originalArraySize: Int): Int {

        // Array size is already power of two
        val binary = originalArraySize.toString(2)
        if (binary.substring(1).all { it == '0' })
            return 2 * arraySize - 1

        // Array size is not power of two,
        // Find the next number of power of two
        val exp = binary.length
        var size = 1
        repeat(exp) {
            size *= 2
        }

        // Change original array size to power of two
        // Original array default value should change based on usage
        // original array default value is same as segment tree default value
        val tempArray = IntArray(size) { defaultValue }
        (0 until arraySize).forEach { tempArray[it] = originalArray[it] }
        originalArray = tempArray
        arraySize = size

        return 2 * size - 1
    }

    // Some helper function
    private fun getLeftChild(i: Int) = 2 * i + 1
    private fun getRightChild(i: Int) = 2 * i + 2
    private fun getParent(i: Int) = (i - 1) / 2

    // Combine child value to get parent value based on different mode
    private fun combineChild(i: Int) = when (mode) {
        Mode.Sum -> segmentTree[getLeftChild(i)] + segmentTree[getRightChild(i)]
        Mode.Min -> minOf(segmentTree[getLeftChild(i)], segmentTree[getRightChild(i)])
        Mode.Max -> maxOf(segmentTree[getLeftChild(i)], segmentTree[getRightChild(i)])
    }

    // Combine two value to get target value based on different mode
    private fun combineChild(left: Int, right: Int) = when (mode) {
        Mode.Sum -> left + right
        Mode.Min -> minOf(left, right)
        Mode.Max -> maxOf(left, right)
    }

    // For tree node at index 'pos', it's range is 'left'..'right'
    private fun buildSegmentTree(left: Int, right: Int, pos: Int) {
        when (left) {
            right -> segmentTree[pos] = originalArray[left]
            else -> {
                val mid = left + ((right - left) shr 1)
                buildSegmentTree(left, mid, getLeftChild(pos))
                buildSegmentTree(mid + 1, right, getRightChild(pos))
                segmentTree[pos] = combineChild(pos)
            }
        }
    }

    // Initialize the segment tree with original array
    init {

        // Create segment tree, set default value, and use index 0 as root
        segmentTree = IntArray(getSegmentTreeSize(arraySize)) { Int.MAX_VALUE }

        // Build segment tree in O(n)
        buildSegmentTree(0, arraySize - 1, 0)
    }

    // For tree node at index 'pos', it's range is 'left'..'right'
    // When calling this function, just set first two parameters
    fun getValue(queryLeft: Int, queryRight: Int, left: Int = 0, right: Int = arraySize - 1, pos: Int = 0): Int = when {

        // Total overlap
        queryLeft <= left && queryRight >= right -> segmentTree[pos]
        // No overlap
        queryLeft > right || queryRight < left -> defaultValue
        // some overlap
        else -> {
            val mid = left + ((right - left) shr 1)
            combineChild(getValue(queryLeft, queryRight, left, mid, getLeftChild(pos)),
                    getValue(queryLeft, queryRight, mid + 1, right, getRightChild(pos))
            )
        }
    }

    // For tree node at index 'pos', it's range is 'left'..'right'
    // When calling this function, just set first two parameters
    fun updateValue(index: Int, value: Int, left: Int = 0, right: Int = arraySize - 1, pos: Int = 0) {
        when (left) {
            right -> segmentTree[pos] = value
            else -> {
                val mid = left + ((right - left) shr 1)
                if (index <= mid) {
                    updateValue(index, value, left, mid, getLeftChild(pos))
                } else {
                    updateValue(index, value, mid + 1, right, getRightChild(pos))
                }
                segmentTree[pos] = combineChild(pos)
            }
        }
    }
}


fun main() {
    val arr = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8)
    val segmentTree = SegmentTree(arr, SegmentTree.Mode.Min)
    println(segmentTree.getValue(0, 3))
    segmentTree.updateValue(4, -1)
    println(segmentTree.getValue(0, 4))
}