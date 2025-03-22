package schkauti.sudoku;

// Point2D is used by AWT
public record Point2(int x, int y) {
	public Point2 withX(final int x) {
		return new Point2(x, this.y);
	}
	
	public Point2 withY(final int y) {
		return new Point2(this.x, y);
	}
}
