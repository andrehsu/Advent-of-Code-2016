fun main(vararg args: String) {
	input.filter { line ->
		line.replace("[", "|[").replace("]", "]|").split("|")
				.partition { '[' !in it }
				.let { (l1, l2) ->
					l1.joinToString("|||") to l2.joinToString("|||")
				}.let { (sn, hn) ->
			(0..(sn.length - 3)).filter { i -> sn[i] == sn[i + 2] && sn[i] != sn[i + 1] }
					.map { i -> sn[i + 1].toString() + sn[i] + sn[i + 1] }
					.any { it in hn }
		}
	}.count().println()
}
