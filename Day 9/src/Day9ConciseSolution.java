import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Andre on 3/5/2017.
 */
public class Day9ConciseSolution {
	private static final String input;
	
	static {
		try {
			input = Files.readAllLines(Paths.get("Java/input.txt")).get(0);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
		int count = 0;
		int startParen = -1;
		
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c == '(') {
				startParen = i;
			} else if (c == ')') {
				int[] ints = stringsToInts(input.substring(startParen + 1, i).split("x"));
				int length = ints[0];
				int weight = ints[1];
				
				count += weight * length;
				i = i + length;
				startParen = -1;
			} else if (startParen == -1) {
				count++;
			}
		}
		
		System.out.println(count);
	}
	
	private static int[] stringsToInts(String[] arr) {
		int[] out = new int[arr.length];
		for (int i = 0; i < out.length; i++) {
			out[i] = Integer.parseInt(arr[i]);
		}
		return out;
	}
}
