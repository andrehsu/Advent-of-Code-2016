import andre.adventofcode.input.Input;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andre on 1/24/2017.
 */
public class ClockSignal {
	public static final List<String> input = Input.readAllLines("Day 25/input.txt");
	
	private final List<String> code;
	private final int initializer;
	
	private int a,
			b = 0,
			c = 0,
			d = 0;
	
	private ClockSignal(List<String> code, int a) {
		this.code = code;
		this.a = a;
		this.initializer = a;
	}
	
	private String firstNOutput(int n) {
		final StringBuilder output = new StringBuilder();
		List<String[]> pregenTokens = new ArrayList<>();
		for (String s : code) {
			pregenTokens.add(s.trim().split(" "));
		}
		
		for (int line = 0; line < code.size(); line++) {
			String[] tokens = pregenTokens.get(line);
			switch (tokens[0]) {
				case "cpy":
					copy(tokens[2], tokens[1]);
					break;
				case "inc":
					increment(tokens[1]);
					break;
				case "dec":
					decrement(tokens[1]);
					break;
				case "jnz":
					if (parseParam(tokens[1]) != 0) {
						line = line - 1 + parseParam(tokens[2]);
					}
					break;
				case "out":
					output.append(parseParam(tokens[1]));
					if (output.length() == n)
						return output.toString();
					break;
			}
		}
		
		return output.toString();
	}
	
	private void increment(String register) {
		switch (register.charAt(0)) {
			case 'a':
				a++;
				break;
			case 'b':
				b++;
				break;
			case 'c':
				c++;
				break;
			case 'd':
				d++;
				break;
		}
	}
	
	private void decrement(String register) {
		switch (register.charAt(0)) {
			case 'a':
				a--;
				break;
			case 'b':
				b--;
				break;
			case 'c':
				c--;
				break;
			case 'd':
				d--;
				break;
		}
	}
	
	private void copy(String register, String param) {
		switch (register.charAt(0)) {
			case 'a':
				a = parseParam(param);
				break;
			case 'b':
				b = parseParam(param);
				break;
			case 'c':
				c = parseParam(param);
				break;
			case 'd':
				d = parseParam(param);
				break;
		}
	}
	
	private int parseParam(String param) {
		try {
			return Integer.parseInt(param);
		} catch (NumberFormatException e) {
			switch (param.charAt(0)) {
				case 'a':
					return a;
				case 'b':
					return b;
				case 'c':
					return c;
				case 'd':
					return d;
			}
		}
		
		throw new RuntimeException("Invalid param");
	}
	
	public static int lowestInitializer(List<String> input) {
		for (int i = 1; i < 10_000; i++) {
			ClockSignal instance = new ClockSignal(input, i);
			if (instance.firstNOutput(12).equals("010101010101"))
				return i;
		}
		
		return -1;
	}
	
	private static class Test {
		public static void main(String[] args) {
			ClockSignal instance = new ClockSignal(input, 198);
			System.out.println(instance.firstNOutput(25));
		}
	}
}

class RunDay25Part1 {
	public static void main(String... args) {
		System.out.println(ClockSignal.lowestInitializer(ClockSignal.input));
	}
}