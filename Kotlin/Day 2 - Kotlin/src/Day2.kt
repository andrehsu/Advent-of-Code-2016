val input = readLinesFromFile("Day 2/input.txt")

fun main(vararg args: String) {
	input.fold(listOf<Pair<Int, Int>>()) { acc, line ->
		acc + line.fold(acc.lastOrNull() ?: 1 to 1) { (r, c), char ->
			when {
				char == 'L' && c != 0 -> r to c - 1
				char == 'R' && c != 2 -> r to c + 1
				char == 'U' && r != 0 -> r - 1 to c
				char == 'D' && r != 2 -> r + 1 to c
				else -> r to c
			}
		}
	}.map(::keypadKey).joinToString("").println()
}

private fun keypadKey(key: Pair<Int, Int>): Int = when (key) {
	0 to 0 -> 1
	0 to 1 -> 2
	0 to 2 -> 3
	1 to 0 -> 4
	1 to 1 -> 5
	1 to 2 -> 6
	2 to 0 -> 7
	2 to 1 -> 8
	2 to 2 -> 9
	else -> throw IllegalArgumentException("No such key (${key.first}, ${key.second})")
}