package de.jkamue.HangManAlgorithm;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Example implementation of the HangManSolver class
 * 
 * @author  mail
 * @version 1.0
 * @since   2019-04-30 
 */
public class Main {
	/** Stores the checked letters */
	static char[] checked = new char[HangManSolver.alphabet.length];
	/** Stores the word known by the pc */
	static char[] guessed;
	/** Stores the word */
	static char[] word;
	/** Counts how many steps the computer takes */
	static int step = 0;
	/** Counts how many right guesses the computer makes */
	static int right = 0;
	/** Counts how many wrong guesses the computer makes */
	static int wrong = 0;
	
	public static void main (String[] args) {
		Scanner k = new Scanner(System.in);
		
		System.out.println("Hangman solver by JKamue");
		System.out.println("Please enter an English word");
		System.out.println("You are allowed to use each letter in the alphabet but no spaces");
		
		// Receive word and convert it to char array
		String input = k.next();
		word = input.toLowerCase().toCharArray();
		
		// Create the array containing the guessed values
		guessed = new char[word.length];
		Arrays.fill(guessed,HangManSolver.empty);
		
		System.out.println("How hard do you think your word is?");
		System.out.println("   1. Simple frequently used word");
		System.out.println("   2. Known word");
		System.out.println("   3. Rarely used word");
		System.out.println("   4. Highly specific word");
		
		// Receive the wordlist
		String wordlist = HangManSolver.lists[k.nextInt()-1]+".txt";
		
		// Loop till the word is found
		System.out.println("-------------------");
		while (HangManSolver.contains(guessed,HangManSolver.empty)) {
			// Make a guess for the next character based on the wordlist
			char next = HangManSolver.nextGuess(guessed, checked, wordlist);
			System.out.println("Guessing "+next);
			if (checkLetter(next)) {
				System.out.println("The character was in the word");
				right+=1;
			} else {
				System.out.println("The character was not in the word");
				wrong+=1;
			}
			System.out.println("-------------------");	
		}
		
		// Print statistics for user
		System.out.println("\nThe word was: " + stringify(guessed));
		System.out.println("It took the computer " + step + " turns");
		System.out.println("It made " + wrong + " mistakes\n");
		
		// Print "Winner"
		if(wrong > 5) {
			System.out.println("You won :/");
		} else {
			System.out.println("The computer won!!");
		}
		
		k.close();
	}
	
	/**
	 * Checks if a character is in the defined word
	 * 
	 * @param letter The character that should be checked
	 * @return True when it is in the word || False when it is not in the word
	 */
	public static boolean checkLetter(char letter) {
		checked[step] = letter;
		step+=1;
		boolean ret = false;
		for (int i = 0; i < word.length; i++) {
			if (word[i] == letter) {
				guessed[i] = letter;
				ret = true;
			}
		}
		System.out.println(stringify(guessed));
		return ret;
	}
	
	/**
	 * Converts a char array into a string
	 * 
	 * @param tmp The char array
	 * @return Char array as string
	 */
	public static String stringify(char[] tmp) {
		String ret = "";
		for (int i=0; i<tmp.length; i++) {
			ret += tmp[i]+" ";
		}
		return ret;
	}
}
