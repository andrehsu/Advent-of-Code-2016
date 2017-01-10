import andre.adventofcode.input.Input;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecipherMessage {
	private static String decipher(List<String> input) {
		StringBuilder correctMessage = new StringBuilder(input.get(0).length());
		
		for (int i_letter = 0; i_letter < input.get(0).length(); i_letter++) {
			Map<Character, Integer> letterOccurrence = new HashMap<>();
			for (String inputLine : input) {
				char c = inputLine.charAt(i_letter);
				letterOccurrence.merge(c, 1, Integer::sum);
			}
			correctMessage.append(mostFrequent(letterOccurrence));
		}
		
		return correctMessage.toString();
	}
	
	private static String actualDecipher(List<String> input) {
		StringBuilder correctMessage = new StringBuilder(input.get(0).length());
		
		for (int i_letter = 0; i_letter < input.get(0).length(); i_letter++) {
			Map<Character, Integer> letterOccurrence = new HashMap<>();
			for (String inputLine : input) {
				char c = inputLine.charAt(i_letter);
				letterOccurrence.merge(c, 1, Integer::sum);
			}
			correctMessage.append(leastFrequent(letterOccurrence));
		}
		
		return correctMessage.toString();
	}
	
	private static char mostFrequent(Map<Character, Integer> input) {
		char mostFrequentLetter = (char) -1;
		int mostFrequentCount = 0;
		for (Map.Entry<Character, Integer> entry : input.entrySet()) {
			if (entry.getValue() > mostFrequentCount) {
				mostFrequentCount = entry.getValue();
				mostFrequentLetter = entry.getKey();
			}
		}
		
		return mostFrequentLetter;
	}
	
	private static char leastFrequent(Map<Character, Integer> input) {
		char leastFrequentLetter = (char) -1;
		int leastFrequentCount = Integer.MAX_VALUE;
		for (Map.Entry<Character, Integer> entry : input.entrySet()) {
			if (entry.getValue() < leastFrequentCount) {
				leastFrequentCount = entry.getValue();
				leastFrequentLetter = entry.getKey();
			}
		}
		
		return leastFrequentLetter;
	}
	
	private static class Run {
		public static void main(String[] args) {
			List<String> input = Input.readAllLines("Day 6/input.txt");
			
			System.out.println(decipher(input));
			System.out.println(actualDecipher(input));
		}
	}
}
