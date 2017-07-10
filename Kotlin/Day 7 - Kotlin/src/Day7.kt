val input = readLinesFromFile("Day 7/input.txt")

fun main(vararg args: String) {
	input.filter { line ->
		line.replace("[", "|[").replace("]", "]|").split("|")
				.partition { '[' !in it }
				.let { (l1, l2) ->
					l1.joinToString(separator = "||||") to l2.joinToString(separator = "||||")
				}.let { (s1, s2) ->
			{ str: String ->
				(0 until str.length - 3)
						.any { i -> str[i] == str[i + 3] && str[i + 1] == str[i + 2] && str[i] != str[i + 1] }
			}.let { abba -> abba(s1) && !abba(s2) }
		}
	}.count().println()
}
