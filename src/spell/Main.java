package spell;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import spell.SpellCorrector.NoSimilarWordFoundException;

/**
 * A simple main class for running the spelling corrector
 */
public class Main {
	
	/**
	 * Give the dictionary file name as the first argument and the word to correct
	 * as the second argument.
	 */
	public static void main(String[] args) throws  IOException, NoSimilarWordFoundException {//fix IO exception
		
		//String dictionaryFileName = args[0];
		//String inputWord = args[1];
		
		String dictionaryFileName = "http://localhost:8081/knowndata/ethnicities.txt";
		String inputWord = "alaskanative";
		
		System.out.println(dictionaryFileName + "  " + inputWord);
		
		
		/**
		 * Create an instance of your corrector here
		 */
		MySpellCorrector corrector = new MySpellCorrector();
		
		corrector.useDictionary(dictionaryFileName);
		List<String> suggestions = corrector.suggestSimilarWords(inputWord);
			
		if (suggestions != null)
			System.out.println(suggestions.toString());

		
	}

}
