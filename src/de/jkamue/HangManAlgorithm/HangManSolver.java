package de.jkamue.HangManAlgorithm;

import java.util.HashMap;
import java.util.Scanner;

/**
 * 
 * @author  JKamue
 * @version 2.0
 * @since   2019-05-03
 */
public class HangManSolver {
	/** The English alphabet */
	public char[] alphabet;
	/** Most used characters in English language */
	private char[] mostUsed;
	/** Character to symbolize empty char */
	public final char empty;
	/** The wordlist the user chose */
	private String wordlist;
	
	/**
	 * Creates a new HangMan solver with a mode 0-3
	 * 
	 * @param mode The size of the wordlist (0 smallest, 3 biggest)
	 */
	public HangManSolver(String[] lang, int mode, String language) {
		alphabet = lang[0].toCharArray();
		mostUsed = lang[1].toCharArray();
		empty = "_".charAt(0);
		wordlist = language + "/" + lang[mode+1].split("-")[1]+".txt";
	}
	
	/**
	 * Generates and returns a language list
	 * 
	 * @return String with all available languages
	 */
	public static String[] getLanguages() {
		Scanner lang = readFile("lang.txt");
		String[] languages = new String[lang.nextInt()];
		for (int i=0; i < languages.length; i++) {
			languages[i] = lang.next();
		}
		return languages;
	}
	
	/**
	 * Gets more information on one language
	 * 
	 * @param code The short code of the language
	 * @return Available languages
	 */
	public static String[] getLanguage(String code) {
		Scanner lang = readFile(code + "/" + code + ".txt");
		String[] language = new String[lang.nextInt()];
		language[0] = lang.next();
		language[1] = lang.next();
		lang.nextLine();
		for (int i=2; i < language.length; i++) {
			language[i] = lang.nextLine();
		}
		return language;
	}
	
	/**
	 * Function that predicts the next most likely character of a word based on a wordlist
	 * and a list of the most frequently used English characters
	 * 
	 * @param guessed Char array with already known characters
	 * @param checked Char array with already checked characters
	 * @param wordlist The Word List that should be used (10000|100000|460000|1000000)
	 * @return The character that is most likely to fill a blank
	 */
	public char nextGuess(char[] guessed, char[] checked) {
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
	private static Scanner readFile(String filename) {
		Scanner reader = null;
		reader = new Scanner(HangManSolver.class.getResourceAsStream("/" + filename));
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
	private HashMap<Character, Integer> goThroughFile(char[] guessed, char[] checked, String wordlist) {
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
	private char getMostUsedChar(HashMap<Character, Integer> possibleChars) {
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
