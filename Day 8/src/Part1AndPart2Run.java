import com.andre.Input;

import java.util.List;

/**
 * Created by andre on 12/8/2016.
 */
public class Part1AndPart2Run {
	public static void main(String[] args) {
		List<String> instructions = Input.readAllLines(CodeDisplay.inputURL);
		
		CodeDisplay display = new CodeDisplay();
		
		display.doInstructions(instructions);
		
		display.printDisplay();
		
		System.out.println("Lit pixels: " + display.litPixels());
	}
}
