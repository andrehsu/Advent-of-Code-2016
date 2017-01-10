package andre.adventofcode.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Input {
	public static List<String> readAllLines(String fileURL) {
		
		try {
			return Files.readAllLines(Paths.get(fileURL));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String readFirstLine(String fileURL) {
		return readAllLines(fileURL).get(0);
	}
}
