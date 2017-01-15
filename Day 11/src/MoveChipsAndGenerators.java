import andre.adventofcode.input.Input;

import java.math.BigInteger;
import java.util.*;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

/**
 * Created by Andre on 1/15/2017.
 */
public class MoveChipsAndGenerators {
	private static final class Item {
		enum Type {
			GENERATOR, MICROCHIP
		}
		
		private final String element;
		private final Type type;
		
		private final String toString;
		private final int hashCode;
		
		Item(String element, Type type) {
			this.element = element.trim().toLowerCase();
			this.type = type;
			
			toString = String.format("%S%S", this.element.charAt(0), this.type.toString().charAt(0));
			
			int result = element.hashCode();
			result = 31 * result + type.hashCode();
			hashCode = result;
		}
		
		boolean isGenerator() {
			return type == Type.GENERATOR;
		}
		
		boolean isMicrochip() {
			return type == Type.MICROCHIP;
		}
		
		// return if item1 and item2 are corresponding microchips and elements
		static boolean isPair(Item item1, Item item2) {
			return item1.type != item2.type && item1.element.equals(item2.element);
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
		
		@Override
		public String toString() {
			return toString;
		}
	}
	
	private static final class Node {
		private static final BigInteger PRIME = BigInteger.valueOf(31);
		
		private final Node parent;
		private final List<Set<Item>> layout;
		private final int elevatorFloor;
		private final int steps;
		
		int getSteps() {
			return steps;
		}
		
		private Node(Node parent, List<Set<Item>> layout, int elevatorFloor, int steps) {
			this.parent = parent;
			this.layout = layout;
			this.elevatorFloor = elevatorFloor;
			this.steps = steps;
		}
		
		boolean isDone() {
			return layout.get(0).isEmpty() &&
					layout.get(1).isEmpty() &&
					layout.get(2).isEmpty();
		}
		
		LinkedList<Node> nextNodes() {
			LinkedList<Node> output = new LinkedList<>();
			
			for (int destinationFloor : new int[]{elevatorFloor - 1, elevatorFloor + 1}) {
				if (destinationFloor < 0 || destinationFloor > 3) continue;
				
				for (Item item1 : layout.get(elevatorFloor)) {
					if (!hasBlownChip(layout, destinationFloor, item1)) {
						List<Set<Item>> nextLayout = cloneListOfSet(layout);
						nextLayout.get(elevatorFloor).remove(item1);
						nextLayout.get(destinationFloor).add(item1);
						output.add(new Node(this, nextLayout, destinationFloor, steps + 1));
					}
					
					for (Item item2 : layout.get(elevatorFloor)) {
						if (item1 != item2 && !hasBlownChip(layout, destinationFloor, item1, item2)) {
							List<Set<Item>> nextLayout = cloneListOfSet(layout);
							nextLayout.get(elevatorFloor).removeAll(Arrays.asList(item1, item2));
							nextLayout.get(destinationFloor).addAll(Arrays.asList(item1, item2));
							output.add(new Node(this, nextLayout, destinationFloor, steps + 1));
						}
					}
				}
			}
			
			return output;
		}
		
		BigInteger heuristic() {
			BigInteger heuristic = ZERO;
			
			for (Set<Item> floor : layout) {
				BigInteger floorValue,
						generatorCount = ZERO,
						microchipCount = ZERO;
				for (Item item : floor) {
					if (item.isGenerator())
						generatorCount = generatorCount.add(ONE);
					else if (item.isMicrochip())
						microchipCount = microchipCount.add(ONE);
				}
				
				floorValue = generatorCount.multiply(PRIME).add(microchipCount);
				heuristic = heuristic.multiply(PRIME).add(floorValue);
			}
			
			return heuristic.multiply(PRIME).add(BigInteger.valueOf(elevatorFloor));
		}
		
		private static boolean hasBlownChip(List<Set<Item>> layout, int elevatorFloor, Item... elevatorItems) {
			Set<Item> floor = new HashSet<>(layout.get(elevatorFloor));
			floor.addAll(Arrays.asList(elevatorItems));
			
			item1:
			for (Item item1 : floor) {
				if (item1.isMicrochip()) {
					for (Item item2 : floor) {
						if (Item.isPair(item1, item2)) {
							continue item1;
						}
					}
					
					for (Item item2 : floor) {
						if (item2.isGenerator()) {
							return true;
						}
					}
				}
			}
			
			return false;
		}
		
		static LinkedList<Node> firstNode(List<Set<Item>> initialLayouts) {
			return new Node(null, initialLayouts, 0, 0).nextNodes();
		}
	}
	
	public static final List<String> testInput = Input.readAllLines("Day 11/test input.txt"),
			input = Input.readAllLines("Day 11/input.txt"),
			input2 = Input.readAllLines("Day 11/input part 2.txt");
	
	private List<Set<Item>> initialLayout;
	private int minimumSteps = -1;
	
	private int getMinimumSteps() {
		return minimumSteps;
	}
	
	private MoveChipsAndGenerators(List<String> input) {
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
	
	private void run() {
		LinkedList<Node> nodes = Node.firstNode(initialLayout);
		Set<BigInteger> heuristics = new HashSet<>();
		trimNodes(nodes, heuristics);
		
		for (int depth = 1; nodes.size() != 0; depth++) {
			System.out.printf("Depth %d: %d nodes%nTraversed nodes: %d%n%n", depth, nodes.size(), heuristics.size());
			
			LinkedList<Node> nextNodes = new LinkedList<>();
			for (Node node : nodes) {
				if (node.isDone()) {
					minimumSteps = node.getSteps();
					return;
				} else
					nextNodes.addAll(node.nextNodes());
			}
			trimNodes(nextNodes, heuristics);
			nodes = nextNodes;
		}
	}
	
	private static void trimNodes(LinkedList<Node> nodes, Set<BigInteger> heuristics) {
		// Remove if cannot add (already exists)
		nodes.removeIf(node -> !heuristics.add(node.heuristic()));
	}
	
	private static <E> List<Set<E>> cloneListOfSet(List<Set<E>> original) {
		List<Set<E>> clone = new ArrayList<>(original.size());
		
		for (Set<E> es : original) {
			clone.add(new HashSet<E>(es));
		}
		
		return clone;
	}
	
	public static int calculateMinimumSteps(List<String> input) {
		MoveChipsAndGenerators instance = new MoveChipsAndGenerators(input);
		instance.run();
		return instance.getMinimumSteps();
	}
}

class RunDay11TestCase {
	public static void main(String[] args) {
		System.out.println(MoveChipsAndGenerators.calculateMinimumSteps(MoveChipsAndGenerators.testInput));
	}
}

class RunDay11Part1 {
	public static void main(String[] args) {
		System.out.println(MoveChipsAndGenerators.calculateMinimumSteps(MoveChipsAndGenerators.input));
	}
}

class RunDay11Part2 {
	public static void main(String[] args) {
		System.out.println(MoveChipsAndGenerators.calculateMinimumSteps(MoveChipsAndGenerators.input2));
	}
}

class RunJeffDay11Part1 {
	public static void main(String[] args) {
		System.out.println(MoveChipsAndGenerators.calculateMinimumSteps(Input.readAllLines("Day 11/jeffery input.txt")));
	}
}