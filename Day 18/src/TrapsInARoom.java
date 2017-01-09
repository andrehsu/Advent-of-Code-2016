import com.andre.Input;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andre on 1/6/2017.
 */
public class TrapsInARoom {
	private enum Tile {
		SAFE('.'),
		TRAPPED('^');
		
		private final char charValue;
		
		char charValue() {
			return charValue;
		}
		
		Tile(char charValue) {
			this.charValue = charValue;
		}
	}
	
	public static final String input = Input.readFirstLine("Day 18/input.txt");
	
	public static List<List<Tile>> generateFloor(String input, final int rows) {
		final int columns = input.length();
		List<List<Tile>> floor = new ArrayList<>(rows);
		
		// First row with given input
		floor.add(new ArrayList<>(columns + 2));
		floor.get(0).add(Tile.SAFE);
		for (int i = 0; i < columns; i++) {
			if (input.charAt(i) == '^')
				floor.get(0).add(Tile.TRAPPED);
			else
				floor.get(0).add(Tile.SAFE);
		}
		floor.get(0).add(Tile.SAFE);
		
		for (int i = 1; i < rows; i++) {
			floor.add(new ArrayList<>(columns + 2));
			
			List<Tile> thisFloor = floor.get(i),
					lastFloor = floor.get(i - 1);
			
			thisFloor.add(Tile.SAFE);
			for (int j = 1; j <= columns; j++) {
				Tile left = lastFloor.get(j - 1),
						center = lastFloor.get(j),
						right = lastFloor.get(j + 1);
				
				if (left == Tile.TRAPPED && center == Tile.TRAPPED && right == Tile.SAFE ||
						left == Tile.SAFE && center == Tile.TRAPPED && right == Tile.TRAPPED ||
						left == Tile.TRAPPED && center == Tile.SAFE && right == Tile.SAFE ||
						left == Tile.SAFE && center == Tile.SAFE && right == Tile.TRAPPED) {
					thisFloor.add(Tile.TRAPPED);
				} else
					thisFloor.add(Tile.SAFE);
			}
			thisFloor.add(Tile.SAFE);
		}
		
		return floor;
	}
	
	public static void printFloor(List<List<Tile>> floor) {
		for (List<Tile> row : floor) {
			for (int i = 1; i <= row.size() - 2; i++) {
				System.out.print(row.get(i).charValue());
			}
			System.out.println();
		}
	}
	
	public static int countSafe(List<List<Tile>> floor) {
		int count = 0;
		for (List<Tile> row : floor) {
			for (int i = 1; i <= row.size() - 2; i++) {
				if (row.get(i) == Tile.SAFE) count++;
			}
		}
		
		return count;
	}
	
	private static class Test {
		private static final String testInput = ".^^.^.^^^^";
		
		public static void main(String[] args) {
			List<List<Tile>> floor = generateFloor(testInput, 10);
			printFloor(floor);
			System.out.println(countSafe(floor));
		}
	}
}

class RunDay18_Part1 {
	public static void main(String[] args) {
		System.out.println(TrapsInARoom.countSafe(TrapsInARoom.generateFloor(TrapsInARoom.input, 40)));
	}
}

class RunDay18_Part2 {
	public static void main(String[] args) {
		System.out.println(TrapsInARoom.countSafe(TrapsInARoom.generateFloor(TrapsInARoom.input, 400_000)));
	}
}