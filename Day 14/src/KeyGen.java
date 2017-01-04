import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

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
	
	public static List<String> generateMD5Hashes(String seed) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		
		List<String> hashes = new ArrayList<>(hashSize);
		for (int i = 0; i < hashSize; i++) {
			hashes.add(DatatypeConverter.printHexBinary(md5.digest((seed + String.valueOf(i)).getBytes())));
		}
		
		System.out.println("Done generating hashes");
		return hashes;
	}
	
	public static List<String> generateStretchedMD5Hashes(String seed) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		
		List<String> hashes = new ArrayList<>(hashSize);
		String hash;
		for (int i = 0; i < hashSize; i++) {
			hash = DatatypeConverter.printHexBinary(md5.digest((seed + String.valueOf(i)).getBytes())).toLowerCase();
			for (int repetition = 0; repetition < 2016; repetition++) {
				hash = DatatypeConverter.printHexBinary(md5.digest(hash.getBytes())).toLowerCase();
			}
			hashes.add(hash);
		}
		
		System.out.println("Done generating stretched hashes");
		return hashes;
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
		System.out.println(KeyGen.indexOf64thKey(KeyGen.generateMD5Hashes(KeyGen.input)));
	}
}

class RunDay14_Part2 {
	public static void main(String[] args) {
		System.out.println(KeyGen.indexOf64thKey(KeyGen.generateStretchedMD5Hashes(KeyGen.input)));
	}
}