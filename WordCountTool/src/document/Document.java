package document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * A class that represents a text document.
 * It does one pass through the document to count the number of syllables, words, 
 * and sentences and then stores those values.
 * 
 * @author Mickael Grivolat
 */

public class Document {

	private String text;
	private int numWords;  // The number of words in the document
	private int numSentences;  // The number of sentences in the document
	private int numSyllables;  // The number of syllables in the document
	private int numCharacters; // The number of characters in the document
	
	/** Create a new document from the given text.*/
	public Document(String text)
	{
		this.text = text;
		processText();
	}
	
	/** Return the entire text of this document */
	public String getText() {
		return this.text;
	}
	/**
	 * Get the number of sentences in the document.
	 * Sentences are defined as contiguous strings of characters ending in an 
	 * end of sentence punctuation (. ! or ?) or the last contiguous set of 
	 * characters in the document, even if they don't end with a punctuation mark.
	 */
	public int getNumSentences() {
		return numSentences;
	}
	/**
	 * Get the number of words in the document.
	 * A "word" is defined as a contiguous string of alphabetic characters
	 * i.e. any upper or lower case characters a-z or A-Z.  This method completely 
	 * ignores numbers when you count words, and assumes that the document does not have 
	 * any strings that combine numbers and letters.
	 */
	public int getNumWords() {
	    return numWords;
	}
	/**
	 * Get the total number of syllables in the document (the stored text). 
	 * To calculate the number of syllables in a word, we use the following rules:
	 *       Each contiguous sequence of one or more vowels is a syllable, 
	 *       with the following exception: a lone "e" at the end of a word 
	 *       is not considered a syllable unless the word has no other syllables. 
	 *       The letter y is considered a vowel.
	 */
	public int getNumSyllables() {
        return numSyllables;
	}
	/**
	 * Get the total number of characters in the document (the stored text). 
	 */
	public int getNumCharacters() {
        return numCharacters;
	}
	
	/** Returns the tokens that match the regex pattern from the document text string.*/
	private List<String> getTokens(String pattern)
	{
		ArrayList<String> tokens = new ArrayList<String>();
		Pattern tokSplitter = Pattern.compile(pattern);
		Matcher m = tokSplitter.matcher(text);
		
		while (m.find()) {
			tokens.add(m.group());
		}
		
		return tokens;
	}
	
	/** 
	 * Take a string that either contains only alphabetic characters, or only sentence-ending punctuation.
	 * Return true if the string or only sentence-ending punctuation.
	 * Return true if the string contains only alphabetic characters, and false if it contains end of sentence punctuation.
	 */
	private boolean isWord(String tok)
	{
		return !(tok.indexOf("!") >=0 || tok.indexOf(".") >=0 || tok.indexOf("?")>=0);
	}
	
    /** Passes through the text one time to count the number of words, syllables 
     *  and sentences, and set the member variables appropriately.
     */
	private void processText()
	{
		// Call getTokens on the text to preserve separate strings that are either words or sentence-ending punctuation.
		// Ignores everything that is not a word or a sentence-ending punctuation.
		List<String> tokens = getTokens("[!?.]+|[a-zA-Z]+");
		
		int numWord = 0, numSyllab = 0, numSent = 0;
		for(String str: tokens) {
			if(isWord(str)) {
				numSyllab += countSyllables(str);
				numWord++;
				if(str.equals(tokens.get(tokens.size()-1))) {
					numSent++;
				}
			} else {
				numSent++;
			}
		}
		numWords = numWord;
		numSyllables = numSyllab;
		numSentences = numSent;
		numCharacters = getText().length();
	}	
	
	/** This is a helper function that returns the number of syllables
	 * in a word. 
	 * The number of syllables in the given word, according to this rule:
	 *       Each contiguous sequence of one or more vowels is a syllable, 
	 *       with the following exception: a lone "e" at the end of a word 
	 *       is not considered a syllable unless the word has no other syllables. 
	 *       The letter y is considered a vowel.
	 */
	private int countSyllables(String word)
	{
			int numSyllables = 0;
			boolean newSyllable = true;
			String vowels = "aeiouy";
			char[] cArray = word.toCharArray();
			for (int i = 0; i < cArray.length; i++)
			{
			    if (i == cArray.length-1 && Character.toLowerCase(cArray[i]) == 'e' 
			    		&& newSyllable && numSyllables > 0) {
	                numSyllables--;
	            }
			    if (newSyllable && vowels.indexOf(Character.toLowerCase(cArray[i])) >= 0) {
					newSyllable = false;
					numSyllables++;
				}
				else if (vowels.indexOf(Character.toLowerCase(cArray[i])) < 0) {
					newSyllable = true;
				}
			}

			return numSyllables;
	}
	
	public static void main(String[] args) throws IOException {
		ReadWriteFile file = new ReadWriteFile("src\\Test File.txt");
		Document doc = new Document(file.getContent());
		System.out.print(file.getContent());
		System.out.println("==================================================");
		System.out.println("Number of sentences: " + doc.getNumSentences());
		System.out.println("Number of words: " + doc.getNumWords());
		System.out.println("Number of syllables: " + doc.getNumSyllables());
	}
	

}
