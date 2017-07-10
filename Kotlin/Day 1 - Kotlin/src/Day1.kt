val input = readStringFromFile("Day 1/input.txt")
typealias Point = Pair<Int, Int>

fun main(vararg args: String) {
	input.split(", ").let { tokens ->
		tokens.map { s -> if (s[0] == 'R') 1 else -1 }
				.fold(listOf<Int>()) { acc, t ->
					acc + (acc.lastOrNull() ?: 0).let { dir ->
						(dir + t).let { when (it) { 4 -> 0; -1 -> 3; else -> it; } }
					}
				} zip tokens.map { it(1).toInt() }
	}.fold(listOf(0 to 0)) {
		acc, (dir, dist) ->
		acc + acc.last().let { (x, y) ->
			(1..dist).map { d ->
				when (dir) {
					0 -> x to y + d
					2 -> x to y - d
					1 -> x + d to y
					else -> x - d to y
				}
			}
		}
	}.also {
		it.last().let { (x, y) -> x.abs() + y.abs() }.println()
	}.let { points ->
		points.withIndex().first { (i, p) -> p in points.take(i) }.value.let { (x, y) -> x.abs() + y.abs() }.println()
	}
}