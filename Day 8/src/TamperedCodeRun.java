import andre.adventofcode.input.Input;

/**
 * Created by andre on 12/11/2016.
 * by a reddit guy on the solutions place
 */
public class TamperedCodeRun {
	public static void main(String[] args){
		CodeDisplay display = new CodeDisplay();
		display.doInstructions(Input.readAllLines("Day 8/tamperedInput.txt"));
		display.printDisplay();
	}
}
