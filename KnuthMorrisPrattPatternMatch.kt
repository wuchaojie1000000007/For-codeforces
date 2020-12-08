// Introduction: Find if given pattern in given string
// Input: String and Pattern to match
// Output: The index where match start or -1 if do not find match
// Learning video: https://www.youtube.com/watch?v=BXCEFAzhxGY&t=927s
fun kpm(string: String, pattern: String): Int {

    // String's length
    val m = string.length

    // Pattern's length
    val n = pattern.length

    // Store the information about prefix and suffix of the pattern
    // Longest prefix same as suffix until that index
    val info = IntArray(n) { 0 }

    // Build the info array
    var i = 0
    var j = 1
    while (j < n) {

        when {
            pattern[i] == pattern[j] -> {
                info[j] = i + 1
                i++
                j++
            }

            // Slow down moving i to the index of 0 using prefix and suffix
            pattern[i] != pattern[j] && i != 0 -> i = info[i - 1]

            pattern[i] != pattern[j] && i == 0 -> j++
        }
    }

    // The index of the pattern which used to compare with string,
    // when it become length of pattern, it mean find pattern in the string
    var k = 0

    // Store the first index of the first occurrence of the pattern
    var index = -1

    for (x in 0 until m) {

        when {

            // Moving to next char of the pattern
            string[x] == pattern[k] -> k++

            // Moving to previous char of the pattern use information stored in the info array
            string[x] != pattern[k] && k != 0 -> k = info[k - 1]

            // Can not move to previous, so do nothing
            string[x] != pattern[k] && k == 0 -> {
            }
        }

        // Find the first occurrence of the pattern, break the loop
        if (k == n) {
            index = x - n + 1
            break
        }
    }
    return index
}

fun main() {
    val s = "qwertyqwertyqwerty"
    val p = "wertyq"
    println(kpm(s, p))
}