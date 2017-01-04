import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andre on 1/4/2017.
 */
public class OfficeMaze {
	public static final class Input {
		public static final int destX = 31, destY = 39, seed = 1358;
	}
	
	private static final class Node {
		private final int x, y;
		private final int seed;
		private final Node parent;
		
		Node(int x, int y, int seed, Node parent) {
			this.seed = seed;
			this.x = x;
			this.y = y;
			this.parent = parent;
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			
			Node node = (Node) o;
			
			if (x != node.x) return false;
			return y == node.y;
		}
		
		@Override
		public int hashCode() {
			int result = x;
			result = 31 * result + y;
			return result;
		}
		
		Set<Node> nextNodes() {
			Set<Node> output = new HashSet<>();
			
			if (x - 1 >= 0 && isOpen(x - 1, y)) output.add(new Node(x - 1, y, seed, this));
			if (isOpen(x + 1, y)) output.add(new Node(x + 1, y, seed, this));
			if (y - 1 >= 0 && isOpen(x, y - 1)) output.add(new Node(x, y - 1, seed, this));
			if (isOpen(x, y + 1)) output.add(new Node(x, y + 1, seed, this));
			
			return output;
			
			
		}
		
		Point getPoint() {
			return new Point(x, y);
		}
		
		private boolean isOpen(int x, int y) {
			int total = (x * x) + (3 * x) + (2 * x * y) + y + (y * y);
			total += seed;
			String binaryRepresentation = Integer.toBinaryString(total);
			int oneCount = 0;
			for (int i = 0; i < binaryRepresentation.length(); i++) {
				if (binaryRepresentation.charAt(i) == '1') {
					oneCount++;
				}
			}
			return oneCount % 2 == 0;
		}
	}
	
	public static int stepToReach(int destX, int destY, int seed) {
		final Node destNode = new Node(destX, destY, -1, null);
		
		Set<Node> nodes = new HashSet<>();
		Set<Node> traversedNodes = new HashSet<>();
		nodes.add(new Node(1, 1, seed, null));
		traversedNodes.addAll(nodes);
		
		step:
		for (int step = 0; ; step++) {
			Set<Node> nextNodes = new HashSet<>();
			for (Node node : nodes) {
				if (node.equals(destNode)) {
					return step;
				} else {
					nextNodes.addAll(node.nextNodes());
				}
			}
			nextNodes.removeAll(traversedNodes);
			traversedNodes.addAll(nextNodes);
			nodes = nextNodes;
		}
	}
	
	public static int distinctLocations(int steps, int seed) {
		Set<Node> nodes = new HashSet<>();
		Set<Node> traversedNodes = new HashSet<>();
		nodes.add(new Node(1, 1, seed, null));
		traversedNodes.addAll(nodes);
		
		for (int step = 0; step < steps; step++) {
			Set<Node> nextNodes = new HashSet<>();
			for (Node node : nodes) {
				nextNodes.addAll(node.nextNodes());
			}
			nextNodes.removeAll(traversedNodes);
			traversedNodes.addAll(nextNodes);
			nodes = nextNodes;
		}
		
		return traversedNodes.size();
	}
	
	private static class Test {
		public static void main(String[] args) {
			System.out.println(stepToReach(7, 4, 10));
		}
	}
}

class RunDay13_Part1 {
	public static void main(String[] args) {
		System.out.println(OfficeMaze.stepToReach(OfficeMaze.Input.destX, OfficeMaze.Input.destY, OfficeMaze.Input.seed));
	}
}

class RunDay13_Part2 {
	public static void main(String[] args) {
		System.out.println(OfficeMaze.distinctLocations(50, OfficeMaze.Input.seed));
	}
}