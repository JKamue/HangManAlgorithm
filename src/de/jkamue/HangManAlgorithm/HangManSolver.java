package de.jkamue.HangManAlgorithm;

import java.util.HashMap;
import java.util.Scanner;

/**
 * 
 * @author  mail
 * @version 1.0
 * @since   2019-04-30 
 */
public class HangManSolver {
	/** Stores the available word lists*/
	static String[] lists = "w10000-w100000-w460000-w1000000".split("-");
	/** The English alphabet */
	static char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
	/** Most used characters in English language */
	static char[] mostUsed = "eariotnslcudpmhgbfywkvxzjq".toCharArray();
	/** Character to symbolize empty char */
	static char empty = "_".charAt(0);
	
	/**
	 * Function that predicts the next most likely character of a word based on a wordlist
	 * and a list of the most frequently used English characters
	 * 
	 * @param guessed Char array with already known characters
	 * @param checked Char array with already checked characters
	 * @param wordlist The Word List that should be used (10000|100000|460000|1000000)
	 * @return The character that is most likely to fill a blank
	 */
	public static char nextGuess(char[] guessed, char[] checked, String wordlist) {
		char mostLikely = getMostUsedChar(goThroughFile(guessed,checked,wordlist));
		// If the word is not in the dictionary take next most likely character from dictionary
		if (mostLikely == empty) {
			for (int i = 0; i < mostUsed.length; i++) {
				if (!contains(checked,mostUsed[i])) {
					// This is the next most likely letter
					mostLikely = mostUsed[i];
					i = 100;
				}
			}
		}
		return mostLikely;
	}
	
	/**
	 * Function that checks if a character is in a char array
	 * 
	 * @param haystack The char array that is to be searched
	 * @param needle The character that is being searched
	 * @return True if character found || False when character not found
	 */
	public static boolean contains(char[] haystack, char needle) {
		boolean ret = false;
		for (int i=0; i<haystack.length; i++) {
			if (haystack[i] == needle) {
				ret = true;
			}
		}
		return ret;
	}
	
	/** 
	 * Opens a file and returns a Scanner that can iterate through it
	 * 
	 * @param name The name of the file
	 * @return Scanner with the file opened
	 */
	private static Scanner readFile(String name) {
		Scanner reader = null;
		
		reader = new Scanner(Main.class.getResourceAsStream("/" + name));
		return reader;
	}
	
	/**
	 * Creates a list with the frequency of possible characters that could fit the blanks
	 * 
	 * @param guessed Char array with already known characters
	 * @param checked Char array with already checked characters
	 * @param wordlist The Word LIst that should be used
	 * @return A hashmap with each character and its frequency
	 */
	private static HashMap<Character, Integer> goThroughFile(char[] guessed, char[] checked, String wordlist) {
		// Hashmap with entry for each character
		HashMap<Character, Integer> possibleChars = new HashMap<Character, Integer>();
		for (int i=0; i < alphabet.length; i++) {
			possibleChars.put(alphabet[i], 0);
		}
		
		// Create scanner for the wished wordlist
		Scanner reader = readFile(wordlist);
		
		// Loop through each word
		while (reader.hasNext()){
		   String word = reader.nextLine().toLowerCase();
		   if (word.length() == guessed.length) {
			   // If the word has the right length
			   char[] tmp = word.toCharArray(); // Convert word to char array
			   boolean same = true; // Boolean that stores if the word is valid
			   String str = ""; // String with possible addable characters
			   
			   // Loop through each character
			   for (int i = 0; i < guessed.length; i++) {
				   if (tmp[i] != guessed[i] && guessed[i] != empty) {
					   // Word is not possible to be right because 2 known chars dont match
					   same = false;
				   }
				   if (guessed[i] == empty && contains(checked,tmp[i])) {
					   // Word is not possible to be right because it contains an already checked char
					   same = false;
				   }
				   if (guessed[i] == empty && !contains(checked,tmp[i])) {
					   // Add possible characters to a list
					   str+=tmp[i];
				   }
			   }
			   
			   // Add the possible characters to an array
			   if (same == true) {
				   char[] possibleLetters =  str.replace("#", "").toCharArray();
				   // Add each character
				   for (int i =0; i < possibleLetters.length; i++ ) {
					   if (contains(alphabet,possibleLetters[i])) {
						   // If the letter is in the english alphabet, add it
						   possibleChars.put(possibleLetters[i], possibleChars.get(possibleLetters[i])+1);
					   }
				   }
			   }
		   }
		}
		return possibleChars;
	}
	
	/**
	 * Determines the character with the highest frequency in the Hashmap
	 * 
	 * @param possibleChars The hashmap with the character as key and the amount as value
	 * @return The most used character
	 */
	private static char getMostUsedChar(HashMap<Character, Integer> possibleChars) {
		char finalChar = empty;
		int uses = 0;
		for (int i = 0; i < possibleChars.size(); i++) {
			// Check if the character is in the defined alphabet
			if (possibleChars.get(alphabet[i]) > uses) {
				uses = possibleChars.get(alphabet[i]);
				finalChar = alphabet[i];
			}
		}
		return finalChar;
	}
}
