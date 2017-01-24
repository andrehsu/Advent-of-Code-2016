import andre.adventofcode.input.Input;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Andre on 1/19/2017.
 */
public class StorageMatrixPart2 {
	private enum Disk {
		UNMOVABLE, MOVABLE, EMPTY, GOAL
	}
	
	private static final class Node {
		private static boolean limitToTopTwoRow = false;
		
		private final Node parent;
		private final Table<Integer, Integer, Disk> diskTable;
		private final int steps;
		private final Point movingDisk,
				goalDisk;
		
		int getSteps() {
			return steps;
		}
		
		private Node(Node parent, Table<Integer, Integer, Disk> diskTable, int steps, Point movingDisk, Point goalDisk) {
			if (movingDisk.y == 0 && movingDisk.x == diskTable.rowKeySet().size() - 2)
				limitToTopTwoRow = true;
			this.parent = parent;
			this.diskTable = diskTable;
			this.steps = steps;
			this.movingDisk = movingDisk;
			this.goalDisk = goalDisk;
		}
		
		boolean isSolution() {
			return diskTable.get(0, 0) == Disk.GOAL;
		}
		
		LinkedList<Node> nextNodes() {
			LinkedList<Node> nextNodes = new LinkedList<>();
			
			int x = movingDisk.x, y = movingDisk.y;
			for (Point destinationPoint :
					Arrays.asList(new Point(x, y - 1),
							new Point(x, y + 1),
							new Point(x - 1, y),
							new Point(x + 1, y))) {
				Disk disk = diskTable.get(destinationPoint.x, destinationPoint.y);
				if (disk == null || disk == Disk.UNMOVABLE)
					continue;
				
				if (limitToTopTwoRow && destinationPoint.y > 2)
					continue;
				
				boolean isGoalDisk = false;
				if (disk == Disk.GOAL) {
					isGoalDisk = true;
				}
				
				Table<Integer, Integer, Disk> newDiskTable = HashBasedTable.create(diskTable);
				swapPlace(newDiskTable, movingDisk.x, movingDisk.y, destinationPoint.x, destinationPoint.y);
				nextNodes.add(new Node(this, newDiskTable, steps + 1, destinationPoint, isGoalDisk ? movingDisk : goalDisk));
			}
			
			return nextNodes;
		}
		
		int heuristics() {
			int heuristic = 0;
			heuristic = heuristic * 31 + movingDisk.x;
			heuristic = heuristic * 31 + movingDisk.y;
			heuristic = heuristic * 31 + goalDisk.x;
			heuristic = heuristic * 31 + goalDisk.y;
			return heuristic;
		}
		
		static LinkedList<Node> initialNodes(Table<Integer, Integer, Disk> diskTable) {
			Point movingDisk = null,
					goalDisk = null;
			for (int x = 0; x < diskTable.rowKeySet().size(); x++) {
				for (int y = 0; y < diskTable.row(x).size(); y++) {
					Disk disk = diskTable.get(x, y);
					if (disk == Disk.EMPTY)
						movingDisk = new Point(x, y);
					else if (disk == Disk.GOAL)
						goalDisk = new Point(x, y);
				}
			}
			
			if (movingDisk == null || goalDisk == null)
				return new LinkedList<>();
			
			return new Node(null, diskTable, 0, movingDisk, goalDisk).nextNodes();
		}
		
		private static <R, C, V> void swapPlace(Table<R, C, V> table, R r1, C c1, R r2, C c2) {
			if (r1.equals(r2) && c1.equals(c2)) return;
			V temp = table.get(r1, c1);
			table.put(r1, c1, table.get(r2, c2));
			table.put(r2, c2, temp);
		}
	}
	
	public static final List<String> input = Input.readAllLines("Day 22/input.txt"),
			testInput = Input.readAllLines("Day 22/test input.txt");
	
	private final Table<Integer, Integer, Disk> diskTable;
	
	private int minimumSteps;
	
	private int getMinimumSteps() {
		return minimumSteps;
	}
	
	//<editor-fold desc="Constructors and factories">
	private StorageMatrixPart2(Table<Integer, Integer, Disk> diskTable) {
		this.diskTable = diskTable;
	}
	
	private static StorageMatrixPart2 create(List<String> input) {
		List<List<Integer>> nodeTable = new ArrayList<>(input.size());
		for (String s : input) {
			if (!s.contains("/dev/grid")) continue;
			
			// make values separated by spaces
			s = s.replaceAll("\\/dev\\/grid\\/node-x|T|%", "").replaceAll("-y", " ");
			
			nodeTable.add(
					Arrays.stream(s.trim().split(" +"))
							.mapToInt(Integer::parseInt).boxed().collect(Collectors.toList()));
		}
		
		// sort by size
		nodeTable.sort(Comparator.comparingInt(o -> o.get(2)));
		int median = nodeTable.get(nodeTable.size() / 2).get(2);
		
		Table<Integer, Integer, Disk> diskTable = HashBasedTable.create();
		
		for (List<Integer> diskValues : nodeTable) {
			if (diskValues.get(2) > median * 1.5)
				diskTable.put(diskValues.get(0), diskValues.get(1), Disk.UNMOVABLE);
			else if (diskValues.get(3) == 0)
				diskTable.put(diskValues.get(0), diskValues.get(1), Disk.EMPTY);
			else
				diskTable.put(diskValues.get(0), diskValues.get(1), Disk.MOVABLE);
		}
		
		diskTable.put(diskTable.rowKeySet().size() - 1, 0, Disk.GOAL);
		
		return new StorageMatrixPart2(diskTable);
	}
	//</editor-fold>
	
	private void run() {
		LinkedList<Node> nodes = Node.initialNodes(diskTable);
		Set<Integer> traversedNodes = new HashSet<>();
		
		for (int steps = 1; nodes.size() != 0; steps++) {
			System.out.printf("Depth: %d%nNodes: %d Traversed Nodes: %d%n%n", steps, nodes.size(), traversedNodes.size());
			LinkedList<Node> nextNodes = new LinkedList<>();
			for (Node node : nodes) {
				if (node.isSolution()) {
					minimumSteps = node.getSteps();
					return;
				} else {
					nextNodes.addAll(node.nextNodes());
				}
			}
			nextNodes.removeIf(node -> !traversedNodes.add(node.heuristics()));
			nodes = nextNodes;
		}
	}
	
	private void print() {
		for (int y = 0; y < diskTable.columnKeySet().size(); y++) {
			for (int x = 0; x < diskTable.column(y).size(); x++) {
				switch (diskTable.get(x, y)) {
					case UNMOVABLE:
						System.out.print("# ");
						break;
					case MOVABLE:
						System.out.print(". ");
						break;
					case EMPTY:
						System.out.print("_ ");
						break;
					case GOAL:
						System.out.print("G ");
						break;
				}
			}
			System.out.println();
		}
	}
	
	public static int minimumSteps(List<String> input) {
		StorageMatrixPart2 instance = create(input);
		instance.run();
		return instance.getMinimumSteps();
	}
	
	public static void printMap(List<String> input) {
		StorageMatrixPart2 instance = create(input);
		instance.print();
	}
	
	private static final class Test {
		public static void main(String[] args) {
			printMap(input);
		}
	}
}

class RunDay22Part2Test {
	public static void main(String[] args) {
		System.out.println(StorageMatrixPart2.minimumSteps(StorageMatrixPart2.testInput));
	}
}

class RunDay22Part2 {
	public static void main(String[] args) {
		System.out.println(StorageMatrixPart2.minimumSteps(StorageMatrixPart2.input));
	}
}