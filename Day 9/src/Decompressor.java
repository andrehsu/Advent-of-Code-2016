import andre.adventofcode.input.Input;

import java.math.BigInteger;

/**
 * Created by andre on 12/11/2016.
 */
public class Decompressor {
	static final String fileURL = "Day 9/input.txt";
	static String input = Input.readFirstLine(Decompressor.fileURL);
	
	public static String decompress(String input) {
		StringBuilder sb = new StringBuilder(input);
		
		int i_startParenthesis = -1, i_endParenthesis = -1;
		
		for (int i = 0; i < sb.length(); i++) {
			char c = sb.charAt(i);
			
			if (c == '(') {
				i_startParenthesis = i;
			} else if (c == ')') {
				i_endParenthesis = i;
				
				String compressionSequence = sb.substring(i_startParenthesis, i_endParenthesis + 1);
				int[] compressionInfo = parseDecompressionSequence(compressionSequence);
				
				String compressionData = sb.substring(i_endParenthesis + 1, i_endParenthesis + 1 + compressionInfo[0]);
				
				sb.replace(i_startParenthesis, i_endParenthesis + 1 + compressionInfo[0], "");
				
				StringBuilder replacementSequence = new StringBuilder();
				for (int time = 0; time < compressionInfo[1]; time++) {
					replacementSequence.append(compressionData);
				}
				
				sb.insert(i_startParenthesis, replacementSequence.toString());
				
				i = i - compressionSequence.length() + compressionInfo[0] * compressionInfo[1];
			}
		}
		
		return sb.toString();
	}
	
	public static BigInteger v2CountCharacters(String input) {
		StringBuilder sb = new StringBuilder(input);
		
		int[] weight = new int[sb.length()];
		for (int i = 0; i < weight.length; i++) {
			weight[i] = 1;
		}
		
		int i_startParenthesis = -1, i_endParenthesis = -1;
		for (int i = 0; i < sb.length(); i++) {
			char c = sb.charAt(i);
			
			if (c == '(') {
				i_startParenthesis = i;
			} else if (c == ')') {
				i_endParenthesis = i;
				int[] compressionInfo = parseDecompressionSequence(sb.substring(i_startParenthesis, i_endParenthesis + 1));
				for (int j = i_endParenthesis + 1; j <= i_endParenthesis + compressionInfo[0]; j++) {
					weight[j] = compressionInfo[1] * weight[i_endParenthesis - 1];
				}
			}
		}
		
		BigInteger sumLength = BigInteger.ZERO;
		boolean inParenthesis = false;
		for (int i = 0; i < sb.length(); i++) {
			char c = sb.charAt(i);
			
			if (c == '(') {
				inParenthesis = true;
				continue;
			} else if (c == ')') {
				inParenthesis = false;
				continue;
			}
			
			if (!inParenthesis) {
				sumLength = sumLength.add(BigInteger.valueOf(weight[i]));
			}
		}
		
		return sumLength;
	}
	
	private static int[] parseDecompressionSequence(String sequence) {
		sequence = sequence.substring(1, sequence.length() - 1);
		
		String[] output_string = sequence.trim().split("x");
		
		return new int[]{Integer.parseInt(output_string[0]), Integer.parseInt(output_string[1])};
	}
	
	private static class Test {
		public static void main(String[] args) {
			System.out.println(decompress("ADVENT"));
			
			System.out.println(decompress("A(1x5)BC"));
			
			System.out.println(decompress("(3x3)XYZ"));
			
			System.out.println(decompress("A(2x2)BCD(2x2)EFG"));
			
			System.out.println(decompress("(6x1)(1x3)A"));
			
			System.out.println(decompress("X(8x2)(3x3)ABCY"));
		}
	}
	
	private static class TestPart2 {
		public static void main(String[] args) {
			System.out.println(v2CountCharacters("(3x3)XYZ"));
		}
	}
}

class RunDay9_Part1 {
	public static void main(String[] args) {
		System.out.println(Decompressor.input.length());
		System.out.println(Decompressor.decompress(Decompressor.input).length());
	}
}

class RunDay9_Part2 {
	public static void main(String[] args) {
		System.out.println(Decompressor.v2CountCharacters(Decompressor.input));
	}
}