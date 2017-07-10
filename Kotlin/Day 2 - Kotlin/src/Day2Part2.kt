fun main(vararg args: String) {
	input.fold(listOf<Pair<Int, Int>>()) { acc, line ->
		acc + line.fold(acc.lastOrNull() ?: 1 to 1) { (r, c), char ->
			when {
				char == 'L' && if (r == 0 || r == 4) c != 2 else if (r == 1 || r == 3) c != 1 else c != 0 -> r to c - 1
				char == 'R' && if (r == 0 || r == 4) c != 2 else if (r == 1 || r == 3) c != 3 else c != 4 -> r to c + 1
				char == 'U' && if (c == 0 || c == 4) r != 2 else if (c == 1 || c == 3) r != 1 else r != 0 -> r - 1 to c
				char == 'D' && if (c == 0 || c == 4) r != 2 else if (c == 1 || c == 3) r != 3 else r != 4 -> r + 1 to c
				else -> r to c
			}
		}
	}.map(::keypadKey).joinToString("").println()
}

private fun keypadKey(key: Pair<Int, Int>) = when (key) {
	0 to 2 -> '1'
	1 to 1 -> '2'
	1 to 2 -> '3'
	1 to 3 -> '4'
	2 to 0 -> '5'
	2 to 1 -> '6'
	2 to 2 -> '7'
	2 to 3 -> '8'
	2 to 4 -> '9'
	3 to 1 -> 'A'
	3 to 2 -> 'B'
	3 to 3 -> 'C'
	4 to 2 -> 'D'
	else -> throw IllegalArgumentException("No such key (${key.first}, ${key.second}")
}