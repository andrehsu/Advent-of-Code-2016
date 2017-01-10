import andre.adventofcode.input.Input;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andre on 1/6/2017.
 */
public class ValidIP {
	public static final List<String> input = Input.readAllLines("Day 20/input.txt");
	
	public static long lowestValidIP(List<String> bannedRanges) {
		long smallestValue = 0;
		
		long lastSize = -1;
		while (bannedRanges.size() != lastSize) {
			lastSize = bannedRanges.size();
			for (Iterator<String> iterator = bannedRanges.iterator(); iterator.hasNext(); ) {
				String next = iterator.next();
				
				long[] ranges = range(next);
				if (ranges[0] <= smallestValue && smallestValue <= ranges[1]) {
					smallestValue = ranges[1] + 1;
					iterator.remove();
				}
			}
		}
		
		return smallestValue;
	}
	
	public static long numberOfValidIPs(List<String> bannedRanges) {
		mergeRanges(bannedRanges);
		
		long sumRemove = 0;
		for (String s : bannedRanges) {
			long[] range = range(s);
			sumRemove += range[1] - range[0] + 1;
		}
		
		return 4294967295L + 1 - sumRemove;
	}
	
	private static void mergeRanges(List<String> IPs) {
		boolean done;
		outer:
		do {
			done = true;
			for (String s1 : new LinkedList<>(IPs)) {
				long[] range1 = range(s1);
				for (String s2 : new LinkedList<>(IPs)) {
					if (s1.equals(s2)) continue;
					
					long[] range2 = range(s2);
					if (range1[0] <= range2[0] && range2[0] <= range1[1]) {
						IPs.remove(s1);
						IPs.remove(s2);
						IPs.add(range1[0] + "-" + (range2[1] > range1[1] ? range2[1] : range1[1]));
						done = false;
						continue outer;
					}
				}
			}
		} while (!done);
	}
	
	private static long[] range(String input) {
		String[] token_strings = input.trim().split("-");
		return new long[]{Long.parseLong(token_strings[0]),
				Long.parseLong(token_strings[1])};
	}
	
	private static class Test {
		private static final List<String> testInput = Input.readAllLines("Day 20/test input.txt");
		
		public static void main(String[] args) {
			System.out.println(lowestValidIP(testInput));
		}
	}
}

class RunDay20_Part1 {
	public static void main(String[] args) {
		System.out.println(ValidIP.lowestValidIP(ValidIP.input));
	}
}

class RunDay20_Part2 {
	public static void main(String[] args) {
		System.out.println(ValidIP.numberOfValidIPs(ValidIP.input));
	}
}