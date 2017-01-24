import andre.adventofcode.input.Input;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Andre on 1/24/2017.
 * coded and ran only once to get the answer, no debugging done
 * (except adding instance.run() to the shortestDistance() method)
 */
public class AirDuctMaze {
	private static final class Node {
		private final char[][] map;
		private final char destinationPoint;
		
		private final Point currentPoint;
		private final int steps;
		
		Point getCurrentPoint() {
			return currentPoint;
		}
		
		int getSteps() {
			return steps;
		}
		
		private Node(char[][] map, char destinationPoint, Point currentPoint, int steps) {
			this.map = map;
			this.destinationPoint = destinationPoint;
			this.currentPoint = currentPoint;
			this.steps = steps;
		}
		
		boolean isSolution() {
			return map[currentPoint.x][currentPoint.y] == destinationPoint;
		}
		
		LinkedList<Node> nextNodes() {
			LinkedList<Node> next_nodes = new LinkedList<>();
			
			int x = currentPoint.x,
					y = currentPoint.y;
			for (Point next_point :
					setOf(new Point(x + 1, y),
							new Point(x - 1, y),
							new Point(x, y + 1),
							new Point(x, y - 1))) {
				if (map[next_point.x][next_point.y] != '#') {
					next_nodes.add(new Node(map, destinationPoint, next_point, steps + 1));
				}
			}
			
			return next_nodes;
		}
		
		static LinkedList<Node> initialNodes(char[][] map, char destinationPoint, Point startingPoint) {
			return new Node(map, destinationPoint, startingPoint, 0).nextNodes();
		}
		
		private static <T> Set<T> setOf(T... items) {
			return new HashSet<>(Arrays.asList(items));
		}
	}
	
	public static final List<String> input = Input.readAllLines("Day 24/input.txt"),
			testInput = Input.readAllLines("Day 24/test input.txt");
	
	private final Table<Character, Character, Integer> adjacencyTable = HashBasedTable.create();
	private final Map<Character, Point> points;
	private final char[][] map;
	
	private Table<Character, Character, Integer> getAdjacencyTable() {
		return adjacencyTable;
	}
	
	private Map<Character, Point> getPoints() {
		return points;
	}
	
	//<editor-fold desc="Constructors and factories">
	private AirDuctMaze(char[][] map, Map<Character, Point> points) {
		this.map = map;
		this.points = points;
	}
	
	private static AirDuctMaze create(List<String> input) {
		char[][] map = new char[input.size()][];
		Map<Character, Point> points = new HashMap<>();
		for (int row = 0; row < input.size(); row++) {
			String s = input.get(row);
			map[row] = new char[s.length()];
			for (int column = 0; column < s.length(); column++) {
				char c = s.charAt(column);
				map[row][column] = c;
				if (Character.isDigit(c))
					points.put(c, new Point(row, column));
			}
		}
		return new AirDuctMaze(map, points);
	}
	//</editor-fold>
	
	private void run() {
		for (Character from : points.keySet()) {
			for (Character to : points.keySet()) {
				Integer opposite = adjacencyTable.get(to, from);
				if (opposite != null)
					adjacencyTable.put(from, to, opposite);
				else {
					LinkedList<Node> nodes = Node.initialNodes(map, to, points.get(from));
					Set<Point> traversedPoints = new HashSet<>();
					
					depth:
					for (int depth = 0; !nodes.isEmpty(); depth++) {
						LinkedList<Node> next_nodes = new LinkedList<>();
						for (Node node : nodes) {
							//noinspection StatementWithEmptyBody
							if (!traversedPoints.add(node.getCurrentPoint())) {
								// pass
							} else if (node.isSolution()) {
								adjacencyTable.put(from, to, node.getSteps());
								break depth;
							} else {
								next_nodes.addAll(node.nextNodes());
							}
						}
						nodes = next_nodes;
					}
				}
			}
		}
	}
	
	public static int shortestDistance(List<String> input) {
		return shortestDistance(input, false);
	}
	
	public static int shortestDistance(List<String> input, boolean returnToStart) {
		AirDuctMaze instance = create(input);
		instance.run();
		Table<Character, Character, Integer> adjacencyTable = instance.getAdjacencyTable();
		Set<Character> points = new HashSet<>(instance.getPoints().keySet());
		char startingPoints = '0';
		points.remove(startingPoints);
		
		Set<Integer> distances = new HashSet<>();
		shortestDistance_node(distances,
				adjacencyTable,
				startingPoints,
				points,
				0,
				returnToStart);
		return distances.stream().mapToInt(Integer::intValue).min().orElse(-1);
	}
	
	private static void shortestDistance_node(Set<Integer> distances,
	                                          Table<Character, Character, Integer> adjacencyTable,
	                                          char currentPoint,
	                                          Set<Character> remainingPoints,
	                                          int distance,
	                                          boolean returnToZero) {
		if (remainingPoints.isEmpty()) {
			if (returnToZero)
				distance += adjacencyTable.get(currentPoint, '0');
			distances.add(distance);
		} else {
			for (Character next_point : remainingPoints) {
				Set<Character> next_remainingPoints = new HashSet<>(remainingPoints);
				next_remainingPoints.remove(next_point);
				
				shortestDistance_node(distances,
						adjacencyTable,
						next_point,
						next_remainingPoints,
						distance + adjacencyTable.get(currentPoint, next_point),
						returnToZero);
			}
		}
	}
}

class RunDay24Part1Test {
	public static void main(String... args) {
		System.out.println(AirDuctMaze.shortestDistance(AirDuctMaze.testInput));
	}
}

class RunDay24Part1 {
	public static void main(String... args) {
		System.out.println(AirDuctMaze.shortestDistance(AirDuctMaze.input));
	}
}

class RunDay24Part2 {
	public static void main(String... args) {
		System.out.println(AirDuctMaze.shortestDistance(AirDuctMaze.input, true));
	}
}