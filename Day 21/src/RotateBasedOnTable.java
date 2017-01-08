import java.util.Scanner;

/**
 * Created by Andre on 1/8/2017.
 */
public class RotateBasedOnTable {
	public static void main(String[] args) {
		System.out.println("Length?");
		int length = new Scanner(System.in).nextInt();
		for (int i = 0; i < length; i++) {
			System.out.printf("%d: %d%n", i, newIndex(i, length));
		}
	}
	
	private static int newIndex(int basedOn, int length) {
		int shift = 1 + basedOn + (basedOn >= 4 ? 1 : 0);
		return (basedOn + shift) % length;
	}
}
