import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Function;

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
		if (positions < 1) return -1;
		
		
		Deque<Integer> left = new LinkedList<>(),
				right = new LinkedList<>();
		for (int i = 1; i <= positions; i++) {
			if (i < positions / 2 + 1)
				left.addFirst(i);
			else
				right.addLast(i);
		}
		
		while (left.size() > 0 && right.size() > 0) {
			// Remove opposite
			if (left.size() > right.size())
				left.pollFirst();
			else
				right.pollFirst();
			
			// Rotate
			right.addLast(left.pollLast());
			left.addFirst(right.pollFirst());
		}
		return left.size() == 1 ? left.pollFirst() : right.pollFirst();
	}
	
	private static final class Test {
		public static void main(String[] args) {
			System.out.println(lastElfMethod1(5));
			System.out.println(lastElfMethod2(5));
		}
	}
}

class GenerateTable {
	public static void main(String[] args) {
		for (int i = 1; i <= 100; i++) {
			System.out.printf("%3d: %3d%n", i, ElfParty.lastElfMethod2(i));
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