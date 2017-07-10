import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter

val input = readStringFromFile("Day 5/input.txt")

fun main(vararg args: String) {
	generateSequence(0) { i -> i + 1 }.map { md5Hash(input + it) }
			.filter { it(end = 5) == "00000" }
			.take(8)
			.map { it[5] }
			.joinToString("").toLowerCase().println()
}

private val md5 = MessageDigest.getInstance("MD5")!!

fun md5Hash(input: String): String {
	val hashResult = md5.digest(input.toByteArray())
	return DatatypeConverter.printHexBinary(hashResult)
}