import andre.adventofcode.input.Input;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class DistanceCalculator {
	public static final String input = Input.readFirstLine("Day 1/input.txt");
	public static final Point startingPoint = new Point(0, 0);
	
	private final String[] sequence;
	private int distanceOfEndPoint, distanceOfRepeatedPoint;
	
	//region Accessors
	public int getDistanceOfEndPoint() {
		return distanceOfEndPoint;
	}
	
	public int getDistanceOfRepeatedPoint() {
		return distanceOfRepeatedPoint;
	}
	//endregion
	
	public DistanceCalculator(String sequenceString) {
		sequence = sequenceString.trim().split(", ");
	}
	
	public void run() {
		Direction direction = Direction.NORTH;
		
		Set<Point> uniquePoints = new HashSet<>();
		
		Point point = new Point(startingPoint);
		//uniquePoints.add(point);
		
		boolean gotRepeatedPoint = false;
		
		for (String navigation : sequence) {
			if (navigation.charAt(0) == 'L')
				direction = Direction.turnLeft(direction);
			else if (navigation.charAt(0) == 'R')
				direction = Direction.turnRight(direction);
			
			int distance = parseNavigationDistance(navigation);
			for (int i = 0; i < distance; i++) {
				switch (direction) {
					case NORTH:
						point.translate(0, 1);
						break;
					case EAST:
						point.translate(1, 0);
						break;
					case SOUTH:
						point.translate(0, -1);
						break;
					case WEST:
						point.translate(-1, 0);
						break;
				}
				
				if (!gotRepeatedPoint && !uniquePoints.add(new Point(point))) {
					distanceOfRepeatedPoint = calculateDistance(startingPoint, point);
					gotRepeatedPoint = true;
				}
				
			}
		}
		
		distanceOfEndPoint = calculateDistance(startingPoint, point);
	}
	
	//region Utility methods
	private static int parseNavigationDistance(String navigation) {
		return Integer.parseInt(navigation.substring(1));
	}
	
	private static int calculateDistance(Point point1, Point point2) {
		return Math.abs(point2.x - point1.x) + Math.abs(point2.y - point1.y);
	}
	//endregion
	
	private enum Direction {
		NORTH, EAST, SOUTH, WEST;
		
		public static Direction turnLeft(Direction direction) {
			switch (direction) {
				case NORTH:
					return WEST;
				case EAST:
					return NORTH;
				case SOUTH:
					return EAST;
				case WEST:
					return SOUTH;
				default:
					throw new RuntimeException();
			}
		}
		
		public static Direction turnRight(Direction direction) {
			switch (direction) {
				case NORTH:
					return EAST;
				case EAST:
					return SOUTH;
				case SOUTH:
					return WEST;
				case WEST:
					return NORTH;
				default:
					throw new RuntimeException();
			}
		}
	}
}

class RunDay1 {
	public static void main(String[] args) {
		DistanceCalculator distanceCalculator = new DistanceCalculator(DistanceCalculator.input);
		distanceCalculator.run();
		System.out.println(distanceCalculator.getDistanceOfEndPoint());
		System.out.println(distanceCalculator.getDistanceOfRepeatedPoint());
	}
}
