import andre.adventofcode.input.Input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andre on 1/24/2017.
 */
public class ClockSignal {
	public static final List<String> input = Input.readAllLines("Day 25/input.txt");
	
	private final List<String> code;
	private final int initializer;
	private final int firstNOutput;
	private final Map<Character, Integer> registers = new HashMap<>();
	private final StringBuilder output = new StringBuilder();
	
	private ClockSignal(List<String> code, int a, int firstNOutput) {
		this.code = code;
		registers.put('a', a);
		registers.put('b', 0);
		registers.put('c', 0);
		registers.put('d', 0);
		this.initializer = a;
		this.firstNOutput = firstNOutput;
	}
	
	private String firstNOutput() {
		List<String[]> pregenTokens = new ArrayList<>();
		for (String s : code) {
			pregenTokens.add(s.trim().split(" "));
		}
		
		for (int line = 0; line < code.size(); line++) {
			String[] tokens = pregenTokens.get(line);
			switch (tokens[0]) {
				case "cpy":
					copy(tokens);
					break;
				case "inc":
					increment(tokens);
					break;
				case "dec":
					decrement(tokens);
					break;
				case "jnz":
					line = line + jnz(tokens) - 1;
					break;
				case "out":
					output(tokens);
					if (output.length() == firstNOutput)
						return output.toString();
					else if (output.length() == 2) {
						if (!output.substring(0, 2).equals("01"))
							return "Invalid";
					}
					break;
			}
		}
		
		return output.toString();
	}
	
	private void increment(String[] tokens) {
		char c = tokens[1].charAt(0);
		if (registers.containsKey(c))
			registers.merge(c, 1, Integer::sum);
		else
			throw new RuntimeException("Invalid param for inc");
	}
	
	private void decrement(String[] tokens) {
		char c = tokens[1].charAt(0);
		if (registers.containsKey(c))
			registers.merge(c, -1, Integer::sum);
		else
			throw new RuntimeException("Invalid param for dec");
	}
	
	private void copy(String[] tokens) {
		String from = tokens[1];
		char to = tokens[2].charAt(0);
		if (registers.containsKey(to)) {
			registers.put(to, parseParam(from));
		} else
			throw new RuntimeException("Invalid param for cpy");
	}
	
	private void output(String[] tokens) {
		output.append(parseParam(tokens[1]));
	}
	
	private int jnz(String[] tokens) {
		if (parseParam(tokens[1]) != 0) {
			return parseParam(tokens[2]);
		} else
			return 1;
	}
	
	private int parseParam(String param) {
		try {
			return Integer.parseInt(param);
		} catch (NumberFormatException e) {
			char c = param.charAt(0);
			if (registers.containsKey(c))
				return registers.get(c);
		}
		
		throw new RuntimeException("Invalid param");
	}
	
	public static int lowestInitializer(List<String> input) {
		for (int i = 1; i < 10_000; i++) {
			ClockSignal instance = new ClockSignal(input, i, 12);
			if (instance.firstNOutput().equals("010101010101"))
				return i;
		}
		
		return -1;
	}
	
	private static class Test {
		public static void main(String[] args) {
			ClockSignal instance = new ClockSignal(input, 198, 25);
			System.out.println(instance.firstNOutput());
		}
	}
}

class RunDay25_Part1 {
	public static void main(String... args) {
		System.out.println(ClockSignal.lowestInitializer(ClockSignal.input));
	}
}