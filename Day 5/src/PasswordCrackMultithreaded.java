import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.IntStream;

/**
 * Created by Andre on 1/31/2017.
 */
public class PasswordCrackMultithreaded {
	public static final String input = "ojvtpuvg";
	
	private static String hexHashOf(String input) {
		MessageDigest MD5;
		try {
			MD5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new Error(e);
		}
		return DatatypeConverter.printHexBinary(MD5.digest(input.getBytes()));
	}
	
	public static String findPassword(String prefix) {
		StringBuilder password = new StringBuilder();
		int seed = 1;
		while (password.length() < 8) {
			IntStream.range(seed, seed += 10_000)
					.parallel()
					.mapToObj(value -> hexHashOf(prefix + value))
					.filter(s -> {
						for (char c : s.substring(0, 5).toCharArray()) {
							if (c != '0')
								return false;
						}
						return true;
					})
					.map(s -> String.valueOf(s.charAt(5)))
					.forEachOrdered(password::append);
		}
		return password.substring(0, 8).toLowerCase();
	}
}

class TestRun {
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		System.out.println(PasswordCrackMultithreaded.findPassword(PasswordCrackMultithreaded.input));
		System.out.println(System.currentTimeMillis() - start + " ms");
	}
}
