import com.andre.Input;

import java.math.BigInteger;
import java.util.*;

/**
 * Created by Andre on 1/5/2017.
 */
public class RTG {
	private static final class Item {
		enum Type {
			MICROCHIP, GENERATOR
		}
		
		private final String element;
		private final Type type;
		private final String toString;
		
		public Item(String element, Type type) {
			this.element = element.trim().toLowerCase();
			this.type = type;
			toString = String.format("%S%S", element.charAt(0), type.toString().charAt(0));
		}
		
		boolean isMicrochip() {
			return type == Type.MICROCHIP;
		}
		
		boolean isGenerator() {
			return type == Type.GENERATOR;
		}
		
		@Override
		public String toString() {
			return toString;
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			
			Item item = (Item) o;
			
			if (!element.equals(item.element)) return false;
			return type == item.type;
		}
		
		@Override
		public int hashCode() {
			int result = element.hashCode();
			result = 31 * result + type.hashCode();
			return result;
		}
		
		static boolean isPair(Item i1, Item i2) {
			return (i1.type != i2.type) && !i1.element.equals(i2.element);
		}
	}
	
	private static class Node {
		private final Node parent;
		private final int elevatorFloor,
				steps;
		private final List<Set<Item>> layout;
		
		private final boolean isZerothNode;
		
		private Node(Node parent, int elevatorFloor, int steps, List<Set<Item>> layout) {
			this(parent, elevatorFloor, steps, layout, false);
		}
		
		private Node(Node parent, int elevatorFloor, int steps, List<Set<Item>> layout, boolean isZerothNode) {
			this.parent = parent;
			this.elevatorFloor = elevatorFloor;
			this.steps = steps;
			this.layout = layout;
			this.isZerothNode = isZerothNode;
		}
		
		LinkedList<Node> getNextNodes() {
			LinkedList<Node> nextNodes = new LinkedList<>();
			
			boolean emptySoFar = true;
			for (int destinationFloor = 0; destinationFloor < 4; destinationFloor++) {
				if (emptySoFar && layout.get(destinationFloor).size() == 0) continue;
				else
					emptySoFar = false;
				
				if (destinationFloor == elevatorFloor) continue;
				
				for (Item item1 : layout.get(elevatorFloor)) {
					getOutput(elevatorFloor, destinationFloor, layout, item1).ifPresent(nextNodes::add);
					
					for (Item item2 : layout.get(elevatorFloor)) {
						if (item1.equals(item2)) continue;
						getOutput(elevatorFloor, destinationFloor, layout, item1, item2).ifPresent(nextNodes::add);
					}
				}
			}
			
			return nextNodes;
		}
		
		int getSteps() {
			return steps;
		}
		
		boolean isDone() {
			return layout.get(0).size() == 0 &&
					layout.get(1).size() == 0 &&
					layout.get(2).size() == 0;
		}
		
		BigInteger heuristic() {
			BigInteger heuristic = BigInteger.ONE;
			
			Map<Character, String> characterToElementMap = new HashMap<>();
			
			for (int floor = 0; floor < 4; floor++) {
				int microchipCount = 0,
						generatorCount = 0;
				for (Item item : layout.get(floor)) {
					if (item.isGenerator())
						generatorCount++;
					else if (item.isMicrochip())
						microchipCount++;
				}
				BigInteger floorValue = BigInteger.valueOf(microchipCount).multiply(BigInteger.valueOf(31)).add(BigInteger.valueOf(generatorCount).multiply(BigInteger.valueOf(41)));
				
				heuristic = heuristic.multiply(BigInteger.valueOf(47)).add(floorValue);
			}
			
			heuristic = heuristic.multiply(BigInteger.valueOf(59)).add(BigInteger.valueOf(elevatorFloor));
			
			return heuristic;
		}
		
		private Optional<Node> getOutput(int elevatorFloor, int destinationFloor, List<Set<Item>> layout, Item... items) {
			layout = cloneListOfSet(layout);
			Set<Item> elevator = new HashSet<>();
			int steps = this.steps;
			
			elevator.addAll(Arrays.asList(items));
			layout.get(elevatorFloor).removeAll(Arrays.asList(items));
			
			for (; elevatorFloor != destinationFloor; elevatorFloor += destinationFloor > elevatorFloor ? 1 : -1) {
				steps++;
				if (checkForBlownChip(elevatorFloor, layout, elevator) == true) {
					return Optional.empty();
				}
			}
			
			layout.get(elevatorFloor).addAll(elevator);
			
			return Optional.of(new Node(isZerothNode ? null : this, elevatorFloor, steps, layout));
		}
		
