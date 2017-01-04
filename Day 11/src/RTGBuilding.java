import com.andre.Input;
import javafx.beans.binding.StringBinding;

import java.util.*;

/**
 * Created by andre on 12/16/2016.
 */
public class RTGBuilding {
	public static final List<String> input = Input.readAllLines("Day 11/input.txt");
	
	private enum Movement_Action {
		GO_FLOOR_ONE(1),
		GO_FLOOR_TWO(2),
		GO_FLOOR_THREE(3),
		GO_FLOOR_FOUR(4);
		
		private final int intValue;
		
		Movement_Action(int intValue) {
			this.intValue = intValue;
		}
		
		int intValue() {
			return intValue;
		}
	}
	
	private static class Item {
		enum Type {
			GENERATOR, MICROCHIP;
			
			@Override
			public String toString() {
				return super.toString().toLowerCase();
			}
		}
		
		private final String element;
		private final Type type;
		
		Item(String element, Type type) {
			this.element = element.trim().toLowerCase();
			this.type = type;
		}
		
		@Override
		public String toString() {
			return String.format("%S%S", element.charAt(0), type.toString().charAt(0));
		}
		
		String getElement() {
			return element;
		}
		
		boolean isGenerator() {
			return type == Type.GENERATOR;
		}
		
		boolean isMicrochip() {
			return type == Type.MICROCHIP;
		}
		
		boolean isPair(Item item) {
			return (item.type != this.type) && (item.getElement().equals(this.getElement()));
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
		private final List<Set<Item>> layout;
		private final Movement_Action movement_action;
		private final Set<Node> newNodes = new HashSet<>();
		private final Node parentNode;
		
		private boolean ran = false,
				failed = false;
		private int elevatorFloor;
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (int floor = 0; floor <= 4; floor++) {
				sb.append(floor).append(": ");
				for (Item item : layout.get(floor)) {
					sb.append(item).append(' ');
				}
				sb.append("\n");
			}
			sb.append("\n");
			sb.append("Current ").append(elevatorFloor).append(" Destination ").append(movement_action.intValue());
			
			return sb.toString();
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			
			Node node = (Node) o;
			
			if (elevatorFloor != node.elevatorFloor) return false;
			if (!layout.equals(node.layout)) return false;
			return movement_action == node.movement_action;
		}
		
		@Override
		public int hashCode() {
			int result = layout.hashCode();
			result = 31 * result + movement_action.hashCode();
			result = 31 * result + elevatorFloor;
			return result;
		}
		
		Node(List<Set<Item>> layout, int elevatorFloor, Movement_Action movement_action, Node parentNode) {
			this.layout = layout;
			this.elevatorFloor = elevatorFloor;
			this.movement_action = movement_action;
			this.parentNode = parentNode;
		}
		
		Set<Node> getNewNodes() {
			return newNodes;
		}
		
		static Set<Node> initialNodes(List<Set<Item>> initialLayout) {
			Node blankNode = new Node(initialLayout, 1, Movement_Action.GO_FLOOR_ONE, null);
			blankNode.ran = true;
			blankNode.generateNewNodes();
			return blankNode.getNewNodes();
		}
		
		void run() {
			if (ran) return;
			
			ran = true;
			
			int destinationElevatorFloor = movement_action.intValue();
			
			while (destinationElevatorFloor != elevatorFloor && !failed) {
				elevatorFloor += destinationElevatorFloor > elevatorFloor ? 1 : -1;
				checkFail();
			}
			// Deposit items in elevator into floor
			layout.get(elevatorFloor).addAll(layout.get(0));
			layout.get(0).clear();
			checkFail();
			
			generateNewNodes();
		}
		
		private void generateNewNodes() {
			if (ran && !failed) {
				for (Movement_Action movementAction : Movement_Action.values()) {
					if (movementAction.intValue() == elevatorFloor) continue;
					
					for (Item item : layout.get(elevatorFloor)) {
						// Add single item
						List<Set<Item>> newLayout = cloneListOfSet(layout);
						newLayout.get(elevatorFloor).remove(item);
						newLayout.get(0).add(item);
						newNodes.add(new Node(newLayout, elevatorFloor, movementAction, this));
						
						// Add two items
						for (Item secondItem : layout.get(elevatorFloor)) {
							if (!secondItem.equals(item)) {
								newLayout = cloneListOfSet(layout);
								newLayout.get(elevatorFloor).remove(item);
								newLayout.get(elevatorFloor).remove(secondItem);
								newLayout.get(0).add(item);
								newLayout.get(0).add(secondItem);
								newNodes.add(new Node(newLayout, elevatorFloor, movementAction, this));
							}
						}
					}
				}
			}
		}
		
		boolean isAllOnFourthFloor() {
			return layout.get(0).size() == 0 &&
					layout.get(1).size() == 0 &&
					layout.get(2).size() == 0 &&
					layout.get(3).size() == 0;
		}
		
