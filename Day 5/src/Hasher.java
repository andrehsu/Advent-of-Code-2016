import javafx.beans.binding.StringBinding;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by Andre on 12/5/2016.
 */
public class Hasher {
	private static String findPassword(String doorID) {
		System.out.println("Calculating...");
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			System.exit(1);
		}
		
		char[] password = new char[8];
		
		int index = 0;
		int i_password = 0;
		outer:
		while (i_password < 8) {
			index++;
			
			if (index == Integer.MAX_VALUE) {
				return null;
			}
			
			byte[] digest = md5.digest((doorID + Integer.toString(index)).getBytes());
			StringBuilder sb = new StringBuilder();
			for (byte b : digest) {
				sb.append(String.format("%02X", b));
			}
			String hash = sb.toString();
			
			for (int i = 0; i < 5; i++) {
				if (hash.charAt(i) != '0') {
					continue outer;
				}
			}
			
			System.out.printf("%s ", hash.charAt(5));
			
			password[i_password] = hash.charAt(5);
			i_password++;
		}
		System.out.println();
		
		StringBuilder result = new StringBuilder(8);
		for (char digit : password) {
			result.append(digit);
		}
		return result.toString();
	}
	
	private static String findPassword2(String doorID) {
		System.out.println("Calculating...");
		
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		char[] password = new char[8];
		for (int i = 0; i < password.length; i++) {
			password[i] = '_';
		}
		boolean[] foundDigit = new boolean[8];
		
		int seed = 0;
		int remainingDigits = 8;
		
		outer:
		while (remainingDigits > 0) {
			seed++;
			
			if (seed == Integer.MAX_VALUE) {
				return null;
			}
			
			byte[] digest = md5.digest((doorID + Integer.toString(seed)).getBytes());
			StringBuilder sb = new StringBuilder(digest.length * 2);
			for (byte b : digest) {
				sb.append(String.format("%02X", b));
			}
			String hash = sb.toString();
			
			for (int i = 0; i < 5; i++) {
				if (hash.charAt(i) != '0') {
					continue outer;
				}
			}
			
			int key = Character.digit(hash.charAt(5), 10);
			char value = hash.charAt(6);
			
			if (key > 7 || key == -1) {
				continue;
			}
			
			if (!foundDigit[key]) {
				foundDigit[key] = true;
				password[key] = value;
				remainingDigits--;
				for (char c : password) {
					System.out.printf("%s ", c);
				}
				System.out.println();
			}
		}
		
		StringBuilder result = new StringBuilder(8);
		for (char digit : password) {
			result.append(digit);
		}
		return result.toString();
	}
	
	private static class Run {
		public static void main(String[] args) {
			String input = "ojvtpuvg";
			String testInput = "abc";
			
			String actualInput = input;
			
			//System.out.printf("For input %s, password 1 is %s%n", actualInput, findPassword(actualInput));
			System.out.printf("For input %s, password 2 is %s%n", actualInput, findPassword2(actualInput));
		}
	}
}
