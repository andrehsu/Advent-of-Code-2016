val input = readLinesFromFile("Day 6/input.txt")

fun main(vararg args: String) {
	input[0].indices.map { i ->
		input.map { it[i] }.groupBy { it }.maxBy { it.value.size }?.key ?: ' '
	}.joinToString("").println()
	
	input[0].indices.map { i ->
		input.map { it[i] }.groupBy { it }.minBy { it.value.size }?.key ?: ' '
	}.joinToString("").println()
}