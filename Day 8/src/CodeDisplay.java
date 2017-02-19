import andre.adventofcode.input.Input;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 12/8/2016.
 */
public class CodeDisplay {
	public static final List<String> input = Input.readAllLines("Day 8/input.txt");
	
	private static final boolean ON = true, OFF = false;
	
	private final List<List<Boolean>> display;
	
	private int getRows() {
		return display.size();
	}
	
	private int getColumns() {
		return display.get(0).size();
	}
	
	public CodeDisplay() {
		display = new ArrayList<>();
		while (display.size() < 6) {
			List<Boolean> row = new ArrayList<>();
			while (row.size() < 50) {
				row.add(OFF);
			}
			display.add(row);
		}
	}
	
	public void doInstructions(List<String> instructions) {
		for (String instruction : instructions) {
			String[] tokens = instruction.trim().split("\\s+");
			
			if (tokens[0].equals("rect")) {
				String[] dimensions_string = tokens[1].split("x");
				lightUpperLeftPixels(Integer.parseInt(dimensions_string[1]), Integer.parseInt(dimensions_string[0]));
			} else if (tokens[1].equals("row")) {
				rotateRow(Integer.parseInt(tokens[2].split("=")[1]), Integer.parseInt(tokens[4]));
			} else {
				rotateColumn(Integer.parseInt(tokens[2].split("=")[1]), Integer.parseInt(tokens[4]));
			}
			
			// For the tampered thing
//		if (instruction.equals("rect 2x1"))
//			display.get(0).set(0, OFF);

//			System.out.println(instruction);
//			printDisplay();
		}
	}
	
	public int litPixels() {
		int litPixels = 0;
		
		for (List<Boolean> row : display) {
			for (Boolean pixel : row) {
				if (pixel == ON) {
					litPixels++;
				}
			}
		}
		
		return litPixels;
	}
	
	private void lightUpperLeftPixels(int rows, int columns) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				display.get(i).set(j, ON);
			}
		}
	}
	
	private void rotateRow(int row, int times) {
		for (int time = 0; time < times; time++) {
			boolean originalState = display.get(row).get(getColumns() - 1);
			for (int i = 0; i < getColumns(); i++) {
				boolean stateToSet = originalState;
				originalState = display.get(row).get(i);
				display.get(row).set(i, stateToSet);
			}
		}
	}
	
	private void rotateColumn(int column, int times) {
		for (int time = 0; time < times; time++) {
			boolean originalState = display.get(getRows() - 1).get(column);
			for (int i = 0; i < getRows(); i++) {
				boolean stateToSet = originalState;
				originalState = display.get(i).get(column);
				display.get(i).set(column, stateToSet);
			}
		}
	}
	
	public void printDisplay() {
		System.out.println("------------------------ Code ------------------------");
		for (int i = 0; i < display.size(); i++) {
			System.out.print(" [ ");
			for (int j = 0; j < display.get(i).size(); j++) {
				if (display.get(i).get(j) == OFF) {
					System.out.print(' ');
				} else if (display.get(i).get(j) == ON) {
					System.out.print('#');
				}
				System.out.print(' ');
			}
			System.out.print(" ] ");
			System.out.println();
		}
		System.out.println();
	}
}

class RunDay8_Part1 {
	public static void main(String[] args) {
		CodeDisplay codeDisplay = new CodeDisplay();
		codeDisplay.doInstructions(CodeDisplay.input);
		System.out.println(codeDisplay.litPixels());
	}
}

class RunDay8_Part2 {
	public static void main(String[] args) {
		CodeDisplay codeDisplay = new CodeDisplay();
		codeDisplay.doInstructions(CodeDisplay.input);
		codeDisplay.printDisplay();
	}
}