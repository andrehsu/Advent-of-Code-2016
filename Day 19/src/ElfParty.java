import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

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
	
	public static int lastElfMethod1_MyImple(int positions) {
		Deque<Integer> elves = new ArrayDeque<>(positions);
		for (int i = 1; i <= positions; i++) elves.addLast(i);
		
		elves.addLast(elves.removeFirst());
		while (elves.size() > 1) {
			elves.removeFirst();
			elves.addLast(elves.removeFirst());
		}
		
		return elves.remove();
	}
	
	public static int lastElfMethod2(int positions) {
		// kinda stole it from reddit
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
	
	public static int lastElfMethod2_SlowImple(int positions) {
		LinkedList<Integer> elves = new LinkedList<>();
		for (int i = 1; i <= positions; i++) {
			elves.addLast(i);
		}
		
		while (elves.size() > 1) {
			elves.remove(elves.size() / 2);
			elves.addLast(elves.removeFirst());
		}
		
		return elves.remove();
	}
	
	public static int lastElfMethod2_MathImple(int positions) {
		int closetPowerOfThree = (int) Math.pow(3, (int) (Math.log(positions) / Math.log(3)));
		
		int index = closetPowerOfThree;
		int lastElf = 1;
		while (++index < positions) {
			if (lastElf < closetPowerOfThree)
				lastElf++;
			else
				lastElf += 2;
		}
		
		return lastElf;
	}
}

class RunDay19_Part1 {
	public static void main(String[] args) {
		System.out.println(ElfParty.lastElfMethod1(ElfParty.input));
	}
}

class RunDay19_Part1_MyImple {
	public static void main(String... args) {
		System.out.println(ElfParty.lastElfMethod1_MyImple(ElfParty.input));
	}
}

class RunDay19_Part2 {
	public static void main(String[] args) {
		System.out.println(ElfParty.lastElfMethod2(ElfParty.input));
	}
}

class RunDay19_Part2_MathImple {
	public static void main(String... args) {
		System.out.println(ElfParty.lastElfMethod2_MathImple(ElfParty.input));
	}
}

class GenTable {
	public static void main(String... args) {
		for (int i = 1; i <= 500; i++) {
			System.out.printf("%4d: %d%n", i, ElfParty.lastElfMethod2_SlowImple(i));
		}
	}
}