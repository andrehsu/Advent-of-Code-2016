import andre.adventofcode.input.Input;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andre on 1/10/2017.
 */
public class Assembunny2Interpreter {
	public static final List<String> input = Input.readAllLines("Day 23/input.txt"),
			input2 = Input.readAllLines("Day 23/optimized input.txt");
	
	public static int interpret(List<String> input, int eggs) {
		Assembunny2Interpreter interpreter = new Assembunny2Interpreter(input, eggs);
		interpreter.run();
		return interpreter.getA();
	}
	
	private final List<String> code;
	private Map<Character, Integer> registers;
	private final int eggs;
	
	private int getA() {
		return registers.get('a');
	}
	
	private Assembunny2Interpreter(List<String> code, int eggs) {
		this.code = code;
		this.eggs = eggs;
	}
	
	private void run() {
		registers = new HashMap<>();
		registers.put('a', eggs);
		registers.put('b', 0);
		registers.put('c', 0);
		registers.put('d', 0);
		
		int lineProcessed = 0;
		for (int i = 0; i < code.size(); i++) {
			lineProcessed++;
			String[] line_tokens = code.get(i).trim().split("\\s+");
			switch (line_tokens[0]) {
				case "cpy":
					copy(line_tokens[1], line_tokens[2]);
					break;
				case "inc":
					increment(line_tokens[1]);
					break;
				case "dec":
					decrement(line_tokens[1]);
					break;
				case "jnz":
					i += jumpIf(line_tokens[1], line_tokens[2]) - 1;
					break;
				case "tgl":
					toggle(line_tokens[1], i);
					break;
				case "mul":
					multiply(line_tokens[1], Arrays.copyOfRange(line_tokens, 2, line_tokens.length));
			}
		}
	}
	
	private void increment(String param) {
		if (registers.containsKey(param.charAt(0))) registers.merge(param.charAt(0), 1, Integer::sum);
	}
	
	private void multiply(String param, String... params) {
		int product = 1;
		for (String s : params) {
			product *= parseParam(s);
		}
		
		if (registers.containsKey(param.charAt(0)))
			registers.put(param.charAt(0), product);
	}
	
	private void decrement(String param) {
		if (registers.containsKey(param.charAt(0))) registers.merge(param.charAt(0), -1, Integer::sum);
	}
	
	private void copy(String from, String to) {
		if (registers.containsKey(to.charAt(0))) {
			char toRegister = to.charAt(0);
			registers.put(toRegister, parseParam(from));
		}
	}
	
	private int jumpIf(String param, String to) {
		return parseParam(param) != 0 ? parseParam(to) : 1;
	}
	
	private void toggle(String param, int currentLine) {
		int lineToChange = currentLine + parseParam(param);
		if (lineToChange < 0 || lineToChange >= code.size()) return;
		
		String line = code.get(lineToChange);
		int argumentSize = line.trim().split("\\s+").length - 1;
		if (argumentSize == 1) {
			if (line.contains("inc"))
				code.set(lineToChange, line.replace("inc", "dec"));
			else if (line.contains("tgl"))
				code.set(lineToChange, line.replace("tgl", "inc"));
			else if (line.contains("dec"))
				code.set(lineToChange, line.replace("dec", "inc"));
		} else if (argumentSize == 2) {
			if (line.contains("jnz"))
				code.set(lineToChange, line.replace("jnz", "cpy"));
			else if (line.contains("cpy"))
				code.set(lineToChange, line.replace("cpy", "jnz"));
		}
	}
	
	private int parseParam(String param) {
		try {
			return Integer.parseInt(param);
		} catch (NumberFormatException e) {
			return registers.get(param.charAt(0));
		}
	}
	
	private static class Test {
		private static final List<String> testInput = Input.readAllLines("Day 23/test input.txt");
		
		public static void main(String[] args) {
			System.out.println(interpret(testInput, 7));
		}
	}
}

class RunDay23_Part1 {
	public static void main(String[] args) {
		System.out.println(Assembunny2Interpreter.interpret(Assembunny2Interpreter.input, 7));
	}
}

class RunDay23_Part2 {
	public static void main(String[] args) {
		System.out.println(Assembunny2Interpreter.interpret(Assembunny2Interpreter.input2, 12));
	}
}