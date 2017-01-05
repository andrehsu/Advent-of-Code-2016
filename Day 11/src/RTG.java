import com.andre.Input;

import java.util.*;

/**
 * Created by Andre on 1/5/2017.
 */
public class RTG {
	private static class Item {
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
	}
	
	private static class Node {
		private final Node parent;
		private final int elevatorFloor;
		private final List<Set<Item>> layout;
		private final Set<Item> elevator;
		
		private Node(Node parent, int elevatorFloor, List<Set<Item>> layout, Set<Item> elevator) {
			this.parent = parent;
			this.elevatorFloor = elevatorFloor;
			this.layout = layout;
			this.elevator = elevator;
		}
		
		Set<Node> getNextNodes() {
			for (int destinationFloor = 0; destinationFloor < 4; destinationFloor++) {
				if(destinationFloor == elevatorFloor) continue;
				
				for (Item item1 : layout.get(elevatorFloor)) {
					
				}
			}
		}
		
		boolean isDone() {
			return layout.get(0).size() == 0 &&
					layout.get(1).size() == 0 &&
					layout.get(2).size() == 0;
		}
		
		static Set<Node> firstNodes(List<Set<Item>> initialLayout) {
			
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
		
		System.out.println();
	}
	
	public void run() {
		Set<Node> nodes = Node.firstNodes(initialLayout);
		step:
		for (int step = 0; step < 1000; step++) {
			Set<Node> nextNodes = new HashSet<>();
			for (Node node : nodes) {
				if (node.isDone()) {
					minimumSteps = step;
					break step;
				} else {
					nextNodes.addAll(node.getNextNodes());
				}
			}
			nodes = nextNodes;
		}
	}
	
	private static <E> List<Set<E>> cloneListOfSet(List<Set<E>> input) {
		List<Set<E>> output = new ArrayList<>(input.size() + 16);
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
	}
}