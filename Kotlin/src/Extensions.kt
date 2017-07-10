@file:Suppress("NOTHING_TO_INLINE")

inline operator fun CharSequence.invoke(): String = toString()

inline operator fun CharSequence.invoke(start: Int): String = substring(start)

operator fun CharSequence.invoke(start: Int = 0, end: Int = length): String =
		substring(if (start < 0) length + start else start, if (end < 0) length + end else end)

operator fun CharSequence.invoke(start: Int = 0, end: Int = length, step: Int): String =
		slice((if (start < 0) length + start else start)..(if (end < 0) length + end else end) step step).toString()


inline operator fun StringBuilder.plusAssign(x: Any) {
	append(x)
}

inline fun <T : Any?> T.println(): T {
	println(this)
	return this
}

inline fun <T : Any?> T.print(): T {
	print(this)
	return this
}

inline fun <T : Any?> T.printf(format: String): T {
	format.format(this).print()
	return this
}

inline fun printf(format: String, vararg args: Any?) {
	print(format.format(*args))
}

inline fun printlnf(format: String, vararg args: Any?) {
	println(format.format(*args))
}

inline fun Char.toDigit() = Character.getNumericValue(this)

inline fun Int.abs() = Math.abs(this)

inline fun Long.abs() = Math.abs(this)

inline fun Float.abs() = Math.abs(this)

inline fun Double.abs() = Math.abs(this)

private val digits = "\\d+".toPattern()

fun String.extractInts(): List<Int> {
	val matcher = digits.matcher(this)
	
	val ints = mutableListOf<Int>()
	while (matcher.find()) {
		ints += substring(matcher.start(), matcher.end()).toInt()
	}
	
	return ints.optimizeReadOnly()
}

private fun <T> List<T>.optimizeReadOnly(): List<T> = when (size) {
	0 -> emptyList<T>()
	1 -> listOf(this[0])
	else -> this
}