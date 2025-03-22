package schkauti.sudoku;

public class SudokuData {
	// final private Point2 dimension;
	final int    width;
	final int    height;
	final Point2 boxDimension;
	
	int[][] numbers;
	
	protected SudokuData(final int[][] data, final Point2 boxDimension) {
		this.boxDimension = boxDimension;
		this.numbers      = data;
		this.width        = data.length;
		this.height       = data[0].length;
	}
	
	public int maximumNumber() {
		return this.boxDimension.x() * this.boxDimension.y();
	}
	
	public boolean isPlaceableColumn(final Point2 position, final int value) {
		for (int currentX = 0; currentX < this.width; currentX++) {
			if (currentX == position.x()) {
				continue;
			}
			
			if (get(position.withX(currentX)) == value) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isPlaceableRow(final Point2 position, final int value) {
		for (int currentY = 0; currentY < this.width; currentY++) {
			if (currentY == position.y()) {
				continue;
			}
			
			if (get(position.withY(currentY)) == value) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isPlaceableGrid(final Point2 position, final int value) {
		final int gridX = (position.x() / this.boxDimension.x()) * this.boxDimension.x();
		final int gridY = (position.y() / this.boxDimension.y()) * this.boxDimension.y();
		
		for (int relativeX = 0; relativeX < this.boxDimension.x(); relativeX++) {
			for (int relativeY = 0; relativeY < this.boxDimension.y(); relativeY++) {
				final Point2 currentPosition = new Point2(gridX + relativeX, gridY + relativeY);
				
				if (currentPosition.equals(position)) {
					continue;
				}
				
				if (get(currentPosition) == value) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean isPlaceable(final Point2 position, final int value) {
		return isPlaceableColumn(position, value) && isPlaceableRow(position, value) && isPlaceableGrid(position, value);
	}
	
	public SudokuData copy() {
		final int[][] dataCopy = new int[this.width][this.height];
		
		for (int i = 0; i < this.height; i++) {
			dataCopy[i] = this.numbers[i].clone();
		}
		
		return new SudokuData(dataCopy, this.boxDimension);
	}
	
	public void set(final Point2 position, final int value) {
		this.numbers[position.x()][position.y()] = value;
	}
	
	public int get(final Point2 position) {
		return this.numbers[position.x()][position.y()];
	}
	
	public boolean isValuePresent(final Point2 position) {
		return get(position) != 0;
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		
		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++) {
				builder.append(this.numbers[x][y]);
			}
			builder.append('\n');
		}
		
		return builder.toString();
	}
}
