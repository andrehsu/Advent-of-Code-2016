import com.andre.Input;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.List;

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
			for (int i = 0; i < tokens.length; i++) {
				ints[i] = Integer.parseInt(tokens[i]);
			}
			
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
	
	public static int viablePairs(List<String> df) {
		// remove header
		df.subList(0, 2).clear();
		
		Table<Integer, Integer, Disk> diskTable = HashBasedTable.create();
		
		for (String s : df) {
			Disk disk = Disk.create(s);
			diskTable.put(disk.getX(), disk.getY(), disk);
		}
		
		int count = 0;
		for (Disk diskA : diskTable.values()) {
			for (Disk diskB : diskTable.values()) {
				if (viablePair(diskA, diskB)) count++;
			}
		}
		
		return count;
	}
	
	private static boolean viablePair(Disk diskA, Disk diskB) {
		return diskA != null && diskB != null && diskA.getUsed() != 0 && diskB.getAvailable() - diskA.getUsed() >= 0 && diskA != diskB;
	}
	
	private static class Test {
		public static void main(String[] args) {
			
		}
	}
}

class RunDay22Part1 {
	public static void main(String[] args) {
		System.out.println(StorageMatrix.viablePairs(StorageMatrix.input));
	}
}