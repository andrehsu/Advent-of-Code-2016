import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andre on 12/4/2016.
 */
public class HashTest {
	public static void main(String[] args) {
		Point point1 = new Point(-1, 1);
		Point point2 = new Point(1, -1);
		
		System.out.println(point1.hashCode() == point2.hashCode());
		System.out.println(point1.equals(point2));
		
		Set<Point> points = new HashSet<>();
		points.add(point1);
		
		System.out.println(points.contains(point2));
	}
}
