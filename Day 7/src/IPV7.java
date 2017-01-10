import andre.adventofcode.input.Input;
import javafx.util.Pair;

import java.util.*;

class IPV7 {
	private static final String inputURL = "Day 7/input.txt";
	
	static int numSupportTLS(List<String> addresses) {
		int numSupported = 0;
		for (Iterator<String> iterator = addresses.iterator(); iterator.hasNext(); ) {
			String next = iterator.next();
			if (supportsTLS(next)) {
				numSupported++;
			}
		}
		
		return numSupported;
	}
	
	private static boolean supportsTLS(String address) {
		Pair<List<String>, List<String>> netSections = parseAddress(address);
		List<String> supernet = netSections.getKey();
		List<String> hypernet = netSections.getValue();
		
		for (String s : hypernet) {
			if (containsTLSSequence(s)) {
				return false;
			}
		}
		
		for (String s : supernet) {
			if (containsTLSSequence(s)) {
				return true;
			}
		}
		
		return false;
	}
	
	private static boolean containsTLSSequence(String string) {
		if (string.length() < 4) {
			return false;
		}
		
		for (int i = 0; i < string.length() - 3; i++) {
			if (string.charAt(i) == string.charAt(i + 3) && string.charAt(i + 1) == string.charAt(i + 2) && string.charAt(i) != string.charAt(i + 1))
				return true;
		}
		
		return false;
	}
	
	private static int numSupportSLS(List<String> addresses) {
		int numSupportSLS = 0;
		
		for (String address : addresses) {
			if (supportsSLS(address)) {
				numSupportSLS++;
			}
		}
		
		return numSupportSLS;
	}
	
	private static boolean supportsSLS(String address) {
		Pair<List<String>, List<String>> output = parseAddress(address);
		List<String> supernet = output.getKey();
		List<String> hypernet = output.getValue();
		
		Set<String> validSectionsInSupernet = new HashSet<>();
		
		for (String s : supernet) {
			validSectionsInSupernet.addAll(SLSSequences(s));
		}
				
		for (String s : hypernet) {
			Set<String> validSectionsInHypernet = SLSSequences(s);
			for (String s1 : validSectionsInHypernet) {
				if(validSectionsInSupernet.contains(reverse(s1))){
					return true;
				}
			}
		}
		
		return false;
	}
	
	private static String reverse(String section){
		if (section.length()!= 3){
			return null;
		}
		
		return String.valueOf(section.charAt(1)) + section.charAt(0) + section.charAt(1);
	}
	
	private static Set<String> SLSSequences(String section) {
		Set<String> output = new HashSet<>();
		
		if (section.length() < 3) {
			return output;
		}
		
		for (int i = 0; i < section.length() - 2; i++) {
			if (section.charAt(i) == section.charAt(i + 2) && section.charAt(i) != section.charAt(i + 1)) {
				output.add(section.substring(i, i + 3));
			}
		}
		
		return output;
	}
	
	private static Pair<List<String>, List<String>> parseAddress(String address) {
		List<String> supernet, hypernet = new ArrayList<>();
		
		StringBuilder sb = new StringBuilder(address);
		
		int i_sBracketStart = 0, i_sBracketEnd = -1;
		for (int i = 0; i < sb.length(); i++) {
			char c = sb.charAt(i);
			if (c == '[') {
				i_sBracketStart = i;
			} else if (c == ']') {
				i_sBracketEnd = i;
				
				hypernet.add(sb.substring(i_sBracketStart + 1, i_sBracketEnd));
				
				sb.replace(i_sBracketStart, i_sBracketEnd + 1, " ");
				i = i_sBracketStart;
			}
		}
		
		supernet = new ArrayList<>(Arrays.asList(sb.toString().trim().split(" ")));
		
		return new Pair<>(supernet, hypernet);
	}
	
	private static class Part1Test {
		public static void main(String[] args) {
			System.out.println(supportsTLS("bqooxxweoytjghrqn[hkwwukixothfyglw]kpasnmikmbzcbfi[vlnyszifsaaicagxtqf]ucdyxasusefuuxlx"));
			System.out.println(supportsTLS("abba[mnop]qrst")); // true
			System.out.println(supportsTLS("abcd[bddb]xyyx")); // false
			System.out.println(supportsTLS("aaaa[qwer]tyui")); // false
			System.out.println(supportsTLS("ioxxoj[asdfgh]zxcvbn")); // true
		}
	}
	
	private static class Part1Run {
		public static void main(String[] args) {
			List<String> input = Input.readAllLines(inputURL);
			
			System.out.println(numSupportTLS(input));
		}
	}
	
	private static class Part2Test {
		public static void main(String[] args) {
			System.out.println(supportsSLS("aba[bab]xyz")); // true
			System.out.println(supportsSLS("xyx[xyx]xyx")); // false
			System.out.println(supportsSLS("aaa[kek]eke")); // true
			System.out.println(supportsSLS("zazbz[bzb]cdb")); // true
		}
	}
	
	private static class Part2Run {
		public static void main(String[] args) {
			List<String> input = Input.readAllLines(inputURL);
			
			System.out.println(numSupportSLS(input));
		}
	}
}