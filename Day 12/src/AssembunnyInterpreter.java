import com.andre.Input;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andre on 12/13/2016.
 */
public class AssembunnyInterpreter {
	public static final List<String> input = Input.readAllLines("Day 12/input.txt");
	
	private final Map<String, Integer> registers = new HashMap<>();
	
	public AssembunnyInterpreter() {
		this(0, 0, 0, 0);
	}
	
	public AssembunnyInterpreter(int a, int b, int c, int d) {
		registers.put("a", a);
		registers.put("b", b);
		registers.put("c", c);
		registers.put("d", d);
	}
	
	public Integer getRegister(String identifier) {
		return registers.get(identifier);
	}
	
	private Integer getValue(String identifier) {
		if (isInteger(identifier)) {
			return Integer.parseInt(identifier);
		} else {
			return getRegister(identifier);
		}
	}
	
	private boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public void runCode(List<String> code) {
		for (int i = 0; i < code.size(); i++) {
			String line = code.get(i);
			
			String[] tokens = line.split(" ");
			switch (tokens[0]) {
				case "cpy":
					registers.put(tokens[2], getValue(tokens[1]));
					break;
				case "inc":
					registers.put(tokens[1], registers.get(tokens[1]) + 1);
					break;
				case "dec":
					registers.put(tokens[1], registers.get(tokens[1]) - 1);
					break;
				case "jnz":
					if (getValue(tokens[1]) != 0) {
						i = i + getValue(tokens[2]) - 1;
					}
					break;
			}
		}
	}
	
	private static class Test {
		public static void main(String[] args) {
			
		}
	}
}

class RunDay12Part1 {
	public static void main(String[] args) {
		AssembunnyInterpreter interpreter = new AssembunnyInterpreter();
		interpreter.runCode(AssembunnyInterpreter.input);
		
		System.out.println(interpreter.getRegister("a"));
	}
}

class RunDay12Part2 {
	public static void main(String[] args){
		AssembunnyInterpreter interpreter = new AssembunnyInterpreter(0,0,1,0);
		interpreter.runCode(AssembunnyInterpreter.input);
		
		System.out.println(interpreter.getRegister("a"));
	}
}