		private static boolean checkForBlownChip(int floorNumber, List<Set<Item>> layout, Set<Item> elevator) {
			Set<Item> floor = new HashSet<>(layout.get(floorNumber));
			floor.addAll(elevator);
			
			items:
			for (Item item1 : floor) {
				if (item1.isMicrochip()) {
					for (Item item2 : floor) {
						if (!item1.equals(item2) && Item.isPair(item1, item2)) {
							continue items;
						}
					}
					
					for (Item item2 : floor) {
						if (item2.isGenerator() && !Item.isPair(item1, item2)) {
							return true;
						}
					}
				}
			}
			
			return false;
		}
		
		static LinkedList<Node> firstNodes(List<Set<Item>> initialLayout) {
			Node zerothNode = new Node(null, 0, 0, initialLayout, true);
			return zerothNode.getNextNodes();
		}
		
		private static class TestHeuristic {
			public static void main(String[] args) {
				List<Set<Item>> layout1 = new ArrayList<>(4);
				while (layout1.size() < 4) layout1.add(new HashSet<>());
				layout1.get(3).add(new Item("lithium", Item.Type.GENERATOR));
				layout1.get(3).add(new Item("hydrogen", Item.Type.MICROCHIP));
				
				List<Set<Item>> layout2 = new ArrayList<>(4);
				while (layout2.size() < 4) layout2.add(new HashSet<>());
				layout2.get(3).add(new Item("hydrogen", Item.Type.GENERATOR));
				layout2.get(3).add(new Item("lithium", Item.Type.MICROCHIP));
				
				Node node1 = new Node(null, 0, 0, layout1);
				Node node2 = new Node(null, 0, 0, layout2);
				System.out.println(node1.heuristic());
				System.out.println(node2.heuristic());
			}
			
			
		}
	}
	
	public static final List<String> input = Input.readAllLines("Day 11/input.txt"),
			inputPart2 = Input.readAllLines("Day 11/input part 2.txt");
	
	private final List<Set<Item>> initialLayout;
	
	private int minimumSteps = -1;
	
	public int getMinimumSteps() {
		return minimumSteps;
	}
	
	public RTG(List<String> input) {
		initialLayout = new ArrayList<>(4);
		for (String s : input) {
			Set<Item> floor = new HashSet<>();
			
			if (!s.contains("nothing")) {
				s = s.replaceAll("The \\w+ floor contains |a |\\.|-compatible|an ", "").replaceAll(",", " and").replaceAll("and and", "and");
				String[] item_strings = s.trim().split(" and ");
				for (String item_string : item_strings) {
					String[] tokens = item_string.trim().split(" +");
					if (tokens[1].equals("microchip")) {
						floor.add(new Item(tokens[0], Item.Type.MICROCHIP));
					} else
						floor.add(new Item(tokens[0], Item.Type.GENERATOR));
				}
			}
			
			initialLayout.add(Collections.unmodifiableSet(floor));
		}
	}
	
	public void run() {
		Set<BigInteger> heuristics = new HashSet<>();
		LinkedList<Node> nodes = Node.firstNodes(initialLayout);
		
		treeDepth:
		for (int treeDepth = 0; nodes.size() != 0; treeDepth++) {
			System.out.printf("Depth %d: %d nodes%nTraversedNodes: %d%n%n", treeDepth, nodes.size(), heuristics.size());
			LinkedList<Node> nextNodes = new LinkedList<>();
			for (Node node : nodes) {
				if (node.isDone()) {
					minimumSteps = node.getSteps();
					break treeDepth;
				} else
					nextNodes.addAll(node.getNextNodes());
			}
			removeDuplicate(nextNodes, heuristics);
			nodes = nextNodes;
		}
	}
	
	private static void removeDuplicate(LinkedList<Node> nodes, Set<BigInteger> heuristics) {
		nodes.removeIf(node -> !heuristics.add(node.heuristic()));
	}
	
	private static <E> List<Set<E>> cloneListOfSet(List<Set<E>> input) {
		List<Set<E>> output = new ArrayList<>(input.size());
		for (Set<E> es : input) {
			output.add(new HashSet<E>(es));
		}
		return output;
	}
	
	private static class Test {
		private static final List<String> testInput = Input.readAllLines("Day 11/test input.txt");
		
		public static void main(String[] args) {
			RTG rtg = new RTG(testInput);
			rtg.run();
			System.out.println(rtg.getMinimumSteps());
		}
	}
}

class RunDay11_Part1 {
	public static void main(String[] args) {
		RTG rtg = new RTG(RTG.input);
		rtg.run();
		System.out.println(rtg.getMinimumSteps());
	}
}

class RunDay11_Part2 {
	public static void main(String[] args) {
		RTG rtg = new RTG(RTG.inputPart2);
		rtg.run();
		System.out.println(rtg.getMinimumSteps());
	}
}