import andre.adventofcode.input.Input;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

/**
 * Created by Andre on 1/15/2017.
 */
public class ChipsAndGenerators {
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
			
			toString = String.format("%S%S", this.element.substring(0, 2), this.type.toString().charAt(0));
			
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
			
			if (element != null ? !element.equals(item.element) : item.element != null) return false;
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
		private final String move;
		private final List<Set<Item>> layout;
		private final int elevatorFloor;
		private final int steps;
		
		int getSteps() {
			return steps;
		}
		
		private Node(Node parent, String move, List<Set<Item>> layout, int elevatorFloor, int steps) {
			this.parent = parent;
			this.move = move;
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
						output.add(new Node(this,
								move + "\n" + item1 + " -> " + (destinationFloor + 1),
								nextLayout, destinationFloor, steps + 1));
					}
					
					for (Item item2 : layout.get(elevatorFloor)) {
						if (item1 != item2 && !hasBlownChip(layout, destinationFloor, item1, item2)) {
							List<Set<Item>> nextLayout = cloneListOfSet(layout);
							nextLayout.get(elevatorFloor).removeAll(Arrays.asList(item1, item2));
							nextLayout.get(destinationFloor).addAll(Arrays.asList(item1, item2));
							output.add(new Node(this,
									move + "\n" + item1 + ", " + item2 + " -> " + (destinationFloor + 1),
									nextLayout, destinationFloor, steps + 1));
						}
					}
				}
			}
			
			return output;
		}
		
		BigInteger heuristic() {
			BigInteger heuristic = BigInteger.valueOf(elevatorFloor);
			
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
			
			return heuristic.multiply(PRIME);
		}
		
		void printMove() {
			System.out.println(move);
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
			return new Node(null, "", initialLayouts, 0, 0).nextNodes();
		}
	}
	
	public static final List<String> testInput = Input.readAllLines("Day 11/test input.txt"),
			input = Input.readAllLines("Day 11/input.txt"),
			input2 = Input.readAllLines("Day 11/input part 2.txt"),
			jefferyInput = Input.readAllLines("Day 11/jeffery input.txt");
	
	private final boolean print;
	
	private List<Set<Item>> initialLayout;
	private int minimumSteps = -1;
	
	private int getMinimumSteps() {
		return minimumSteps;
	}
	
	private ChipsAndGenerators(List<String> input, boolean print) {
		this.print = print;
		
		Pattern generatorPattern = Pattern.compile("\\w+(?= generator)"),
				microchipPattern = Pattern.compile("\\w+(?=-compatible microchip)");
		
		initialLayout = new ArrayList<>(4);
		for (String s : input) {
			Set<Item> floor = new HashSet<>();
			
			if (!s.contains("nothing")) {
				// Do microchip
				Matcher generators = generatorPattern.matcher(s);
				while (generators.find()) {
					floor.add(new Item(generators.group(), Item.Type.GENERATOR));
				}
				
				Matcher microchips = microchipPattern.matcher(s);
				while (microchips.find()) {
					floor.add(new Item(microchips.group(), Item.Type.MICROCHIP));
				}
			}
			
			initialLayout.add(Collections.unmodifiableSet(floor));
		}
	}
	
	private void run() {
		LinkedList<Node> nodes = Node.firstNode(initialLayout);
		Set<BigInteger> heuristics = new HashSet<>();
		
		for (int depth = 1; nodes.size() != 0; depth++) {
			if (print)
				System.out.printf("Depth %d: %d nodes%nTraversed nodes: %d%n%n", depth, nodes.size(), heuristics.size());
			
			LinkedList<Node> nextNodes = new LinkedList<>();
			for (Node node : nodes) {
				if (node.isDone()) {
					minimumSteps = node.getSteps();
					if (print) node.printMove();
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
	
	public static int calculateMinimumSteps(List<String> input, boolean print) {
		ChipsAndGenerators instance = new ChipsAndGenerators(input, print);
		instance.run();
		return instance.getMinimumSteps();
	}
}

class RunDay11TestCase {
	public static void main(String[] args) {
		boolean print = args == null || args.length != 1 ? true : Boolean.valueOf(args[0]);
		System.out.println(ChipsAndGenerators.calculateMinimumSteps(ChipsAndGenerators.testInput, print));
	}
}

class RunDay11Part1 {
	public static void main(String[] args) {
		boolean print = args == null || args.length != 1 ? true : Boolean.valueOf(args[0]);
		System.out.println(ChipsAndGenerators.calculateMinimumSteps(ChipsAndGenerators.input, print));
	}
}

class RunDay11Part2 {
	public static void main(String[] args) {
		boolean print = args == null || args.length != 1 ? true : Boolean.valueOf(args[0]);
		System.out.println(ChipsAndGenerators.calculateMinimumSteps(ChipsAndGenerators.input2, print));
	}
}

class RunJeffDay11Part1 {
	public static void main(String[] args) {
		boolean print = args == null || args.length != 1 ? true : Boolean.valueOf(args[0]);
		System.out.println(ChipsAndGenerators.calculateMinimumSteps(ChipsAndGenerators.jefferyInput, print));
	}
}

class RunAll {
	public static void main(String[] args) {
		RunDay11TestCase.main(new String[]{"false"});
		RunDay11Part1.main(new String[]{"false"});
		RunDay11Part2.main(new String[]{"false"});
		RunJeffDay11Part1.main(new String[]{"false"});
	}
}