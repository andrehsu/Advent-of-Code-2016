import andre.adventofcode.input.Input;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Andre on 12/5/2016.
 */
public class Triangle {
	public static final List<String> input = Input.readAllLines("Day 3/input.txt");
	
	public static int countNumOfTriangle(List<String> input) {
		Set<List<Integer>> triangles = input.stream()
				.map(s -> Arrays.stream(s.trim().split("\\s+")).map(Integer::parseInt).collect(Collectors.toList()))
				.collect(Collectors.toSet());
		int count = 0;
		for (List<Integer> triangle : triangles) {
			if (isTriangle(triangle))
				count++;
		}
		return count;
	}
	
	public static int countNumOfTriangleVertical(List<String> input) {
		Set<List<Integer>> triangles = new HashSet<>(input.size());
		Function<String, List<Integer>> strToInts = s -> Arrays.stream(s.trim().split("\\s+")).map(Integer::parseInt).collect(Collectors.toList());
		for (int i = 0; i < input.size(); i += 3) {
			List<Integer> l1 = strToInts.apply(input.get(i)),
					l2 = strToInts.apply(input.get(i + 1)),
					l3 = strToInts.apply(input.get(i + 2));
			for (int j = 0; j < 3; j++) {
				triangles.add(Arrays.asList(l1.get(j), l2.get(j), l3.get(j)));
			}
		}
		int count = 0;
		for (List<Integer> triangle : triangles) {
			if (isTriangle(triangle))
				count++;
		}
		return count;
	}
	
	private static boolean isTriangle(List<Integer> sides) {
		return sides.get(0) + sides.get(1) > sides.get(2) &&
				sides.get(0) + sides.get(2) > sides.get(1) &&
				sides.get(1) + sides.get(2) > sides.get(0);
	}
}

class RunDay3Part1 {
	public static void main(String... args) {
		System.out.println(Triangle.countNumOfTriangle(Triangle.input));
	}
}

class RunDay3Part2 {
	public static void main(String... args) {
		System.out.println(Triangle.countNumOfTriangleVertical(Triangle.input));
	}
}