// Introduction: Find the longest increasing subsequence in the array in nlogn time
// Input: The array
// Output: Longest increasing subsequence in the array
// Learning video: https://www.youtube.com/watch?v=S9oUiVYEq7E&t=285s
// Learning text: https://cp-algorithms.com/sequences/longest_increasing_subsequence.html
fun lis(array: IntArray): IntArray {

    // lis -> longest increasing subsequence

    // Get size of the array
    val n = array.size

    // Index of the surface of the specific length
    val surface = IntArray(n) { -1 }

    // Index of the parent of some value, follow the parent, can print increasing value
    val parent = IntArray(n) { -1 }

    // Length of the lis have found but minus one
    var len = 0

    // Init surface with first value
    // Mean for length 1 but minus one, the last value is at index 0
    surface[0] = 0

    // Loop for each value in the array
    for (i in 1 until n) {
        when {

            // Finding a value that can put later in the lis had found

            // Return longest non decreasing sequence
            // Change 4 things.
            // tail and head and how binary search work and candidate index can not be 0.
            // tail: '>' to '>=' (if the value is same, can put it to surface)
            // head: '<=' to '<' (if the value is same, maybe have same value in the later)
            // binary: should search for first value that > i'th value
            // candidate: if candidate index is 0, then should not set parent index of that value

            array[i] > array[surface[len]] -> {

                // The parent of value at index i become surface of lis had found
                parent[i] = surface[len++]

                // After length increase, set surface of lis to i
                surface[len] = i
            }

            // Finding a smallest value in the array until now
            array[i] <= array[surface[0]] -> {

                // Set i'th value as the length one array
                surface[0] = i
            }

            // The i'th value is in between of surface ( 0 < i <= len )
            else -> {

                // Find the ceiling of i'th value use binary search and replace it with i
                var left = 1
                var right = len
                var candidateIndex = -1
                while (left <= right) {

                    val mid = left + ((right - left) shr 1)

                    // What number do you want to replace
                    when {
                        // Find first value that >= i'th value
                        array[i] <= array[surface[mid]] -> {
                            candidateIndex = mid
                            right = mid - 1
                        }
                        else -> {
                            left = mid + 1
                        }
                    }
                }

                // Change the surface of length of array which candidateIndex is in
                surface[candidateIndex] = i

                // Set parent of i'th value to the surface of previous length
                parent[i] = surface[candidateIndex - 1]
            }
        }
    }

    // Recall len == the length of lis minus one

    // Restore the array, i -> position to write value, j -> index of value to write
    val lis = IntArray(len + 1) { -1 }
    var i = len
    var j = surface[len]
    while (i >= 0) {
        lis[i--] = array[j]
        j = parent[j]
    }

    // Return longest increasing subsequence
    return lis
}

fun main() {
    //val a = intArrayOf(1, 2, 3, 4)
    val a = intArrayOf(4, 3, 2, 1)
    println(lis(a).joinToString(" "))
}