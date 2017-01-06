/**
 * Created by Andre on 1/6/2017.
 */
public class TestFunction {
	public static void main(String[] args) {
		for (int i = 1; i <= 64; i++) {
			System.out.printf("%d: %d%n", i, lastElf(i));
		}
	}
	
	private static int lastElf(int positions) {
		if (positions < 1) return -1;
		
		String binary = Integer.toBinaryString(positions);
		return Integer.parseInt(binary.substring(1) + binary.charAt(0), 2);
	}
}
