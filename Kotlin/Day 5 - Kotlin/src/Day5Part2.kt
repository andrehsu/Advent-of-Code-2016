import kotlin.system.measureTimeMillis

fun main(vararg args: String) {
	measureTimeMillis {
		generateSequence(0) { i -> i + 1 }.map { md5Hash(input + it) }
				.filter { it(end = 5) == "00000" }
				.filter { it[5].isDigit() }
				.filter { it[5].toDigit() in 0..7 }
				.let { seq ->
					mutableMapOf<Char, Char>().also { map ->
						seq.takeWhile { map.size < 8 }
								.forEach { map.putIfAbsent(it[5], it [6]) }
					}
				}.toList()
				.sortedBy { it.first }
				.map { it.second }
				.joinToString("").toLowerCase().println()
	}.println()
}