import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Andre on 1/6/2017.
 */
public class Primes {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("From?");
		int from = scanner.nextInt();
		System.out.println("To?");
		int to = scanner.nextInt();
		List<Integer> list = new LinkedList<>();
		for (int i = from < 2 ? 2 : from; i <= to; i++) {
			list.add(i);
		}
		
		int sqrt = (int) Math.sqrt(to);
		for (int i = 2; i <= sqrt; i++) {
			for (int j = i + i; j <= to; j += i) {
				list.remove((Integer) j);
			}
		}
		
		System.out.println(list);
	}
}
