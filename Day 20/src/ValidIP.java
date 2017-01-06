import com.andre.Input;

import java.util.*;

/**
 * Created by Andre on 1/6/2017.
 */
public class ValidIP {
	public static final List<String> input = Input.readAllLines("Day 20/input.txt");
	
	public static long lowestValidIP(List<String> bannedRanges) {
		long smallestValue = 0;
		
		long lastSize = -1;
		while (bannedRanges.size() != lastSize) {
			lastSize = bannedRanges.size();
			for (Iterator<String> iterator = bannedRanges.iterator(); iterator.hasNext(); ) {
				String next = iterator.next();
				
				long[] ranges = ranges(next);
				if (ranges[0] <= smallestValue && smallestValue <= ranges[1]) {
					smallestValue = ranges[1] + 1;
					iterator.remove();
				}
			}
		}
		
		return smallestValue;
	}
	
	private static long[] ranges(String input) {
		String[] token_strings = input.trim().split("-");
		return new long[]{Long.parseLong(token_strings[0]),
				Long.parseLong(token_strings[1])};
	}
	
	private static class Test {
		private static final List<String> testInput = Input.readAllLines("Day 20/test input.txt");
		
		public static void main(String[] args) {
			System.out.println(lowestValidIP(testInput));
		}
	}
}

class RunDay20_Part1 {
	public static void main(String[] args) {
		System.out.println(ValidIP.lowestValidIP(ValidIP.input));
	}
}