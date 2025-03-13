package schkauti;

public class Minesweeper {
	static class Board {
		// boolean[][] fog;
		Field[][] fields;
		
		private Board(final int width, final int height) {
			this.fields = new Field[width][height];
			
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					this.fields[x][y] = new Field();
				}
			}
		}
		
		public int width() {
			return this.fields.length;
		}
		
		public int height() {
			return this.fields[0].length;
		}
		
		public static Board generate(final int width, final int height) {
			// TODO: set nearbyBombs value for fields
			final Board board = new Board(width, height);
			
			board.fields[width - 1][height - 1].makeBomb();
			
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					board.calculateFieldValue(i, j);
				}
			}
			
			return board;
		}
		
		private void calculateFieldValue(final int x, final int y) {
			int value = 0;
			
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					final int currentX = x + i;
					final int currentY = y + j;
					
					if (currentX < 0 || currentY < 0 || currentX == this.width() || currentY == this.height()) {
						continue;
					}
					
					if (this.fields[currentX][currentY].isBomb()) {
						value += 1;
					}
				}
			}
			
			this.fields[x][y].nearbyBombs = value;
		}
		
		static class Field {
			int     nearbyBombs = 0;
			boolean hidden      = true;
			boolean bomb        = false;
			boolean flagged     = false;
			
			public int getNearbyBombs() {
				return this.nearbyBombs;
			}
			
			public boolean isHidden() {
				return this.hidden;
			}
			
			public boolean isBomb() {
				return this.bomb;
			}
			
			public void reveal() {
				this.flagged = false;
				this.hidden  = false;
			}
			
			public boolean isFlag() {
				return this.flagged;
			}
			
			public void makeFlag() {
				this.flagged = true;
			}
			
			public void makeBomb() {
				this.bomb = true;
			}
			
			public char getChar() {
				if (this.isFlag()) {
					return '⚑';
				}
				if (this.isHidden()) {
					return '█';
				}
				if (!this.isHidden() && this.isBomb()) {
					return '●';
				}
				return (char) ('0' + this.nearbyBombs);
			}
		}
		
		public void applyFlag(final int x, final int y) {
			final Field field = this.fields[x][y];
			
			if (field.isHidden()) {
				field.makeFlag();
			}
		}
		
		public boolean applyMove(final int x, final int y) {
			final Field field = this.fields[x][y];
			
			if (field.isBomb()) {
				this.revealBoard();
				return true;
			}
			
			field.reveal();
			return false;
		}
		
		public boolean applyMove(final int x, final int y, final boolean applyFlag) {
			//! TODO: verify move
			
			if (applyFlag) {
				this.applyFlag(x, y);
				return false;
			} else {
				return this.applyMove(x, y);
			}
		}
		
		private void revealBoard() {
			for (final Field[] fs : this.fields) {
				for (final Field f : fs) {
					f.reveal();
				}
			}
		}
		
		public void displayBoard() {
			for (final Field[] fs : this.fields) {
				for (final Field f : fs) {
					System.out.print(f.getChar());
				}
				System.out.println();
			}
		}
	}
	
	public static void main(final String[] args) {
		final Board board = Board.generate(30, 30);
		board.applyFlag(0, 0);
		board.fields[1][0].reveal();
		board.fields[2][0].bomb = true;
		board.displayBoard();
		board.revealBoard();
		board.displayBoard();
	}
}
