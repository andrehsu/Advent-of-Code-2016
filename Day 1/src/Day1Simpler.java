import andre.adventofcode.input.Input;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;

/**
 * Created by Andre on 1/28/2017.
 */
public class Day1Simpler {
	public static final String input = Input.readFirstLine("Day 1/input.txt");
	
	public static int endPoint(String input) {
		String[] tokens = input.split(", ");
		
		int direction = 0;
		int x = 0,
				y = 0;
		
		for (String token : tokens) {
			direction = getDirection(direction, token);
			
			int distance = Integer.parseInt(token.substring(1));
			switch (direction) {
				case 0:
					x += distance;
					break;
				case 1:
					y += distance;
					break;
				case 2:
					x -= distance;
					break;
				case 3:
					y -= distance;
					break;
			}
		}
		
		return Math.abs(x) + Math.abs(y);
	}
	
	public static int firstRepeat(String input) {
		String[] tokens = input.split(", ");
		
		int direction = 0;
		int x = 0,
				y = 0;
		Set<List<Integer>> visited = new HashSet<>();
		
		for (String token : tokens) {
			direction = getDirection(direction, token);
			
			int distance = Integer.parseInt(token.substring(1));
			for (int i = 0; i < distance; i++) {
				if (!visited.add(asList(x, y))) {
					return Math.abs(x) + Math.abs(y);
				}
				switch (direction) {
					case 0:
						x += 1;
						break;
					case 1:
						y += 1;
						break;
					case 2:
						x -= 1;
						break;
					case 3:
						y -= 1;
						break;
				}
			}
		}
		return -1;
	}
	
	private static int getDirection(int direction, String token) {
		if (token.charAt(0) == 'R') {
			direction = (direction + 1) % 4;
		} else {
			direction--;
			if (direction < 0)
				direction = 3;
		}
		return direction;
	}
}

class RunDay1Part1 {
	public static void main(String[] args) {
		System.out.println(Day1Simpler.endPoint(Day1Simpler.input));
	}
}

class RunDay1Part2 {
	public static void main(String[] args) {
		System.out.println(Day1Simpler.firstRepeat(Day1Simpler.input));
	}
}