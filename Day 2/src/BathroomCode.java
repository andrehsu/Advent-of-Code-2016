import andre.adventofcode.input.Input;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andre on 12/4/2016.
 */
public class BathroomCode {
	public static final List<String> input = Input.readAllLines("Day 2/input.txt");
	
	private final List<String> digitSequence;
	private final List<Character> digits = new ArrayList<>();
	private static final char LEFT = 'L', RIGHT = 'R', UP = 'U', DOWN = 'D';
	
	public String getDigits() {
		StringBuilder result = new StringBuilder();
		for (char digit : digits) {
			result.append(digit);
		}
		return result.toString();
	}
	
	public BathroomCode(List<String> input) {
		digitSequence = input;
	}
	
	public void run() {
		Key key1 = new Type1(); // Question 1
		Key key2 = new Type2(); // Question 2
		for (String digit : digitSequence) {
			for (char c : digit.toCharArray()) {
				switch (c) {
					case LEFT:
						key2 = key2.left();
						break;
					case RIGHT:
						key2 = key2.right();
						break;
					case UP:
						key2 = key2.up();
						break;
					case DOWN:
						key2 = key2.down();
						break;
					default:
						throw new RuntimeException("Invalid Input");
				}
			}
			
			digits.add(key2.value());
		}
	}
	
	private static class Run {
		public static void main(String[] args) {
			BathroomCode calculator = new BathroomCode(input);
			calculator.run();
			System.out.println(calculator.getDigits());
		}
	}
}