import com.andre.Input;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andre on 1/7/2017.
 */
public class PasswordScrambler {
	public static final List<String> input = Input.readAllLines("Day 21/input.txt");
	
	public static String scramble(String string, List<String> instructions) {
		CharSequence cs = string;
		
		for (String instruction : instructions) {
			String[] tokens = instruction.trim().split(" ");
			
			if (instruction.matches("swap position .+ with position .+")) {
				cs = swapPosition(cs, Integer.parseInt(tokens[2]), Integer.parseInt(tokens[5]));
			} else if (instruction.matches("swap letter .+ with letter .+")) {
				cs = swapLetter(cs, tokens[2].charAt(0), tokens[5].charAt(0));
			} else if (instruction.matches("rotate right .+ step(s)?")) {
				cs = rotate(cs, Integer.parseInt(tokens[2]));
			} else if (instruction.matches("rotate left .+ step(s)?")) {
				cs = rotate(cs, -Integer.parseInt(tokens[2]));
			} else if (instruction.matches("rotate based on position of letter .+")) {
				cs = rotate(cs, tokens[6].charAt(0));
			} else if (instruction.matches("reverse positions .+ through .+"))
				cs = reverse(cs, Integer.parseInt(tokens[2]), Integer.parseInt(tokens[4]));
			else if (instruction.matches("move position .+ to position .+"))
				cs = move(cs, Integer.parseInt(tokens[2]), Integer.parseInt(tokens[5]));
			else {
				throw new RuntimeException("Invalid syntax encountered");
			}
		}
		
		return cs.toString();
	}
	
	public static String unscramble(String string, List<String> instructions) {
		LinkedList<String> reversed = new LinkedList<>();
		instructions.forEach(s -> reversed.addFirst(s));
		CharSequence cs = string;
		
		for (String instruction : reversed) {
			String[] tokens = instruction.trim().split(" ");
			
			if (instruction.matches("swap position .+ with position .+")) {
				cs = swapPosition(cs, Integer.parseInt(tokens[5]), Integer.parseInt(tokens[2]));
			} else if (instruction.matches("swap letter .+ with letter .+")) {
				cs = swapLetter(cs, tokens[2].charAt(0), tokens[5].charAt(0));
			} else if (instruction.matches("rotate right .+ step(s)?")) {
				cs = rotate(cs, -Integer.parseInt(tokens[2]));
			} else if (instruction.matches("rotate left .+ step(s)?")) {
				cs = rotate(cs, Integer.parseInt(tokens[2]));
			} else if (instruction.matches("rotate based on position of letter .+")) {
				cs = reverseRotate(cs, tokens[6].charAt(0));
			} else if (instruction.matches("reverse positions .+ through .+"))
				cs = reverse(cs, Integer.parseInt(tokens[2]), Integer.parseInt(tokens[4]));
			else if (instruction.matches("move position .+ to position .+"))
				cs = move(cs, Integer.parseInt(tokens[5]), Integer.parseInt(tokens[2]));
			else {
				throw new RuntimeException("Invalid syntax encountered");
			}
		}
		
		return cs.toString();
	}
	
	private static CharSequence swapPosition(CharSequence cs, int position1, int position2) {
		StringBuilder sb = new StringBuilder(cs);
		char temp = sb.charAt(position1);
		sb.setCharAt(position1, sb.charAt(position2));
		sb.setCharAt(position2, temp);
		return sb.toString();
	}
	
	private static CharSequence swapLetter(CharSequence cs, char letter1, char letter2) {
		return cs.toString().replace(letter1, (char) 0).replace(letter2, letter1).replace((char) 0, letter2);
	}
	
	private static CharSequence rotate(CharSequence cs, int offSet) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < cs.length(); i++) {
			int i_char = i - offSet;
			while (i_char < 0) i_char += cs.length();
			while (i_char >= cs.length()) i_char -= cs.length();
			sb.append(cs.charAt(i_char));
		}
		return sb.toString();
	}
	
	private static CharSequence rotate(CharSequence cs, char basedOn) {
		int offset = cs.toString().indexOf(basedOn);
		if (offset >= 4) offset++;
		offset++;
		
		return rotate(cs, offset);
	}
	
	private static CharSequence reverseRotate(CharSequence cs, char basedOn) {
		int offset = cs.toString().indexOf(basedOn);
		if (offset >= 4) offset++;
		offset++;
		
		return rotate(cs, -offset);
	}
	
	private static CharSequence reverse(CharSequence cs, int from, int to) {
		CharSequence subSequence = cs.subSequence(from, to + 1);
		
		StringBuilder reversed = new StringBuilder();
		for (int i = subSequence.length() - 1; i >= 0; i--) {
			reversed.append(subSequence.charAt(i));
		}
		
		return new StringBuilder(cs).replace(from, to + 1, reversed.toString()).toString();
	}
	
	private static CharSequence move(CharSequence cs, int from, int to) {
		StringBuilder sb = new StringBuilder(cs);
		char c = sb.charAt(from);
		sb.deleteCharAt(from);
		sb.insert(to, c);
		return sb.toString();
	}
	
	private static class Test {
		private static final List<String> testInput = Input.readAllLines("Day 21/test input.txt");
		
		public static void main(String[] args) {
			System.out.println(scramble("abcde", testInput));
			System.out.println(unscramble("dgfaehcb",input));
		}
	}
}

class RunDay21_Part1 {
	public static void main(String[] args) {
		System.out.println(PasswordScrambler.scramble("abcdefgh", PasswordScrambler.input));
	}
}

class RunDay21_Part2{
	public static void main(String[] args){
		System.out.println(PasswordScrambler.unscramble("fbgdceah",PasswordScrambler.input));
	}
}