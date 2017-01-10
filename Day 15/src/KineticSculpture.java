import andre.adventofcode.input.Input;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andre on 1/4/2017.
 */
public class KineticSculpture {
	private static class Disc {
		private final int positions;
		private int currentPosition;
		
		Disc(int positions, int currentPosition) {
			this.positions = positions;
			this.currentPosition = currentPosition;
		}
		
		boolean isOpenAt(int time) {
			return (currentPosition + time) % positions == 0;
		}
		
		@Override
		public String toString() {
			return String.format("%d, %d", positions, currentPosition);
		}
	}
	
	public static final List<String> input = Input.readAllLines("Day 15/input.txt");
	
	public static int timeToPress(List<String> input) {
		List<Disc> discs = new ArrayList<>(input.size());
		for (String s : input) {
			// Remove unnecessary keywords
			String[] tokens = s.replaceAll("Disc #. has | positions; at time=0, it is at position|\\.", "").split(" ");
			discs.add(new Disc(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1])));
		}
		
		seconds:
		for (int second = 0; second < 1_000_000_000; second++) {
			int secondWhileDropping = second;
			for (Disc disc : discs) {
				secondWhileDropping++;
				if (disc.isOpenAt(secondWhileDropping) == false) continue seconds;
			}
			return second;
		}
		
		return -1;
	}
	
	
	private static class Test {
		private static final List<String> testInput = Input.readAllLines("Day 15/test input.txt");
		
		public static void main(String[] args) {
			List<Disc> discs = new ArrayList<>(testInput.size());
			for (String s : testInput) {
				// Remove unnecessary keywords
				String[] tokens = s.replaceAll("Disc #. has | positions; at time=0, it is at position|\\.", "").split(" ");
				discs.add(new Disc(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1])));
			}
			System.out.println(discs.get(0).isOpenAt(6));
			System.out.println(discs.get(1).isOpenAt(7));
			
			
			System.out.println(timeToPress(testInput));
		}
	}
}

class RunDay15_Part1 {
	private static final List<String> inputPart2 =Input.readAllLines("Day 15/input part 2.txt");
	public static void main(String[] args) {
		
		System.out.println(KineticSculpture.timeToPress(inputPart2));
	}
}