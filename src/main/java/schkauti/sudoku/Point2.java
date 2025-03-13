package schkauti.sudoku;

// Point2D is used by AWT
public class Point2 {
	final int x;
	final int y;
	
	public Point2(final int x, final int y) {
		this.x = x;
		this.y = y;
	}
	
	public Point2(final int[] data) {
		this.x = data[0];
		this.y = data[1];
	}
	
	public Point2 withX(final int x) {
		return new Point2(x, this.y);
	}
	
	public Point2 withY(final int y) {
		return new Point2(this.x, y);
	}
}
