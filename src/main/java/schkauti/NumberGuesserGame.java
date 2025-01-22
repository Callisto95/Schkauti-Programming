package schkauti;

import java.io.*;
import java.util.*;

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
		int min;
		int max;
		int currentDelta;
		int currentGuess;
		
		public ComputerData(final int min, final int max) {
			this.min          = min;
			this.max          = max;
			this.currentDelta = (max - min) / 2;
			this.currentGuess = this.min + this.currentDelta;
		}
		
		public void applyMove(Guess guess) {
			currentDelta /= 2;
			
			if (currentDelta == 0) {
				System.out.println("You're cheating! >:(");
				System.exit(1);
			}
			
			if (guess == Guess.HIGHER) {
				currentGuess += currentDelta;
			} else {
				currentGuess -= currentDelta;
			}
		}
		
		public int getGuess() {
			return currentGuess;
		}
	}
	
	public static <T extends Enum<T>> List<String> getStringsFromEnum(Class<T> e) {
		return Arrays.stream(e.getEnumConstants()).map(Enum::name).toList();
	}
	
	public static <T extends Enum<T>> Optional<T> getValueFromEnum(Class<T> e, String input) {
		return Arrays.stream(e.getEnumConstants()).filter(constant -> constant.name().equals(input.toUpperCase())).findFirst();
	}
	
	public static final Random RANDOM = new Random();
	
	public static void main(String[] args) {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
			
			int minValue = getNumberFromUser("What is the minimum number?", br);
			int maxValue = getBoundNumberFromUser(minValue, Integer.MAX_VALUE, "What is the maximum number?", br);
			
			ComputerData computerData = new ComputerData(minValue, maxValue);
			
			int maxTries = getNumberFromUser("How often can you guess?", br);
			
			GuessingPlayer whoIsGuessing = getEnumChoiceFromUser("Who is guessing?", GuessingPlayer.class, br);
			
			int numberToGuess = Integer.MIN_VALUE;
			if (whoIsGuessing == GuessingPlayer.USER) {
				numberToGuess = RANDOM.nextInt(minValue, maxValue + 1);
			}
			
			int guesses = 0;
			while (guesses < maxTries) {
				if (whoIsGuessing == GuessingPlayer.USER) {
					int number = getNumberFromUser("What is your guess?", br);
					
					if (number == numberToGuess) {
						break;
					}
					
					System.out.println(capitalize(((number < numberToGuess) ? Guess.HIGHER : Guess.LOWER).name()));
				} else {
					System.out.printf("The computer guessed %d. ", computerData.getGuess());
					Guess userInstruction = getEnumChoiceFromUser("Is that higher or lower?", Guess.class, br);
					
					if (userInstruction == Guess.CORRECT) {
						break;
					}
					
					computerData.applyMove(userInstruction);
				}
				
				guesses++;
			}
			
			System.out.printf("%s has won!%n", (guesses < maxTries) ? "Guesser" : "The number dude");
		} catch (IOException exc) {
			exc.printStackTrace();
		}
	}
	
	public static <T extends Enum<T>> T getEnumChoiceFromUser(String question, Class<T> eClass, BufferedReader input) throws IOException {
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
	
	public static int getBoundNumberFromUser(int min, int max, String question, BufferedReader input) throws IOException {
		while (true) {
			int number = getNumberFromUser(question, input);
			
			if (number < min || number > max) {
				System.out.println("That number is outside of the given bounds.");
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
