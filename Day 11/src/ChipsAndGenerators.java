import andre.adventofcode.input.Input;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
			
			toString = this.element.substring(0, 2).toUpperCase() + Character.toUpperCase(this.type.toString().charAt(0));
			
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
		boolean isPair(Item item2) {
			return type != item2.type && element.equals(item2.element);
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			
			Item item = (Item) o;
			
			return element.equals(item.element) && type == item.type;
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
		private final Node parent;
		private final String moves;
		private final List<Set<Item>> layout;
		private final int elevatorFloor;
		private final int steps;
		
		int getSteps() {
			return steps;
		}
		
		private Node(Node parent, String moves, List<Set<Item>> layout, int elevatorFloor, int steps) {
			this.parent = parent;
			this.moves = moves;
			this.layout = layout;
			this.elevatorFloor = elevatorFloor;
			this.steps = steps;
		}
		
		boolean isSolution() {
			return layout.get(0).isEmpty() &&
					layout.get(1).isEmpty() &&
					layout.get(2).isEmpty() &&
					elevatorFloor == 3;
		}
		
		LinkedList<Node> nextNodes() {
			LinkedList<Node> output = new LinkedList<>();
			
			for (int destinationFloor : Arrays.asList(elevatorFloor - 1, elevatorFloor + 1)) {
				// Outside of the building
				if (destinationFloor < 0 || destinationFloor > 3) continue;
				
				List<Set<Item>> nextLayout;
				
				for (Item item1 : layout.get(elevatorFloor)) {
					nextLayout = getLayout(layout, elevatorFloor, destinationFloor, item1);
					if (!hasBlownChip(nextLayout)) {
						output.add(new Node(
								this,
								moves + item1 + " -> " + (destinationFloor + 1) + "\n",
								nextLayout,
								destinationFloor,
								steps + 1)
						);
					}
					
					for (Item item2 : layout.get(elevatorFloor)) {
						if (item1 == item2) continue; // Same item twice
						nextLayout = getLayout(layout, elevatorFloor, destinationFloor, item1, item2);
						if (!hasBlownChip(nextLayout)) {
							output.add(new Node(
									this,
									moves + item1 + ", " + item2 + " -> " + (destinationFloor + 1) + "\n",
									nextLayout,
									destinationFloor,
									steps + 1)
							);
						}
					}
				}
			}
			
			return output;
		}
		
		String abstraction() {
			StringBuilder output = new StringBuilder(elevatorFloor + "|");
			for (Set<Item> items : layout) {
				int generators = 0,
						microchips = 0;
				for (Item item : items) {
					if (item.isMicrochip())
						microchips++;
					else
						generators++;
				}
				output.append(microchips).append(' ')
						.append(generators).append(' ');
			}
			return output.toString();
		}
		
		void printMove() {
			System.out.println(moves);
		}
		
		private static boolean hasBlownChip(List<Set<Item>> layout) {
			for (Set<Item> floor : layout) {
				item1:
				for (Item item1 : floor) {
					if (item1.isMicrochip()) {
						for (Item item2 : floor) {
							// If generator is on the same floor
							if (item1.isPair(item2)) {
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
			}
			return false;
		}
		
		static LinkedList<Node> firstNode(List<Set<Item>> initialLayout) {
			return new Node(null, "", initialLayout, 0, 0).nextNodes();
		}
		
		private static <E> List<Set<E>> cloneListOfSet(List<Set<E>> original) {
			List<Set<E>> clone = new ArrayList<>(original.size());
			original.forEach(es -> clone.add(new HashSet<>(es)));
			return clone;
		}
		
		private static List<Set<Item>> getLayout(List<Set<Item>> layout, int fromFloor, int toFloor, Item... items) {
			layout = cloneListOfSet(layout);
			Set<Item> itemSet = new HashSet<>(Arrays.asList(items));
			layout.get(toFloor).addAll(itemSet);
			layout.get(fromFloor).removeAll(itemSet);
			return layout;
		}
	}
	
	public static final List<String> testInput = Input.readAllLines("Day 11/test input.txt"),
			input = Input.readAllLines("Day 11/input.txt"),
			input2 = Input.readAllLines("Day 11/input part 2.txt"),
			jefferyInput = Input.readAllLines("Day 11/jeffery input.txt"),
			jefferyInput2 = Input.readAllLines("Day 11/jeffery input part 2.txt");
	
	private static final boolean print = true;
	
	private final List<Set<Item>> initialLayout;
	
	private int minimumSteps = -1;
	
	private int getMinimumSteps() {
		return minimumSteps;
	}
	
	private ChipsAndGenerators(List<Set<Item>> initialLayout) {
		this.initialLayout = initialLayout;
	}
	
	private static ChipsAndGenerators create(List<String> input) {
		Pattern generatorPattern = Pattern.compile("\\w+(?= generator)"),
				microchipPattern = Pattern.compile("\\w+(?=-compatible microchip)");
		
		List<Set<Item>> initialLayout = new ArrayList<>(4);
		for (String s : input) {
			Set<Item> floor = new HashSet<>();
			
			if (!s.contains("nothing")) {
				Matcher generators = generatorPattern.matcher(s);
				while (generators.find()) {
					floor.add(new Item(generators.group(), Item.Type.GENERATOR));
				}
				
				Matcher microchips = microchipPattern.matcher(s);
				while (microchips.find()) {
					floor.add(new Item(microchips.group(), Item.Type.MICROCHIP));
				}
			}
			
			initialLayout.add(floor);
		}
		
		return new ChipsAndGenerators(initialLayout);
	}
	
	private ChipsAndGenerators run() {
		LinkedList<Node> nodes = Node.firstNode(initialLayout);
		Set<String> abstractions = new HashSet<>();
		
		for (int depth = 1; nodes.size() != 0; depth++) {
			if (print)
				System.out.printf("Depth %d: %d nodes%nTraversed nodes: %d%n%n", depth, nodes.size(), abstractions.size());
			
			LinkedList<Node> nextNodes = new LinkedList<>();
			for (Node node : nodes) {
				if (node.isSolution()) {
					minimumSteps = node.getSteps();
					if (print) node.printMove();
					return this;
				} else {
					nextNodes.addAll(node.nextNodes());
				}
			}
			// Remove if cannot add (already exists)
			nextNodes.removeIf(next -> !abstractions.add(next.abstraction()));
			nodes = nextNodes;
		}
		
		return this;
	}
	
	public static int calculateMinimumSteps(List<String> input) {
		return create(input).run().getMinimumSteps();
	}
}

class RunDay11_TestCase {
	public static void main(String[] args) {
		System.out.println(ChipsAndGenerators.calculateMinimumSteps(ChipsAndGenerators.testInput));
	}
}

class RunDay11_Part1 {
	public static void main(String[] args) {
		System.out.println(ChipsAndGenerators.calculateMinimumSteps(ChipsAndGenerators.input));
	}
}

class RunDay11_Part2 {
	public static void main(String[] args) {
		System.out.println(ChipsAndGenerators.calculateMinimumSteps(ChipsAndGenerators.input2));
	}
}

class RunJeffDay11 {
	public static void main(String[] args) {
		System.out.println(ChipsAndGenerators.calculateMinimumSteps(ChipsAndGenerators.jefferyInput));
		System.out.println(ChipsAndGenerators.calculateMinimumSteps(ChipsAndGenerators.jefferyInput2));
	}
}