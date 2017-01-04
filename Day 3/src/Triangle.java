import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andre on 12/5/2016.
 */
public class Triangle {
	public static void main(String[] args) throws IOException {
		List<String> file = Files.readAllLines(Paths.get("Day 3/input.txt"));
		
		List<int[]> sets1 = new ArrayList<>();
		for (String line : file) {
			String[] setString = line.trim().split("\\s+");
			int[] set = new int[3];
			set[0] = Integer.parseInt(setString[0]);
			set[1] = Integer.parseInt(setString[1]);
			set[2] = Integer.parseInt(setString[2]);
			sets1.add(set);
		}
		System.out.printf("1: %d%n", validateSets(sets1));
		
		List<int[]> sets2 = new ArrayList<>();
		for (int i_line = 0; i_line < file.size(); i_line += 3) {
			String[] line1 = file.get(i_line).trim().split("\\s+");
			String[] line2 = file.get(i_line + 1).trim().split("\\s+");
			String[] line3 = file.get(i_line + 2).trim().split("\\s+");
			sets2.add(new int[]{Integer.parseInt(line1[0]), Integer.parseInt(line2[0]), Integer.parseInt(line3[0])});
			sets2.add(new int[]{Integer.parseInt(line1[1]), Integer.parseInt(line2[1]), Integer.parseInt(line3[1])});
			sets2.add(new int[]{Integer.parseInt(line1[2]), Integer.parseInt(line2[2]), Integer.parseInt(line3[2])});
		}
		System.out.printf("2: %d%n", validateSets(sets2));
	}
	
	private static boolean validateTriangle(int side1, int side2, int side3) {
		return side1 + side2 > side3 && side2 + side3 > side1 && side1 + side3 > side2;
	}
	
	private static int validateSets(List<int[]> sets) {
		int validTriangleCount = 0;
		for (int[] set : sets) {
			if (validateTriangle(set[0], set[1], set[2])) {
				validTriangleCount++;
			}
		}
		return validTriangleCount;
	}
	
	private static class Test {
		public static void main(String[] args) {
			System.out.println(validateTriangle(5, 10, 25));
		}
	}
}
