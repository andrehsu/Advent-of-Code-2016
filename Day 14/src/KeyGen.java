import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import static java.lang.System.currentTimeMillis;

/**
 * Created by Andre on 1/4/2017.
 */
public class KeyGen {
	public static final String input = "yjdafjpo";
	
	private static final int hashSize = 30_000; // Smaller for faster run time
	
	public static int indexOf64thKey(List<String> hashes) {
		int keyCount = 0;
		for (int i = 0; i < hashes.size() - 1000; i++) {
			String hash = hashes.get(i);
			
			char repeatedCharacter = 0;
			for (int i_char = 0; i_char < hash.length() - 2; i_char++) {
				if (hash.charAt(i_char) == hash.charAt(i_char + 1) && hash.charAt(i_char + 1) == hash.charAt(i_char + 2)) {
					repeatedCharacter = hash.charAt(i_char);
					break;
				}
			}
			
			if (repeatedCharacter == 0) continue;
			
			String repeatSequence;
			{
				StringBuilder repeatedSequence_sb = new StringBuilder();
				for (int j = 0; j < 5; j++) {
					repeatedSequence_sb.append(repeatedCharacter);
				}
				repeatSequence = repeatedSequence_sb.toString();
			}
			
			for (int j = 1; j <= 1000; j++) {
				if (hashes.get(i + j).contains(repeatSequence)) {
					keyCount++;
					if (keyCount == 64)
						return i;
				}
			}
		}
		
		return -1;
	}
	
	private static String MD5HashOf(String seed, int i) {
		MessageDigest MD5;
		try {
			MD5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		
		return DatatypeConverter.printHexBinary(MD5.digest((seed + i).getBytes()));
	}
	
	public static List<String> generateMD5Hashes(String seed) {
		Map<Integer, String> hashes = new ConcurrentHashMap<>();
		
		long start = currentTimeMillis();
		IntStream.range(0, hashSize).parallel().forEach(value -> hashes.put(value, MD5HashOf(seed, value)));
		System.out.println(currentTimeMillis() - start);
		
		System.out.println("Done generating hashes");
		List<String> hashList = new ArrayList<>(hashSize);
		IntStream.range(0, hashSize).forEach(value -> hashList.add(hashes.get(value)));
		return hashList;
	}
	
	public static List<String> generateStretchedMD5Hashes(String seed) {
		Map<Integer, String> hashes = new ConcurrentHashMap<>();
		
		// Before parallel stream optimization: 38430 ms, now: 14607 ms
		IntStream.range(0, hashSize).parallel().forEach(value -> hashes.put(value, stretchedMD5HashOf(seed, value)));
		
		System.out.println("Done generating stretched hashes");
		List<String> hashList = new ArrayList<>(hashSize);
		IntStream.range(0, hashSize).forEach(value -> hashList.add(hashes.get(value)));
		return hashList;
	}
	
	private static String stretchedMD5HashOf(String seed, int i) {
		MessageDigest MD5;
		try {
			MD5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		
		String hash;
		hash = DatatypeConverter.printHexBinary(MD5.digest((seed + i).getBytes())).toLowerCase();
		for (int repetition = 0; repetition < 2016; repetition++) {
			hash = DatatypeConverter.printHexBinary(MD5.digest(hash.getBytes())).toLowerCase();
		}
		return hash;
	}
	
	private static class Test {
		public static void main(String[] args) {
			System.out.println(indexOf64thKey(generateMD5Hashes("abc")));
			System.out.println(indexOf64thKey(generateStretchedMD5Hashes("abc")));
		}
	}
}

class RunDay14_Part1 {
	public static void main(String[] args) {
		long start = currentTimeMillis();
		System.out.println(KeyGen.indexOf64thKey(KeyGen.generateMD5Hashes(KeyGen.input)));
		System.out.println(currentTimeMillis() - start);
	}
}

class RunDay14_Part2 {
	public static void main(String[] args) {
		long start = currentTimeMillis();
		System.out.println(KeyGen.indexOf64thKey(KeyGen.generateStretchedMD5Hashes(KeyGen.input)));
		System.out.println(currentTimeMillis() - start);
	}
}