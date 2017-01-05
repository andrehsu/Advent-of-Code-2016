import com.andre.Input;

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
		
		Set<Node> getNextNodes() {
			Set<Node> nextNodes = new HashSet<>();
			
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
		
		private Optional<Node> getOutput(int elevatorFloor, int destinationFloor, List<Set<Item>> layout, Item... items) {
			layout = cloneListOfSet(layout);
			Set<Item> elevator = new HashSet<>();
			int steps = this.steps;
			
			elevator.addAll(Arrays.asList(items));
			layout.get(elevatorFloor).removeAll(Arrays.asList(items));
			
			for (; elevatorFloor != destinationFloor; elevatorFloor += destinationFloor > elevatorFloor ? 1 : -1) {
				steps++;
				if (checkBlow(elevatorFloor, layout, elevator) == true) {
					return Optional.empty();
				}
			}
			
			layout.get(elevatorFloor).addAll(elevator);
			
			return Optional.of(new Node(isZerothNode ? null : this, elevatorFloor, steps, layout));
		}
		
		private static boolean checkBlow(int floorNumber, List<Set<Item>> layout, Set<Item> elevator) {
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
		
		static Set<Node> firstNodes(List<Set<Item>> initialLayout) {
			Node zerothNode = new Node(null, 0, 0, initialLayout, true);
			return zerothNode.getNextNodes();
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			
			Node node = (Node) o;
			
			if (elevatorFloor != node.elevatorFloor) return false;
			return layout.equals(node.layout);
		}
		
		@Override
		public int hashCode() {
			int result = elevatorFloor;
			result = 31 * result + layout.hashCode();
			return result;
		}
	}
	
	public static final List<String> input = Input.readAllLines("Day 11/input.txt");
	
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
				s = s.replaceAll("The \\w+ floor contains |a |\\.|-compatible", "").replaceAll(",", " and").replaceAll("and and", "and");
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
		Set<Node> traversedNodes = new HashSet<>();
		Set<Node> nodes = Node.firstNodes(initialLayout);
		
		treeDepth:
		for (int treeDepth = 0; treeDepth < 100; treeDepth++) {
			System.out.printf("Depth %d: %d nodes%nTraversedNodes: %d%n%n", treeDepth, nodes.size(), traversedNodes.size());
			Set<Node> nextNodes = new HashSet<>();
			for (Node node : nodes) {
				if (node.isDone()) {
					minimumSteps = node.getSteps();
					break treeDepth;
				} else
					nextNodes.addAll(node.getNextNodes());
			}
			
			
			traversedNodes.addAll(nodes);
			nextNodes.removeAll(traversedNodes);
			nodes = nextNodes;
		}
	}
	
	private static void removeDuplcateNodes(Set<Node> nodes, Set<Node> traversedNodes) {
		
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