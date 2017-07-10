fun main(vararg args: String) {
	input.map { line ->
		line.trim().split(" +".toRegex()).map(String::toInt)
	}.let { l ->
		(l.indices step 3).flatMap { i ->
			val (al1, bl1, cl1) = l[i]
			val (al2, bl2, cl2) = l[i + 1]
			val (al3, bl3, cl3) = l[i + 2]
			listOf(listOf(al1, al2, al3), listOf(bl1, bl2, bl3), listOf(cl1, cl2, cl3))
		}
	}.filter { (l1, l2, l3) ->
		l1 + l2 > l3 && l2 + l3 > l1 && l1 + l3 > l2
	}.count().println()
}