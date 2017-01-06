import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Andre on 1/6/2017.
 */
public class Primes {
	public static void main(String[] args) {
		System.out.println("Size?");
		int size = new Scanner(System.in).nextInt();
		List<Integer> list = new ArrayList<>(size);
		for (int i = 2; i < size; i++) {
			list.add(i);
		}
		
		int sqrt = (int) Math.sqrt(size);
		for (int i = 2; i <= sqrt; i++) {
			for (int j = i+i; j < list.size(); j += i) {
				list.remove((Integer) j);
			}
		}
		
		System.out.println(list);
	}
}
