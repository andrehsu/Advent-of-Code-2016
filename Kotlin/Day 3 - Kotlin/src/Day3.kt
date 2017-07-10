val input = readLinesFromFile("Day 3/input.txt")

fun main(vararg args: String) {
	input.map { line ->
		line.trim().split(" +".toRegex()).map(String::toInt)
	}.filter { (l1, l2, l3) ->
		l1 + l2 > l3 && l2 + l3 > l1 && l1 + l3 > l2
	}.count().println()
}