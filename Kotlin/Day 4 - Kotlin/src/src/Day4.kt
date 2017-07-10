val input = readLinesFromFile("Day 4/input.txt")

fun main(vararg args: String) {
	input.map { line ->
		listOf(line(0, -11).filter(Char::isLetter), line(-10, -7), line(-6, -1))
	}.filter { (name, _, checkSum) ->
		name.groupBy { it }
				.mapValues { it.value.size }.toList()
				.sortedWith(Comparator { (k1, v1), (k2, v2) ->
					(v2 - v1).let { if (it == 0) k1.compareTo(k2) else it }
				})
				.take(5)
				.map { it.first }
				.joinToString("") == checkSum
	}.map { (_, sectorId, _) -> sectorId.toInt() }
			.sum().println()
}