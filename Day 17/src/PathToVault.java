import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by Andre on 1/5/2017.
 */
public class PathToVault {
	private static class Node {
		private final String seed;
		private final Node parent;
		private final char direction;
		private final int x, y;
		
		private Node(String seed, Node parent, char direction, int x, int y) {
			this.seed = seed;
			this.parent = parent;
			this.direction = direction;
			this.x = x;
			this.y = y;
		}
		
		boolean isDone() {
			return x == 4 && y == 4;
		}
		
		Set<Node> getNextNodes() {
			Set<Node> nextNodes = new HashSet<>();
			
			String hash = MD5Hash(seed + toString());
			
			// Up
			if (y - 1 >= 1 && matchOpen(hash.charAt(0))) {
				nextNodes.add(new Node(seed, this, 'U', x, y - 1));
			}
			
			// Down
			if (y + 1 <= 4 && matchOpen(hash.charAt(1))) {
				nextNodes.add(new Node(seed, this, 'D', x, y + 1));
			}
			
			// Left
			if (x - 1 >= 1 && matchOpen(hash.charAt(2))) {
				nextNodes.add(new Node(seed, this, 'L', x - 1, y));
			}
			
			// Right
			if (x + 1 <= 4 && matchOpen(hash.charAt(3))) {
				nextNodes.add(new Node(seed, this, 'R', x + 1, y));
			}
			
			return nextNodes;
		}
		
		@Override
		public String toString() {
			return parent == null ? "" : parent.toString() + direction;
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			
			Node node = (Node) o;
			
			if (x != node.x) return false;
			if (y != node.y) return false;
			return toString().equals(node.toString());
		}
		
		@Override
		public int hashCode() {
			int result = toString().hashCode();
			result = 31 * result + x;
			result = 31 * result + y;
			return result;
		}
		
		static Set<Node> getFirstNodes(String seed) {
			Node zerothNode = new Node(seed, null, 'X', 1, 1);
			return zerothNode.getNextNodes();
		}
		
		private static boolean matchOpen(char c) {
			return String.valueOf(c).matches("[B-F]");
		}
		
		//<editor-fold desc="MD5">
		private static MessageDigest MD5 = null;
		
		private static String MD5Hash(String input) {
			if (MD5 == null) try {
				MD5 = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
			
			return DatatypeConverter.printHexBinary(MD5.digest(input.getBytes()));
		}
		//</editor-fold>
	}
	
	public static final String input = "pgflpeqp";
	
	public static String shortestPath(String seed) {
		Set<Node> nodes = Node.getFirstNodes(seed);
		
		for (int steps = 1; steps < 1000 && nodes.size() != 0; steps++) {
			Set<Node> nextNodes = new HashSet<>();
			
			for (Node node : nodes) {
				if (node.isDone()) {
					return node.toString();
				} else {
					nextNodes.addAll(node.getNextNodes());
				}
			}
			
			nodes = nextNodes;
		}
		
		return "-1";
	}
	
	public static String longestPath(String seed) {
		Set<Node> nodes = Node.getFirstNodes(seed);
		
		Map<Node, Integer> paths = new HashMap<>();
		
		for (int steps = 1; nodes.size() != 0; steps++) {
			Set<Node> nextNodes = new HashSet<>();
			
			for (Node node : nodes) {
				if (node.isDone())
					paths.put(node, steps);
				else
					nextNodes.addAll(node.getNextNodes());
			}
			
			nodes = nextNodes;
		}
		
		Optional<Map.Entry<Node, Integer>> entryOptional = paths.entrySet().parallelStream().max(Comparator.comparingInt(Map.Entry::getValue));
		return entryOptional.isPresent() ? entryOptional.get().getKey().toString() : "-1";
	}
	
	private static class Test {
		public static void main(String[] args) {
			assert shortestPath("hijkl").equals("-1");
			assert shortestPath("ihgpwlah").equals("DDRRRD");
			assert shortestPath("kglvqrro").equals("DDUDRLRRUDRD");
			assert shortestPath("ulqzkmiv").equals("DRURDRUDDLLDLUURRDULRLDUUDDDRR");
			System.out.println("All tests for part 1 completed successfully");
			
			assert longestPath("ihgpwlah").length() == 370;
			assert longestPath("kglvqrro").length() == 492;
			assert longestPath("ulqzkmiv").length() == 830;
			System.out.println("All tests for part 2 completed successfully");
		}
	}
}

class RunDay17_Part1 {
	public static void main(String[] args) {
		String result = PathToVault.shortestPath(PathToVault.input);
		System.out.println(result);
		System.out.println(result.length());
	}
}

class RunDay17_Part2 {
	public static void main(String[] args) {
		System.out.println(PathToVault.longestPath(PathToVault.input).length());
	}
}