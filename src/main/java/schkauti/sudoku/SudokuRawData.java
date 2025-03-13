package schkauti.sudoku;

import com.fasterxml.jackson.annotation.*;

public class SudokuRawData {
	@JsonProperty
	int[] dimension;
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
}
