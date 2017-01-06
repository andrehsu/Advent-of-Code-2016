/**
 * Created by Andre on 1/6/2017.
 */
public class ElfParty {
	public static final int input = 3017957;
	
	public static int lastElfMethod1(int positions) {
		if (positions < 1) return -1;
		
		String binary = Integer.toBinaryString(positions);
		return Integer.parseInt(binary.substring(1) + binary.charAt(0), 2);
	}
	
	public static int lastElfMethod2(int positions) {
		
	}
	
	private static final class Test {
		public static void main(String[] args) {
			System.out.println(lastElfMethod1(5));
			System.out.println(lastElfMethod2(5));
		}
	}
}

class RunDay19_Part1 {
	public static void main(String[] args) {
		System.out.println(ElfParty.lastElfMethod1(ElfParty.input));
	}
}

class RunDay19_Part2 {
	public static void main(String[] args) {
		System.out.println(ElfParty.lastElfMethod2(ElfParty.input));
	}
}