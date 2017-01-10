import andre.adventofcode.input.Input;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.List;
import java.util.Map;

/**
 * Created by Andre on 1/8/2017.
 */
public class StorageMatrix {
	private static final class Disk {
		final int x,
				y,
				size,
				used,
				available,
				usedPercentage;
		
		static Disk create(String dfLine) {
			String tokens[] = dfLine.replaceAll("-y", " ").replaceAll("\\/dev\\/grid\\/node-x|T|%", "").trim().split("\\s+");
			
			int[] ints = new int[tokens.length];
			for (int i = 0; i < tokens.length; i++) ints[i] = Integer.parseInt(tokens[i]);
			
			int x = ints[0],
					y = ints[1],
					size = ints[2],
					used = ints[3],
					available = ints[4],
					usedPercentage = ints[5];
			
			return new Disk(x, y, size, used, available, usedPercentage);
		}
		
		private Disk(int x, int y, int size, int used, int available, int usedPercentage) {
			this.x = x;
			this.y = y;
			this.size = size;
			this.used = used;
			this.available = available;
			this.usedPercentage = usedPercentage;
		}
		
		int getX() {
			return x;
		}
		
		int getY() {
			return y;
		}
		
		int getSize() {
			return size;
		}
		
		int getUsed() {
			return used;
		}
		
		int getAvailable() {
			return available;
		}
		
		int getUsedPercentage() {
			return usedPercentage;
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			
			Disk disk = (Disk) o;
			
			if (x != disk.x) return false;
			if (y != disk.y) return false;
			if (size != disk.size) return false;
			if (used != disk.used) return false;
			if (available != disk.available) return false;
			return usedPercentage == disk.usedPercentage;
		}
		
		@Override
		public int hashCode() {
			int result = x;
			result = 31 * result + y;
			result = 31 * result + size;
			result = 31 * result + used;
			result = 31 * result + available;
			result = 31 * result + usedPercentage;
			return result;
		}
	}
	
	public static final List<String> input = Input.readAllLines("Day 22/input.txt");
	
	
	public static int minimumSteps(Table<Integer, Integer, Disk> diskTable) {
		return -1;
	}
	
	public static void printTable(Table<Integer, Integer, Disk> diskTable) {
		for (Map<Integer, Disk> column : diskTable.rowMap().values()) {
			for (Disk disk : column.values()) {
				if (disk.getY() == 0 && disk.getX() == diskTable.columnKeySet().size() - 1) {
					System.out.printf("%s ", 'G');
				} else {
					int viablePairCount = 0;
					for (Disk diskA : diskTable.values()) {
						if (isViablePair(diskA, disk)) viablePairCount++;
					}
					
					if (viablePairCount == 0) {
						System.out.printf("%s ", '#');
					} else if (viablePairCount == diskTable.size() - 1) {
						System.out.printf("%s ", '_');
					} else {
						System.out.printf("%s ", '.');
					}
				}
			}
			System.out.println();
		}
	}
	
	public static Table<Integer, Integer, Disk> generateDiskTable(List<String> df) {
		Table<Integer, Integer, Disk> diskTable = HashBasedTable.create();
		
		for (String s : df) {
			if (!s.matches("\\/dev\\/grid\\/node-x\\d+-y\\d+ +\\d+T +\\d+T +\\d+T +\\d+%")) continue;
			Disk disk = Disk.create(s);
			diskTable.put(disk.getY(), disk.getX(), disk);
		}
		
		return diskTable;
	}
	
	public static int countViablePairs(Table<Integer, Integer, Disk> diskTable) {
		int count = 0;
		for (Disk diskA : diskTable.values()) {
			count += countViablePairsWith(diskA, diskTable);
		}
		
		return count;
	}
	
	private static int countViablePairsWith(Disk diskA, Table<Integer, Integer, Disk> diskTable) {
		int count = 0;
		for (Disk diskB : diskTable.values()) {
			if (isViablePair(diskA, diskB)) count++;
		}
		return count;
	}
	
	private static boolean isViablePair(Disk diskA, Disk diskB) {
		return diskA != diskB &&
				diskA.getUsed() != 0 &&
				diskB.getAvailable() - diskA.getUsed() >= 0;
	}
	
	private static class Test {
		private static final List<String> testInput = Input.readAllLines("Day 22/test input.txt");
		
		public static void main(String[] args) {
			printTable(generateDiskTable(testInput));
			System.out.println();
			printTable(generateDiskTable(input));
		}
	}
}

class RunDay22Part1 {
	public static void main(String[] args) {
		System.out.println(StorageMatrix.countViablePairs(StorageMatrix.generateDiskTable(StorageMatrix.input)));
	}
}