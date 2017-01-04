import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;

/**
 * Created by Andre on 12/5/2016.
 */
public class DecoyData {
	
	private static String matchKey(List<String> keys, String match) {
		for (String key : keys) {
			String[] keySplit = splitKey(key);
			String unencryptedName = unencryptName(keySplit[0], Integer.parseInt(keySplit[1]));
			if (unencryptedName.contains(match)) {
				return key;
			}
		}
		return null;
	}
	
	private static String unencryptName(String encryptedName, int shiftFactor) {
		StringBuilder unencryptedName = new StringBuilder(encryptedName);
		
		for (int i = 0; i < encryptedName.length(); i++) {
			char c = encryptedName.charAt(i);
			
			for (int j = 0; j < shiftFactor % 26; j++) {
				if (Character.toString(c).matches("[a-y]")) {
					c = ((char) (c + 1));
				} else if (c == 'z') {
					c = 'a';
				} else if (c == '-') {
					c = ' ';
				}
			}
			
			unencryptedName.setCharAt(i, c);
		}
		
		return unencryptedName.toString();
	}
	
	private static int sectorSum(List<String> keys) {
		int sum = 0;
		for (String key : keys) {
			if (validateKey(key)) {
				sum += Integer.parseInt(splitKey(key)[1]);
			}
		}
		
		return sum;
	}
	
	private static boolean validateKey(String keyString) {
		String[] key = splitKey(keyString);
		
		List<Entry<Character, Integer>> occurrenceCount = new ArrayList<>(countCharOccurrence(key[0]).entrySet());
		
		StringBuilder topFive = new StringBuilder();
		for (int i = 0; i < 5; i++) {
			topFive.append(occurrenceCount.get(i).getKey());
		}
		
		if (key[2].equals(topFive.toString())) {
			return true;
		} else
			return false;
	}
	
	private static Map<Character, Integer> countCharOccurrence(String string) {
		Map<Character, Integer> occurrenceCount = new HashMap<>();
		
		StringBuilder sb = new StringBuilder(string);
		for (int i = 0; i < sb.length(); i++) {
			char c = sb.charAt(i);
			
			occurrenceCount.put(c, occurrenceCount.get(c) == null ? 1 : occurrenceCount.get(c) + 1);
		}
		
		List<Entry<Character, Integer>> sortedList = new LinkedList<>(occurrenceCount.entrySet());
		
		Collections.sort(sortedList, new Comparator<Entry<Character, Integer>>() {
			@Override
			public int compare(Entry<Character, Integer> o1, Entry<Character, Integer> o2) {
				int valueCompareResult = Integer.compare(o2.getValue(), o1.getValue());
				if (valueCompareResult == 0) {
					return Integer.compare(o1.getKey(), o2.getKey());
				} else {
					return valueCompareResult;
				}
			}
		});
		
		Map<Character, Integer> sortedOccurrenceMap = new LinkedHashMap<>();
		
		for (Entry<Character, Integer> entry : sortedList) {
			sortedOccurrenceMap.put(entry.getKey(), entry.getValue());
		}
		
		return sortedOccurrenceMap;
	}
	
	
	private static String[] splitKey(String key) {
		String[] output = new String[3];
		
		int split1 = 0, split2 = 0;
		for (int i = 0; i < key.length(); i++) {
			char character = key.charAt(i);
			if (character == '[')
				split2 = i;
			if (character == '-')
				split1 = i;
		}
		
		output[0] = key.substring(0, split1);
		output[1] = key.substring(split1 + 1, split2);
		output[2] = key.substring(split2 + 1, key.length() - 1);
		
		StringBuilder sb = new StringBuilder(output[0]);
		for (int i = 0; i < sb.length(); i++) {
			if (sb.charAt(i) == '-') {
				sb.deleteCharAt(i);
			}
		}
		output[0] = sb.toString();
		
		return output;
	}
	
	private static class Run {
		public static void main(String[] args) throws IOException {
			List<String> keys = Files.readAllLines(Paths.get("Day 4/input.txt"));
			
			System.out.println(sectorSum(keys));
			
			System.out.println(matchKey(keys, "north"));
		}
	}
}
