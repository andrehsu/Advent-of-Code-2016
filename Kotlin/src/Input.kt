import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

fun readLinesFromFile(url: String): List<String> {
	try {
		return Files.readAllLines(Paths.get(url)).filterNotNull()
	} catch (e: IOException) {
		System.err.println("File $url not found")
		System.exit(1)
		throw RuntimeException(e)
	}
}

fun readStringFromFile(url: String): String {
	return readLinesFromFile(url).joinToString("")
}