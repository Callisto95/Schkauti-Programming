package schkauti.sudoku;

public class SudokuData {
	// final private Point2 dimension;
	final int width;
	final int height;
	
	int[][] numbers;
	
	protected SudokuData(final int[][] data) {
		this.numbers = data;
		this.width   = data.length;
		this.height  = data[0].length;
	}
	
	public boolean isPlaceableColumn(final int x, final int y, final int value) {
		for (int currentX = 0; currentX < this.width; currentX++) {
			if (currentX == x) {
				continue;
			}
			
			if (this.numbers[currentX][y] == value) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isPlaceableRow(final int x, final int y, final int value) {
		for (int currentY = 0; currentY < this.width; currentY++) {
			if (currentY == y) {
				continue;
			}
			
			if (this.numbers[x][currentY] == value) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isPlaceableGrid(final int x, final int y, final int value) {
		final int gridX = (x / 3) * 3;
		final int gridY = (y / 3) * 3;
		
		for (int relX = 0; relX < 3; relX++) {
			for (int relY = 0; relY < 3; relY++) {
				final int posX = gridX + relX;
				final int posY = gridY + relY;
				
				if (posX == x && posY == y) {
					continue;
				}
				
				if (this.numbers[posX][posY] == value) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static SudokuData fromRaw(final SudokuRawData rawData) {
		return new SudokuData(rawData.sudoku);
	}
	
	public SudokuData copy() {
		final int[][] dataCopy = new int[this.width][this.height];
		
		for (int i = 0; i < this.height; i++) {
			dataCopy[i] = this.numbers[i].clone();
		}
		
		return new SudokuData(dataCopy);
	}
	
	public void set(final int x, final int y, final int value) {
		this.numbers[x][y] = value;
	}
	
	public boolean isValuePresent(final int x, final int y) {
		return this.numbers[x][y] != 0;
	}
	
	public void print() {
		for(int x = 0; x < this.width; x++) {
			for(int y = 0; y < this.height; y++) {
				System.out.print(this.numbers[x][y]);
			}
			System.out.println();
		}
	}
}
