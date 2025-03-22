package schkauti.sudoku;

import com.fasterxml.jackson.databind.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class SudokuSolver {
	public static void main(final String[] args) throws IOException {
		final URL           sudokuFileURI = SudokuSolver.class.getResource("sudoku.json");
		final ObjectMapper  mapper        = new ObjectMapper();
		final SudokuRawData rawData       = mapper.readValue(sudokuFileURI, SudokuRawData.class);
		final SudokuData    data          = rawData.convert();
		
		final List<SudokuData> solutions = solve(data);
		System.out.print("Solutions found: ");
		System.out.println(solutions.size());
		printSolutions(solutions);
	}
	
	private static void printSolutions(final List<SudokuData> solutions) {
		for (SudokuData solution : solutions) {
			System.out.print(solution);
			System.out.println("---------");
		}
	}
	
	public static List<SudokuData> solve(final SudokuData sudokuData) {
		List<SudokuData> solutions = List.of(sudokuData);
		
		for (int x = 0; x < sudokuData.width; x++) {
			for (int y = 0; y < sudokuData.height; y++) {
				final Point2 position = new Point2(x, y);
				
				if (sudokuData.isValuePresent(position)) {
					continue;
				}
				
				List<Optional<SudokuData>> possibleSolutions = new ArrayList<>();
				for (int number = 1; number <= sudokuData.maximumNumber(); number++) {
					for (SudokuData solution : solutions) {
						possibleSolutions.add(solveValueAt(position, number, solution));
					}
				}
				
				solutions = possibleSolutions.stream()
					.filter(Optional::isPresent)
					.map(Optional::get)
					.toList();
				// System.out.printf("%d-%d: %d%n", x, y, solutions.size());
			}
		}
		
		return solutions;
	}
	
	private static Optional<SudokuData> solveValueAt(final Point2 position, final int value, final SudokuData data) {
		if (data.isPlaceable(position, value)) {
			final SudokuData copy = data.copy();
			copy.set(position, value);
			return Optional.of(copy);
		}
		
		return Optional.empty();
	}
}
