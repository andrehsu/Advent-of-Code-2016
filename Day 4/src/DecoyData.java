import andre.adventofcode.input.Input;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by Andre on 12/5/2016.
 */
public class DecoyData {
	public static final List<String> input = Input.readAllLines("Day 4/input.txt");
	
	static String matchKey(List<String> input, String match) {
		for (String key : input) {
			String[] keySplit = splitKey(key);
			String unencryptedName = decryptName(keySplit[0], Integer.parseInt(keySplit[1]));
			if (unencryptedName.contains(match))
				return key;
		}
		return null;
	}
	
	private static String decryptName(String encryptedName, int shiftFactor) {
		StringBuilder unencryptedName = new StringBuilder(encryptedName);
		for (int i = 0; i < encryptedName.length(); i++) {
			char c = encryptedName.charAt(i);
			for (int j = 0; j < shiftFactor % 26; j++)
				if (Character.toString(c).matches("[a-y]"))
					c = (char) (c + 1);
				else if (c == 'z')
					c = 'a';
				else if (c == '-')
					c = ' ';
			unencryptedName.setCharAt(i, c);
		}
		return unencryptedName.toString();
	}
	
	static int sectorSum(List<String> input) {
		int sum = 0;
		for (String key : input) if (validateKey(key)) sum += Integer.parseInt(splitKey(key)[1]);
		return sum;
	}
	
	private static boolean validateKey(String keyString) {
		String[] key = splitKey(keyString);
		return key[2].equals(sortedString(key[0]).substring(0, 5));
	}
	
	private static String sortedString(String string) {
		// Count
		Map<Character, Integer> occurrenceCount = new HashMap<>();
		StringBuilder sb = new StringBuilder(string);
		for (int i = 0; i < sb.length(); i++) {
			occurrenceCount.merge(sb.charAt(i), 1, Integer::sum);
		}
		
		// Sort
		List<Entry<Character, Integer>> sortedList = new LinkedList<>(occurrenceCount.entrySet());
		sortedList.sort((o1, o2) -> {
			int valueCompareResult = Integer.compare(o2.getValue(), o1.getValue());
			return valueCompareResult == 0 ? Integer.compare(o1.getKey(), o2.getKey()) : valueCompareResult;
		});
		
		StringBuilder output = new StringBuilder();
		sortedList.forEach(characterIntegerEntry -> output.append(characterIntegerEntry.getKey()));
		return output.toString();
	}
	
	private static String[] splitKey(String key) {
		String[] output = new String[3];
		int split1 = key.lastIndexOf('-'), split2 = key.indexOf('[');
		output[0] = key.substring(0, split1).replaceAll("-", "");
		output[1] = key.substring(split1 + 1, split2);
		output[2] = key.substring(split2 + 1, key.length() - 1);
		return output;
	}
}

class RunDay4_Part1 {
	public static void main(String[] args) {
		System.out.println(DecoyData.sectorSum(DecoyData.input));
	}
}

class RunDay4_Part2 {
	public static void main(String[] args) {
		System.out.println(DecoyData.matchKey(DecoyData.input, "north"));
	}
}