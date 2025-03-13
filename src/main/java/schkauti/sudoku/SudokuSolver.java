package schkauti.sudoku;

import com.fasterxml.jackson.databind.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class SudokuSolver {
	public static void main(final String[] args) throws IOException {
		final URL           sudokuFileURI = SudokuSolver.class.getResource("sudoku.json");
		final ObjectMapper  mapper        = new ObjectMapper();
		final SudokuRawData rawData       = mapper.readValue(sudokuFileURI, SudokuRawData.class);
		final SudokuData    data          = SudokuData.fromRaw(rawData);
		
		final List<SudokuData> solutions = solve(data);
		System.out.println(solutions.size());
		printSolutions(solutions);
	}
	
	private static void printSolutions(final List<SudokuData> solutions) {
		for (SudokuData sudokuData : solutions) {
			sudokuData.print();
			System.out.println("---------");
		}
	}
	
	public static List<SudokuData> solve(final SudokuData data) {
		List<SudokuData> possibleSolutions = new ArrayList<>();
		possibleSolutions.add(data);
		
		for (int x = 0; x < data.width; x++) {
			for (int y = 0; y < data.height; y++) {
				
				if (data.isValuePresent(x, y)) {
					continue;
				}
				
				List<Optional<SudokuData>> solutions = new ArrayList<>();
				for (int number = 1; number <= 9; number++) {
					for (SudokuData sudokuData : possibleSolutions) {
						solutions.add(solveValueAt(x, y, number, sudokuData));
					}
				}
				possibleSolutions = solutions.stream()
					.filter(Optional::isPresent)
					.map(Optional::get)
					.toList();
				System.out.printf("%d-%d: %d%n", x, y, possibleSolutions.size());
			}
		}
		
		return possibleSolutions;
	}
	
	public static <T> T getFuture(final Future<T> future) {
		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static Optional<SudokuData> solveValueAt(
		final int x,
		final int y,
		final int value,
		final SudokuData data
	) {
		final boolean placeableRow    = data.isPlaceableRow(x, y, value);
		final boolean placeableColumn = data.isPlaceableColumn(x, y, value);
		final boolean placeableGrid   = data.isPlaceableGrid(x, y, value);
		
		if (placeableColumn && placeableRow && placeableGrid) {
			final SudokuData copy = data.copy();
			copy.set(x, y, value);
			return Optional.of(copy);
		}
		
		return Optional.empty();
	}
}
