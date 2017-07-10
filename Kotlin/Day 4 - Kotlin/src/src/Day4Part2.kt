fun main(vararg args: String) {
	input.map { line ->
		listOf(line(0, -11), line(-10, -7), line(-6, -1))
	}.map { (name, sectorId, _) ->
		sectorId.toInt().let { sectorIdInt -> name.rot(sectorIdInt).replace('-', ' ') to sectorIdInt }
	}.filter { "north" in it.first }.forEach { it.println() }
}

fun String.rot(times: Int): String {
	return this.map { c ->
		if (c.isLetter()) {
			val min = (if (c.isUpperCase()) 'A' else 'a').toInt()
			val max = (if (c.isUpperCase()) 'Z' else 'z').toInt() - min + 1
			val i = c.toInt()
			((i - min + times % 26) % max + min).toChar()
		} else {
			c
		}
	}.joinToString("")
}