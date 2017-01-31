import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Andre on 12/5/2016.
 */
public class PasswordCrack {
	public static final String input = "ojvtpuvg";
	
	private static final MessageDigest MD5;
	
	static {
		try {
			MD5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	public static String findPassword(String doorID) {
		System.out.println("Calculating...");
		
		char[] password = new char[8];
		
		int salt = 0;
		int i = 0;
		
		while (i < 8 && ++salt < Integer.MAX_VALUE) {
			String hash = DatatypeConverter.printHexBinary(MD5.digest((doorID + salt).getBytes()));
			
			if (hash.substring(0, 5).contains("00000")) {
				System.out.printf("%s ", hash.charAt(5));
				
				password[i] = Character.toLowerCase(hash.charAt(5));
				i++;
			}
		}
		System.out.println();
		
		return new String(password);
	}
	
	public static String findPassword2(String doorID) {
		System.out.println("Calculating...");
		
		char[] password = "________".toCharArray();
		
		int salt = 0;
		int remainingDigits = 8;
		
		while (remainingDigits > 0 && ++salt < Integer.MAX_VALUE) {
			String hash = DatatypeConverter.printHexBinary(MD5.digest((doorID + salt).getBytes()));
			
			if (hash.substring(0, 5).contains("00000")) {
				char charAt = hash.charAt(5);
				if (charAt <= '7') {
					int key = Character.digit(charAt, 10);
					
					if (password[key] == '_') {
						password[key] = Character.toLowerCase(hash.charAt(6));
						remainingDigits--;
						for (char c : password) {
							System.out.printf("%s ", c);
						}
						System.out.println();
					}
				}
			}
		}
		
		return new String(password);
	}
}

class RunDay5_Part1 {
	public static void main(String[] args) {
		System.out.println(PasswordCrack.findPassword(PasswordCrack.input));
	}
}

class RunDay5_Part2 {
	public static void main(String[] args) {
		System.out.println(PasswordCrack.findPassword2(PasswordCrack.input));
	}
}