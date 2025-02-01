package schkauti;

import java.io.*;
import java.util.*;


/*
Ich habe dieses Spiel nicht mit einem einfachen Text-editor, sondern einer IDE geschrieben.
Ich bin eine IDE zu gewöhnt und hatte einfach Lust mal wieder etwas zu programmieren.
 */

public class NumberGuesserGame {
	public enum GuessingPlayer {
		USER,
		COMPUTER;
	}
	
	public enum Guess {
		HIGHER,
		LOWER,
		CORRECT;
	}
	
	public static class ComputerData {
		private final int min;
		private final int max;
		private       int currentDelta;
		private       int currentGuess;
		private       int previousDelta;
		
		public ComputerData(final int min, final int max) {
			this.min           = min;
			this.max           = max;
			this.currentDelta  = (max - min) / 2;
			this.currentGuess  = min + this.currentDelta;
			this.previousDelta = -1;
		}
		
		public void applyMove(Guess guess) {
			currentDelta = Math.ceilDiv(currentDelta, 2);
			
			if (guess == Guess.HIGHER) {
				currentGuess += currentDelta;
			} else {
				currentGuess -= currentDelta;
			}
			
			currentGuess = Math.clamp(currentGuess, min, max);
			
			if (currentDelta == previousDelta) {
				System.out.println("You're cheating! >:(");
				System.exit(1);
			}
			
			previousDelta = currentDelta;
		}
		
		public int getGuess() {
			return currentGuess;
		}
	}
	
	public static <T extends Enum<T>> List<String> getStringsFromEnum(Class<T> e) {
		return Arrays.stream(e.getEnumConstants()).map(Enum::name).toList();
	}
	
	public static <T extends Enum<T>> Optional<T> getValueFromEnum(Class<T> e, String input) {
		final String uppercaseInput = input.toUpperCase();
		return Arrays.stream(e.getEnumConstants())
			.filter(constant -> constant.name().equals(uppercaseInput))
			.findFirst();
	}
	
	public static final Random RANDOM = new Random();
	
	public static void main(String[] args) {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
			
			int minValue = getNumberFromUser("What is the minimum number?", br);
			int maxValue = getBoundNumberFromUser(minValue, Integer.MAX_VALUE, "What is the maximum number?", br);
			
			ComputerData computerData = new ComputerData(minValue, maxValue);
			
			int    maxGuesses = getNumberFromUser("How often can you guess?", br);
			int    guessWidth = (int) Math.log10(maxGuesses) + 1;
			String format     = String.format("%%%1$dd/%%%1$dd | ", guessWidth);
			
			GuessingPlayer whoIsGuessing = getEnumChoiceFromUser("Who is guessing?", GuessingPlayer.class, br);
			
			int numberToGuess = Integer.MIN_VALUE;
			if (whoIsGuessing == GuessingPlayer.USER) {
				numberToGuess = RANDOM.nextInt(minValue, maxValue + 1);
			}
			
			int guesses = 0;
			while (guesses < maxGuesses) {
				System.out.printf(format, guesses + 1, maxGuesses);
				
				Guess instruction;
				
				if (whoIsGuessing == GuessingPlayer.USER) {
					int number = getNumberFromUser("What is your guess?", br);
					
					if (number == numberToGuess) {
						instruction = Guess.CORRECT;
					} else if (number < numberToGuess) {
						instruction = Guess.HIGHER;
					} else {
						instruction = Guess.LOWER;
					}
					
					System.out.println(capitalize(instruction.name()));
				} else {
					System.out.printf("The computer guessed %d. ", computerData.getGuess());
					instruction = getEnumChoiceFromUser("Must it guess higher or lower?", Guess.class, br);
					computerData.applyMove(instruction);
				}
				
				if (instruction == Guess.CORRECT) {
					break;
				}
				
				guesses++;
			}
			
			System.out.printf("The %s has won!%n", (guesses < maxGuesses) ? "guesser" : "number dude");
		} catch (IOException exc) {
			exc.printStackTrace();
		}
	}
	
	public static <T extends Enum<T>> T getEnumChoiceFromUser(String question, Class<T> eClass, BufferedReader input)
		throws IOException {
		List<String> choices = getStringsFromEnum(eClass).stream().map(NumberGuesserGame::capitalize).toList();
		
		while (true) {
			System.out.println(question);
			System.out.print("Your choices: ");
			System.out.println(String.join(", ", choices));
			
			final Optional<T> userInput = getValueFromEnum(eClass, input.readLine());
			
			if (userInput.isEmpty()) {
				System.out.println("That is not one of the choices.");
				
				continue;
			}
			return userInput.get();
		}
	}
	
	public static int getBoundNumberFromUser(int min, int max, String question, BufferedReader input)
		throws IOException {
		while (true) {
			int number = getNumberFromUser(question, input);
			
			// min and max are inclusive
			// 'number == min' is fine
			if (number < min || number > max) {
				System.out.printf("That number is outside of the given bounds (must be between %d and %d).", min, max);
				continue;
			}
			
			return number;
		}
	}
	
	public static int getNumberFromUser(String question, BufferedReader input) throws IOException {
		while (true) {
			System.out.println(question);
			
			String userInput = input.readLine();
			
			if (!isInteger(userInput)) {
				System.out.println("That is not a number.");
				continue;
			}
			
			return Integer.parseInt(userInput);
		}
	}
	
	public static boolean isInteger(String integer) {
		try {
			Integer.parseInt(integer);
			return true;
		} catch (NumberFormatException exc) {
			return false;
		}
	}
	
	public static String capitalize(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
	}
}