		private void checkFail() {
			if (!failed) {
				floor:
				for (int i = 1; i <= 4; i++) {
					Set<Item> floor;
					// Add elevator to floor if elevator is on the same floor
					if (elevatorFloor == i) {
						floor = new HashSet<>(layout.get(i));
						floor.addAll(layout.get(0));
					} else
						floor = layout.get(i);
					
					item:
					for (Item item : floor) {
						if (item.isMicrochip()) {
							for (Item item2 : floor) {
								if (item2.isGenerator() && item2.isPair(item)) {
									continue item;
								}
							}
							
							for (Item item2 : floor) {
								if (item2.isGenerator() && !item2.isPair(item)) {
									failed = true;
									break floor;
								}
							}
						}
					}
				}
			}
		}
		
		private static class Test {
			public static void main(String[] args) {
				Item LM = new Item("lithium", Item.Type.MICROCHIP),
						LG = new Item("lithium", Item.Type.GENERATOR),
						HM = new Item("hydrogen", Item.Type.MICROCHIP),
						HG = new Item("hydrogen", Item.Type.GENERATOR);
				
				List<Set<Item>> layout = new ArrayList<>(5);
				
				Set<Item> elevator = new HashSet<>();
				layout.add(elevator);
				
				Set<Item> firstFloor = new HashSet<>();
				layout.add(firstFloor);
				
				Set<Item> secondFloor = new HashSet<>();
				layout.add(secondFloor);
				
				Set<Item> thirdFloor = new HashSet<>();
				thirdFloor.add(HM);
				thirdFloor.add(LM);
				layout.add(thirdFloor);
				
				Set<Item> fourthFloor = new HashSet<>();
				fourthFloor.add(HG);
				firstFloor.add(LG);
				layout.add(fourthFloor);
				
				Node node = new Node(layout, 3, null, null);
				node.checkFail();
				System.out.println(node.failed);
				System.out.println(node.isAllOnFourthFloor());
				
				node.ran = true;
				node.generateNewNodes();
				for (Node newNode : node.newNodes) {
					System.out.println(newNode);
				}
			}
		}
	}
	
	private final List<Set<Item>> initialLayout;
	
	private int minimumSteps = -1;
	
	public int getMinimumSteps() {
		return minimumSteps;
	}
	
	public RTGBuilding(List<String> startingArrangement) {
		int numberOfItems = 0;
		initialLayout = new ArrayList<>(5);
		while (initialLayout.size() < 5) {
			initialLayout.add(new HashSet<>());
		}
		
		int floor = 0;
		for (String s : startingArrangement) {
			floor++;
			
			// Remove unnecessary words
			s = s.replaceAll("\\b(a |The \\w+ floor contains |relevant)\\b|-compatible\\b|\\.", "");
			s = s.replaceAll(", (and )?", " and ");
			
			// If not empty
			if (!s.contains("nothing")) {
				String[] item_strings = s.trim().split(" and ");
				for (String item_string : item_strings) {
					String[] words = item_string.trim().split(" ");
					
					Item item;
					if (words[1].equals("generator")) {
						item = new Item(words[0], Item.Type.GENERATOR);
					} else {
						item = new Item(words[0], Item.Type.MICROCHIP);
					}
					
					numberOfItems++;
					initialLayout.get(floor).add(item);
				}
			}
		}
		System.out.printf("Parsed %d items%n", numberOfItems);
	}
	
	public void run() {
		Set<Node> nodes = Node.initialNodes(initialLayout);
		
		step:
		for (int step = 1; step < 100; step++) {
			System.out.printf("%d: %d%n", step, nodes.size());
			
			Set<Node> newNodes = new HashSet<>();
			for (Node node : nodes) {
				node.run();
				if (node.isAllOnFourthFloor()) {
					minimumSteps = step;
					System.out.println(node);
					break step;
				} else {
					newNodes.addAll(node.getNewNodes());
				}
			}
			nodes = newNodes;
		}
	}
	
	private static <E> List<Set<E>> cloneListOfSet(List<Set<E>> original) {
		List<Set<E>> output = new ArrayList<>(original.size());
		for (Set<E> es : original) {
			output.add(new HashSet<E>(es));
		}
		return output;
	}
	
	private static class Test {
		public static final List<String> testInput = Input.readAllLines("Day 11/test input.txt");
		
		public static void main(String[] args) {
			RTGBuilding rtgBuilding = new RTGBuilding(testInput);
			rtgBuilding.run();
			System.out.println(rtgBuilding.getMinimumSteps());
		}
	}
}

class RunDay11Part1 {
	public static void main(String[] args) {
		RTGBuilding rtgBuilding = new RTGBuilding(RTGBuilding.input);
		rtgBuilding.run();
		System.out.println(rtgBuilding.getMinimumSteps());
	}
}