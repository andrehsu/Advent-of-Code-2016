import andre.adventofcode.input.Input;

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
		private final int hashCode;
		
		public Item(String element, Type type) {
			this.element = element.trim().toLowerCase();
			this.type = type;
			toString = String.format("%S%S", element.charAt(0), type.toString().charAt(0));
			int result = element.hashCode();
			result = 31 * result + type.hashCode();
			hashCode = result;
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
			return hashCode;
		}
	}
	
	private static class Node {
		// prime used in heuristic calculation
		private static final BigInteger PRIME = BigInteger.valueOf(31);
		
		// parent for debugging purposes
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
				// Optimization: Don't move items down to empty floors below
				if (emptySoFar && layout.get(destinationFloor).size() == 0) continue;
				else
					emptySoFar = false;
				
				if (destinationFloor == elevatorFloor) continue;
				
				for (Item item1 : layout.get(elevatorFloor)) {
					getNode(elevatorFloor, destinationFloor, layout, item1).ifPresent(nextNodes::add);
					
					for (Item item2 : layout.get(elevatorFloor)) {
						if (item1.equals(item2)) continue;
						getNode(elevatorFloor, destinationFloor, layout, item1, item2).ifPresent(nextNodes::add);
					}
				}
			}
			
			return nextNodes;
		}
		
		int getSteps() {
			return steps;
		}
		
		boolean isDone() {
			// return if all items are on top floor
			return layout.get(0).size() == 0 &&
					layout.get(1).size() == 0 &&
					layout.get(2).size() == 0;
		}
		
		BigInteger heuristic() {
			// Generate heuristic for nodes
			BigInteger heuristic = BigInteger.ZERO;
			for (int floor = 0; floor < 4; floor++) {
				int microchipCount = 0,
						generatorCount = 0;
				for (Item item : layout.get(floor)) {
					if (item.isGenerator())
						generatorCount++;
					else if (item.isMicrochip())
						microchipCount++;
				}
				BigInteger floorValue = BigInteger.valueOf(microchipCount).multiply(PRIME).add(BigInteger.valueOf(generatorCount));
				
				heuristic = heuristic.multiply(PRIME).add(floorValue);
			}
			
			heuristic = heuristic.multiply(PRIME).add(BigInteger.valueOf(elevatorFloor));
			
			return heuristic;
		}
		
		private Optional<Node> getNode(int elevatorFloor, int destinationFloor, List<Set<Item>> layout, Item... items) {
			layout = cloneListOfSet(layout);
			Set<Item> elevator = new HashSet<>();
			int steps = this.steps;
			
			elevator.addAll(Arrays.asList(items));
			layout.get(elevatorFloor).removeAll(Arrays.asList(items));
			
			for (; elevatorFloor != destinationFloor; elevatorFloor += destinationFloor > elevatorFloor ? 1 : -1) {
				steps++;
				// If any chip was blown during movement
				if (hasBlownChip(elevatorFloor, layout, elevator)) {
					return Optional.empty();
				}
			}
			
			layout.get(elevatorFloor).addAll(elevator);
			
			return Optional.of(new Node(isZerothNode ? null : this, elevatorFloor, steps, layout));
		}
		
		private static boolean hasBlownChip(int floorNumber, List<Set<Item>> layout, Set<Item> elevator) {
			Set<Item> floor = new HashSet<>(layout.get(floorNumber));
			floor.addAll(elevator);
			
			items:
			for (Item item1 : floor) {
				if (item1.isMicrochip()) {
					// Check for generator protecting chip
					for (Item item2 : floor) {
						if (item2.isGenerator()) {
							// for some reason not equals work
							if (!item1.element.equals(item2.element))
								continue items;
						}
					}
					
					// Check if ANY other generator present to blow chip
					for (Item item2 : floor) {
						if (item2.isGenerator()) return true;
					}
				}
			}
			
			return false;
		}
		
		static LinkedList<Node> firstNodes(List<Set<Item>> initialLayout) {
			Node zerothNode = new Node(null, 0, 0, initialLayout, true);
			return zerothNode.getNextNodes();
		}
	}
	
	public static final List<String> input = Input.readAllLines("Day 11 (Works only on my input)/input.txt"),
			inputPart2 = Input.readAllLines("Day 11 (Works only on my input)/input part 2.txt");
	
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
		List<Set<E>> output;
		if (input instanceof LinkedList) {
			output = new LinkedList<>();
		} else
			output = new ArrayList<>(input.size());
		for (Set<E> es : input) {
			output.add(new HashSet<>(es));
		}
		return output;
	}
	
	private static class Test {
		private static final List<String> testInput = Input.readAllLines("Day 11 (Works only on my input)/test input.txt");
		
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
		System.out.println(rtg.getMinimumSteps()); // 37
	}
}

class RunDay11_Part2 {
	public static void main(String[] args) {
		RTG rtg = new RTG(RTG.inputPart2);
		rtg.run();
		System.out.println(rtg.getMinimumSteps()); // 61
	}
}