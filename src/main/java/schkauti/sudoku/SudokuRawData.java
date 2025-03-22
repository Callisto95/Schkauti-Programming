package schkauti.sudoku;

import com.fasterxml.jackson.annotation.*;

/**
 * json values
 * intermediary, as not all values are currently used
 * will be removed at some point (probably)
 */
public class SudokuRawData {
	@JsonProperty
	int[]   dimension;
	@JsonProperty
	int[][] sudoku;
	
	// ? What are these values?
	@JsonProperty
	int[][][] thermo;
	@JsonProperty
	int[][][] whisper;
	@JsonProperty
	int[][][] x;
	@JsonProperty
	int[][][] v;
	
	public SudokuData convert() {
		return new SudokuData(this.sudoku, new Point2(this.dimension[0], this.dimension[1]));
	}
}
