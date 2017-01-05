/**
 * Created by Andre on 1/5/2017.
 */
public class DragonChecksum {
	public static final String input = "11011110011011101";
	public static final int inputLength = 272,
			part2InputLength = 35651584;
	
	public static String checksumOfFillerString(String input, int length) {
		while (input.length() < length) {
			input = nextString(input);
		}
		return checksum(input.substring(0, length));
	}
	
	private static String nextString(String input) {
		String b = input;
		b = flipBinary(reverse(b));
		return input + '0' + b;
	}
	
	private static String reverse(String input) {
		StringBuilder sb = new StringBuilder();
		for (int i = input.length() - 1; i >= 0; i--) {
			sb.append(input.charAt(i));
		}
		
		return sb.toString();
	}
	
	private static String flipBinary(String input) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == '1')
				sb.append(0);
			else
				sb.append(1);
		}
		
		return sb.toString();
	}
	
	private static String checksum(String input) {
		String output = input;
		
		while (output.length() % 2 == 0) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < output.length(); i += 2) {
				if (output.charAt(i) == output.charAt(i + 1)) {
					sb.append(1);
				} else
					sb.append(0);
			}
			output = sb.toString();
		}
		
		return output;
	}
	
	private static class Test {
		public static void main(String[] args) {
			System.out.println(checksum("110010110100"));
			System.out.println(checksumOfFillerString("10000", 20));
		}
	}
}

class RunDay16_Part1 {
	public static void main(String[] args) {
		System.out.println(DragonChecksum.checksumOfFillerString(DragonChecksum.input, DragonChecksum.inputLength));
	}
}

class RunDay16_Part2 {
	public static void main(String[] args){
		System.out.println(DragonChecksum.checksumOfFillerString(DragonChecksum.input,DragonChecksum.part2InputLength));
	}
}